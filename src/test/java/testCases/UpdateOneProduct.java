package testCases;


import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class UpdateOneProduct {
	String baseURI;
	SoftAssert softAssert;
	String updatePayloadFilePath;
	
	public UpdateOneProduct() {
		baseURI="https://techfios.com/api-prod/api/product";
		
		softAssert= new SoftAssert();
		
		updatePayloadFilePath="src\\main\\java\\data\\UpdatePayload.json";
	}
	@Test(priority=1)
	
	public void updateOneProduct() {

		Response response=
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset=UTF-8")
			.header("Authorization", "Bearer puttokenhere")
			.body(new File(updatePayloadFilePath)).
			
			
		
		when()
		
			.put("/update.php").
		
		then()

		
		.extract().response();
		
		int statusCode= response.getStatusCode();
		System.out.println("Status Code: " + statusCode );
	//	Assert.assertEquals(statusCode, 200);  this is hard Assert
		softAssert.assertEquals(statusCode, 200);
		
	//	response.getTime();
		long responseTime= response.getTime();
		System.out.println("Responce time: "+responseTime );
		
		if(responseTime<=2000) {
			System.out.println("Responce Time is within range.");
		}else {
			System.out.println("Responcetime is out of rance.");
		}
	//	response.getHeader("Content-Type")
		
		String responseheaderContentType= response.getHeader("Content-Type");
		System.out.println("Response header Content-Type: " +responseheaderContentType );
	//	Assert.assertEquals(responseheaderContentType, "application/json");
		softAssert.assertEquals(responseheaderContentType, "application/json; charset=UTF-8", "Status code not matching!");
		
		
		
		
		String responseBody=response.getBody().asString();
		System.out.println("Responce body: " + responseBody ); 
		
		
		
		JsonPath jp= new JsonPath(responseBody);
	
		
		String productmessage= jp.get("message");
		System.out.println("Product message: " + productmessage );
	
		softAssert.assertEquals(productmessage, "Product was updated.", "Product message not matching");
		
		softAssert.assertAll();
		
	}
	@Test(priority=2)

	public void readOneProduct() {
		
		JsonPath jp2= new JsonPath(updatePayloadFilePath);
	String 	updateProductID=jp2.get("id");

		Response response=
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer puttokenhere")
			.queryParam("id", updateProductID).
		
		when()
		
			.get("/read_one.php").
		
		then()

		.extract().response();
		
		String expectedProductName= jp2.get("name");
		
		String expectedProductDescription= jp2.get("description");
		
		String expectedProductPrice= jp2.get("price");
		
		String responseBody=response.getBody().asString();
		System.out.println("Responce body: " + responseBody ); 
		
		
		JsonPath jp= new JsonPath(responseBody);
		String updatedProductName= jp.get("name");
		System.out.println("Actual product name: " + updatedProductName );
		

		softAssert.assertEquals(updatedProductName, expectedProductName, "Product name not matching");
		
		String updatedProductDescription= jp.get("description");
		System.out.println("Updated Product description: " + updatedProductDescription );


		softAssert.assertEquals(updatedProductDescription, expectedProductDescription, "Product description not matching!");
		
		String updatedProductPrice= jp.get("price");
		System.out.println("Actual Product price: " + updatedProductPrice );
		softAssert.assertEquals(updatedProductPrice, expectedProductPrice, "price not matching!");
		
		softAssert.assertAll();
		
	}
}
