package testCases;


import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class CreateOneProduct {
	String baseURI;
	SoftAssert softAssert;
	String firstProductID;
	String createPayloadFilepath;
	
	public CreateOneProduct() {
		baseURI="https://techfios.com/api-prod/api/product";
		
		softAssert= new SoftAssert();
		createPayloadFilepath= "src\\main\\java\\data\\CreatePayload.json";
	}
	@Test(priority=1)
	
	public void createOneProduct() {

		Response response=
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset=UTF-8")
			.header("Authorization", "Bearer 'puttokenhere' ")
	
			.body(new File(createPayloadFilepath)).
		when()
//				.log().all()
			.post("/create.php").
		
		then()
//			.statusCode(200).header("Content-Type", "application/json:charset=UTF-8");
		
		.extract().response();
		
		int statusCode= response.getStatusCode();
		System.out.println("Status Code: " + statusCode );
	//	Assert.assertEquals(statusCode, 200);  this is hard Assert
		softAssert.assertEquals(statusCode, 201);
		
	//	response.getTime();
		long responseTime= response.getTime();
		
		if(responseTime<=2000) {
			System.out.println("Responce Time is within range.");
		}else {
			System.out.println("Responcetime is out of rance.");
		}


		
		String responseheaderContentType= response.getHeader("Content-Type");
		System.out.println("Response header Content-Type: " +responseheaderContentType );
		


		softAssert.assertEquals(responseheaderContentType, "application/json; charset=UTF-8");
		
		
		String responseBody=response.getBody().asString();
		System.out.println("Responce body: " + responseBody ); 
		
		
		
		JsonPath jp= new JsonPath(responseBody);
	
		
		String productmessage= jp.get("message");
		System.out.println("Product message: " + productmessage );
	
		softAssert.assertEquals(productmessage, "Product was created.", "Product message not matching");
		
		
		softAssert.assertAll();
	}
@Test(priority=2)
	
	public void readAllProducts() {

		Response response=
		given()
			.baseUri("https://techfios.com/api-prod/api/product")
			.header("Content-Type", "application/json; charset=UTF-8")
			.auth().preemptive().basic("demo2techfios.com", "abc123").
		 
		when()
	
		.get("/read.php").
		
		then()

		
		.extract().response();
		
		
		String responseBody=response.getBody().asString();
		System.out.println("Responce body: " + responseBody ); 
		
		
		
		JsonPath jp= new JsonPath(responseBody); 
	
		
		firstProductID= jp.get("records[0].id");
		System.out.println("First product ID: " + firstProductID );
		
		
		
	}
@Test(priority=3)

public void readOneProduct() {

	Response response=
	given()
		.baseUri(baseURI)
		.header("Content-Type", "application/json")
		.header("Authorization", "Bearer puttokenhere")
		.queryParam("id", firstProductID).
	
	when()
	
		.get("/read_one.php").
	
	then()

	.extract().response();
	
	
	JsonPath jp2= new JsonPath(createPayloadFilepath);
	String expectedProductName= jp2.get("name");
	
	String expectedProductDescription= jp2.get("description");
	
	String expectedProductPrice= jp2.get("price");
	
	String responseBody=response.getBody().asString();
	System.out.println("Responce body: " + responseBody ); 
	
	
	JsonPath jp= new JsonPath(responseBody);
	String actualproductName= jp.get("name");
	System.out.println("Actual product name: " + actualproductName );
	

	softAssert.assertEquals(actualproductName, expectedProductName, "Product name not matching");
	
	String actualProductDescription= jp.get("description");
	System.out.println("Actual Product description: " + actualProductDescription );


	softAssert.assertEquals(actualProductDescription, expectedProductDescription, "Product description not matching!");
	
	String actualProductPrice= jp.get("price");
	System.out.println("Actual Product price: " + actualProductPrice );
	softAssert.assertEquals(actualProductPrice, expectedProductPrice, "price not matching!");
	
	softAssert.assertAll();
	
}
}