package TestRestProject.TestRestProject;

import static io.restassured.RestAssured.form;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.payload;
import files.reUsableMethods;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class jiraTest {

	@Test
	public static void addComment() {

		// 1. given() - specify queryParam, header and body of POST request
		// 2. when() - "resource" will be POST in this case and "http method"
		// 3. then() - validation/Assertion

		RestAssured.baseURI = "http://localhost:8080";

		// login scenario
		SessionFilter sessionFilter = new SessionFilter(); // this will get the session of the subsiquent request and
															// save it in filter
		String response = given().relaxedHTTPSValidation().header("Content-Type", "application/json")
				.body("{ \"username\": \"ahmed.usman_shani\", \"password\": \"redhat11!\" }").log().all()
				.filter(sessionFilter).when().post("/rest/auth/1/session").then().log().all().assertThat()
				.statusCode(200).extract().asString();
		
		String expectedMessage ="Commnet Added by Ahmed via RestAssuredJavaProject";



		System.out.println(response);
		JsonPath js =new JsonPath(response);

		String commentId= js.getString("id");

		// add comment to an existing issue
		given().pathParam("key", "10025").log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"Commnet Added by Ahmed via RestAssuredJavaProject\",\r\n"
						+ "    \"visibility\": {\r\n" + "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n" + "    }\r\n" + "}")
				.filter(sessionFilter).when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat()
				.statusCode(201);
		
		// add attachment to an existing issue
		given().header("X-Atlassian-Token", "no-check").filter(sessionFilter).pathParam("key", "10025")
				.header("Content-Type", "multipart/form-data").multiPart("file", new File("jira.txt")).when()
				.post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

		// get issue
		String issueDetails = given().filter(sessionFilter).pathParam("key", "10025")
				.queryParam("fields", "comment").queryParam("fields", "Comment")
				.log().all().when().get("/rest/api/2/issue/{key}").then()
				.log().all().extract().response().asString();

		System.out.println(issueDetails);

		JsonPath js1 =new JsonPath(issueDetails);
		int commentsCount=js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++)
		{
			String commentIdIssue =js1.get("fields.comment.comments["+i+"].id").toString();
				if (commentIdIssue.equalsIgnoreCase(commentId))
					{
						String message= js1.get("fields.comment.comments["+i+"].body").toString();
						System.out.println(message);

						Assert.assertEquals(message, expectedMessage);
					}

		}
	
	
	}
	}

