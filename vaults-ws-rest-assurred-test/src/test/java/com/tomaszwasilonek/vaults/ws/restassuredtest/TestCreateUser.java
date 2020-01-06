package com.tomaszwasilonek.vaults.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import groovy.json.JsonException;
import io.restassured.RestAssured;
import io.restassured.response.Response;

class TestCreateUser {
	
	private final String CONTEXT_PATH = "/rest/v1";
	
	private final String FIRST_NAME_KEY = "firstName";
	private final String LAST_NAME_KEY = "lastName";
	private final String EMAIL_KEY = "email";
	private final String PASSWORD_KEY = "password";
	private final String USER_ID_KEY = "userId";

	private final String FIRST_NAME = "RestAssured";
	private final String LAST_NAME = "User";
	private final String EMAIL = "rau@test.pl";
	private final String PASSWORD = "1234";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8888;
	}

	@Test
	void testCreateUser() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put(FIRST_NAME_KEY, FIRST_NAME);
		userDetails.put(LAST_NAME_KEY, LAST_NAME);
		userDetails.put(EMAIL_KEY, EMAIL);
		userDetails.put(PASSWORD_KEY, PASSWORD);
			
		Response response = given().
			contentType("application/json").
			accept("application/json").
			body(userDetails).
		when().
			post(CONTEXT_PATH + "/users").
		then().
			statusCode(200).
			contentType("application/json").
			extract().
			response();
		
		String userId = response.jsonPath().getString(USER_ID_KEY);
		assertNotNull(userId);
		
		String firstName = response.jsonPath().getString(FIRST_NAME_KEY);
		assertEquals(FIRST_NAME, firstName);
		
		String lastName = response.jsonPath().getString(LAST_NAME_KEY);
		assertEquals(LAST_NAME, lastName);
		
		String email = response.jsonPath().getString(EMAIL_KEY);
		assertEquals(EMAIL, email);
	}
	
	// TODO: test failing CreateUser when user already exists (send request again after creating a user successfully)

}
