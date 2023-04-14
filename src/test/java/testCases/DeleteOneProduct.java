package testCases;


import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class DeleteOneProduct {
	String baseURI;
	SoftAssert softAssert;
	
	HashMap<String, String> deletePayload;
	
	public DeleteOneProduct() {
		baseURI="https://techfios.com/api-prod/api/product";
		
		softAssert= new SoftAssert();
		
		deletePayload= new HashMap<String,String>();
		
	}
	public Map<String, String> deletePayloadMap() {
		
		
		deletePayload.put("id", "7496");
		
		
		return deletePayload;
	}
	@Test(priority=1)
	
	public void deleteOneProduct() {

		Response response=
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json; charset=UTF-8")
			.header("Authorization", "Bearer puttokenhere")
			.body(deletePayloadMap()).
			
			
		
		when()
		
			.delete("/delete.php").
		
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
	
		softAssert.assertEquals(productmessage, "Product was deleted.", "Product message not matching");
		
		softAssert.assertAll();
		
	}
	@Test(priority=2)

	public void readOneProduct() {
		
		
	String 	deleteProductID=deletePayloadMap().get("id");
		Response response=
		given()
			.baseUri(baseURI)
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer puttokenhere")
			.queryParam("id", deleteProductID).
		
		when()
		
			.get("/read_one.php").
		
		then()

		.extract().response();
		int statusCode= response.getStatusCode();
		System.out.println("Status Code: " + statusCode );

		softAssert.assertEquals(statusCode, 404, "Status code not matching!");
		
		
		String responseBody=response.getBody().asString();
		System.out.println("Responce body: " + responseBody ); 
		
		
		JsonPath jp= new JsonPath(responseBody);
		String productMessage= jp.get("message");
		System.out.println("Actual product name: " + productMessage );
		
		softAssert.assertEquals(productMessage, "Product does not exist.", "Product message not matching");
		
		
		
		softAssert.assertAll();
		
	}
}
