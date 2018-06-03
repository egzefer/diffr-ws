package com.egzefer.diffr.controller;

import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiffControllerIT {

	private static final String JOHN_DOE_JSON_ENCODED = "ewoJImZpcnN0TmFtZSI6IkpvaG4iLAoJImxhc3ROYW1lIjoiRG9lIgp9";

	@BeforeClass
	public static void setUp() {
		String port = System.getProperty("server.port");
		if (port == null) {
			RestAssured.port = Integer.valueOf(8080);
		}
		else{
			RestAssured.port = Integer.valueOf(port);
		}

		String basePath = System.getProperty("server.base");
		if(basePath==null){
			basePath = "/v1/diff";
		}
		RestAssured.basePath = basePath;

		String baseHost = System.getProperty("server.host");
		if(baseHost==null){
			baseHost = "http://localhost";
		}
		RestAssured.baseURI = baseHost;

	}

	@Test
	public void addDiff_leftSide_createdStatusReturned() {
		given()
			.body(JOHN_DOE_JSON_ENCODED)
			.when().post("/1/left").then()
			.statusCode(201);
	}

	@Test
	public void getDiffContent() {
	}

	@Test
	public void getDiffContent_emptySet_noContentStatusReturned() {
		given().when().get("/1/right").then().statusCode(404);
	}

	@Test
	public void getDecodedDiffContent() {

	}

}