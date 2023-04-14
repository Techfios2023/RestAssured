package testCases;


import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class ReadAllProducts {
	SoftAssert softAssert;
	@Test
	
	public void readAllProducts() {
	/*	Given: all inputs(baseURI,headers,Authorization,payload/body,QueryParams)
		When: submit api requests(http method, endpoint/resource)
		then: validate response(status code, headers, responce time,payload/body)
		https://techfios.com/api-prod/api/product /read.php
*/
		Response response=
		given()
			.baseUri("https://techfios.com/api-prod/api/product")
			.header("Content-Type", "application/json; charset=UTF-8")
			.auth().preemptive().basic("demo2techfios.com", "abc123").
		//	.log().all(). 
		when()
	//	.log().all()
		.get("/read.php").
		
		then()
//		.statusCode(200).header("Content-Type", "application/json:charset=UTF-8");
		
		.extract().response();
		
		int statusCode= response.getStatusCode();
		System.out.println("Status Code: " + statusCode );
		Assert.assertEquals(statusCode, 200);
	//	softAssert.assertEquals(statusCode, 200);
		
	//	response.getTime();
		long responseTime= response.getTime();
		
		if(responseTime<=2000) {
			System.out.println("Responce Time is within range.");
		}else {
			System.out.println("Responcetime is out of range.");
		}
	//	response.getHeader("Content-Type")
		
		String responseheaderContentType= response.getHeader("Content-Type");
		System.out.println("Response header Content-Type: " + responseheaderContentType );
		
		Assert.assertEquals(responseheaderContentType, "application/json; charset=UTF-8");
		
		//response.getBody() (changing datatype to string)
		
		String responseBody=response.getBody().asString();
		System.out.println("Responce body: " + responseBody ); 
		
		//now the response body is in string and need to convert into json path inorder to retrieve the first element
		
		JsonPath jp= new JsonPath(responseBody); 
	//	jp.get("records[0].id")-- store it in a variable
		
		String firstProductID= jp.get("records[0].id");
		System.out.println("First product ID: " + firstProductID );
		
		if(firstProductID!=null) {
			System.out.println("First product ID is not null. ");
			
		}else {
			System.out.println("First product is is null! ");
			
		}
		
	}
}
