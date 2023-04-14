package testCases;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class CreateOneProductUsingHashMap {
	String baseURI;
	SoftAssert softAssert;
	String firstProductID;
	String createPayloadFilepath;
	HashMap<String, String> createpayload;

	public CreateOneProductUsingHashMap() {
		baseURI = "https://techfios.com/api-prod/api/product";

		softAssert = new SoftAssert();
		createPayloadFilepath = "src\\main\\java\\data\\CreatePayload.json";
		createpayload = new HashMap<String, String>();

	}

	public Map<String, String> createPayloadMap() {

		createpayload.put("name", "Sudip's Amazing Pillow ONSALE HashMap");
		createpayload.put("price", "299");
		createpayload.put("description", "Most amazing programmers Buy 2 get 1 Free.");
		createpayload.put("catagory_id", "2");
		createpayload.put("catagory_name", "Electronics");

		return createpayload;

	}

	@Test(priority = 1)

	public void createOneProduct() {

		Response response = 
				given().baseUri(baseURI).header("Content-Type", "application/json; charset=UTF-8")
				.header("Authorization", "Bearer 'puttokenhere' ")
				.body(createPayloadMap())
				.when().post("/create.php").

				then()

				.extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);

		softAssert.assertEquals(statusCode, 201, "Status code not matching!");

		long responseTime = response.getTime();

		if (responseTime <= 2000) {
			System.out.println("Responce Time is within range.");
		} else {
			System.out.println("Responcetime is out of rance.");
		}

		String responseheaderContentType = response.getHeader("Content-Type");
		System.out.println("Response header Content-Type: " + responseheaderContentType);

		softAssert.assertEquals(responseheaderContentType, "application/json; charset=UTF-8");

		String responseBody = response.getBody().asString();
		System.out.println("Responce body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		String productmessage = jp.get("message");
		System.out.println("Product message: " + productmessage);

		softAssert.assertEquals(productmessage, "Product was created.", "Product message not matching");

		softAssert.assertAll();
	}

	@Test(priority = 2)

	public void readAllProducts() {

		Response response = given().baseUri("https://techfios.com/api-prod/api/product")
				.header("Content-Type", "application/json; charset=UTF-8").auth().preemptive()
				.basic("demo2techfios.com", "abc123").

				when()

				.get("/read.php").

				then()

				.extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("Responce body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);

		firstProductID = jp.get("records[0].id");
		System.out.println("First product ID: " + firstProductID);

	}

	@Test(priority = 3)

	public void readOneProduct() {

		Response response = given().baseUri(baseURI).header("Content-Type", "application/json")
				.header("Authorization", "Bearer puttokenhere").queryParam("id", firstProductID).

				when()

				.get("/read_one.php").

				then()

				.extract().response();

		String expectedProductName = createPayloadMap().get("name");

		String expectedProductDescription = createPayloadMap().get("description");

		String expectedProductPrice = createPayloadMap().get("price");

		String responseBody = response.getBody().asString();
		System.out.println("Responce body: " + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String actualproductName = jp.get("name");
		System.out.println("Actual product name: " + actualproductName);

		softAssert.assertEquals(actualproductName, expectedProductName, "Product name not matching");

		String actualProductDescription = jp.get("description");
		System.out.println("Actual Product description: " + actualProductDescription);

		softAssert.assertEquals(actualProductDescription, expectedProductDescription,
				"Product description not matching!");

		String actualProductPrice = jp.get("price");
		System.out.println("Actual Product price: " + actualProductPrice);
		softAssert.assertEquals(actualProductPrice, expectedProductPrice, "price not matching!");

		softAssert.assertAll();

	}
}