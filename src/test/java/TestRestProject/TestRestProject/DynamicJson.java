package TestRestProject.TestRestProject;

import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.payload;
import files.reUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

	// 1. given() - specify queryParam, header and body of POST request
	// 2. when() - "resource" will be POST in this case and "http method"
	// 3. then() - validation/Assertion
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle ) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
				.body(payload.addBook(isbn, aisle)).when().post("/Library/Addbook.php").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js = reUsableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);

	}

	@DataProvider(name="BooksData")
	public Object[][] getData() {
		//array collection of elements
		return new Object[][] {{"1234a","1234"},{"1111a","1111"},{"2222a","2222"}};
 
		
	}

}
