package TestRestProject.TestRestProject;

import static io.restassured.RestAssured.form;

import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;
import junit.framework.Assert;

public class CourcesSum {

	
	@Test
	public void sumOfAll() {
		JsonPath js = new JsonPath(payload.CoursePrice());
		int purchaseAmount= js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		int count = js.getInt("courses.size()"),sum =0;
		System.out.println(count);
	
		for(int i=0; i< count; i++)
		{
			int indSum= js.getInt("courses["+i+"].Price");
			int indCopies= js.getInt("courses["+i+"].copies");
			int amount= indSum*indCopies;
			sum=sum+ amount;
			
		}
		System.out.println(sum);
		
		Assert.assertEquals(purchaseAmount, sum);

		
	}

}
