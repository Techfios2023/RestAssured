package testCases;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ReadOneProduct {
	String baseURI;
	SoftAssert softAssert;

	public ReadOneProduct() {
		baseURI = "https://techfios.com/api-prod/api/product";

		softAssert = new SoftAssert();
	}

	@Test

	public void readOneProduct() {

		Response response = given().baseUri(baseURI).header("Content-Type", "application/json")
				.header("Authorization", "Bearer puttokenhere").queryParam("id", "7598").

				when()

				.get("/read_one.php").

				then()
//			.statusCode(200).header("Content-Type", "application/json:charset=UTF-8");

				.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		// Assert.assertEquals(statusCode, 200); this is hard Assert
		softAssert.assertEquals(statusCode, 200);

		
		long responseTime = response.getTime();
		System.out.println("Responce time: " + responseTime);

		if (responseTime <= 2000) {
			System.out.println("Responce Time is within range.");
		} else {
			System.out.println("Responcetime is out of range.");
		}
	

		String responseheaderContentType = response.getHeader("Content-Type");
		System.out.println("Response header Content-Type: " + responseheaderContentType);
		// Assert.assertEquals(responseheaderContentType, "application/json");
		softAssert.assertEquals(responseheaderContentType, "application/json", "Status code not matching!");


		String responseBody = response.getBody().asString();
		System.out.println("Responce body: " + responseBody);

	

		JsonPath jp = new JsonPath(responseBody);
	

		String productName = jp.get("name");
		System.out.println("First product name: " + productName);
		// Assert.assertEquals(productName, "Sudip's pillow buy one get one free");
		softAssert.assertEquals(productName, "MD's Amazing Pillow 2.0", "Product name not matching");

		String productDescription = jp.get("description");
		System.out.println("Product description: " + productDescription);
		// Assert.assertEquals(productDescription, "put your product description here");
		 softAssert.assertEquals(productDescription, "The best pillow for amazing programmers.", "Product Description not matching");

		String productPrice = jp.get("price");
		System.out.println("Product price: " + productPrice);
		softAssert.assertEquals(productPrice, "199", "price not matching!");

		softAssert.assertAll();

	}
}
