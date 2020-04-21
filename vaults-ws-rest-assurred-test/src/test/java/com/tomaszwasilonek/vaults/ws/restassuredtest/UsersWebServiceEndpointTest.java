package com.tomaszwasilonek.vaults.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(OrderAnnotation.class)
class UsersWebServiceEndpointTest {
	
	private final String CONTEXT_PATH = "/api";
	
	private final String FIRST_NAME_KEY = "firstName";
	private final String LAST_NAME_KEY = "lastName";
	private final String EMAIL_KEY = "email";
	private final String PASSWORD_KEY = "password";
	private final String USER_ID_KEY = "userId";

	private final String FIRST_NAME = "RestAssured";
	private final String LAST_NAME = "User";
	private final String EMAIL = "RestAssuredTest@test.pl";
	private final String PASSWORD = "1234";
	
	private final String VAULT_NAME_KEY = "name";
	private final String VAULT_BALANCE_KEY = "balance";
	private final String VAULT_ID_KEY = "vaultId";
	private final String VAULT_NAME = "Vault1";
	
	private final String JSON = "application/json";
	
	private static String authorizationHeader;
	private static String userId;
	private static String vaultId;

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}
	
	@Test
	@Order(1)
	void testCreateUser() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put(FIRST_NAME_KEY, FIRST_NAME);
		userDetails.put(LAST_NAME_KEY, LAST_NAME);
		userDetails.put(EMAIL_KEY, EMAIL);
		userDetails.put(PASSWORD_KEY, PASSWORD);
			
		Response response = given().
				contentType(JSON).
				accept(JSON).
				body(userDetails).
			when().
				post(CONTEXT_PATH + "/users").
			then().
				statusCode(200).
				contentType(JSON).
				extract().
				response();

		assertNotNull(response.jsonPath().getString(USER_ID_KEY));
		assertEquals(FIRST_NAME, response.jsonPath().getString(FIRST_NAME_KEY));
		assertEquals(LAST_NAME, response.jsonPath().getString(LAST_NAME_KEY));
		assertEquals(EMAIL, response.jsonPath().getString(EMAIL_KEY));
	}
	
	@Test
	@Order(2)
	void testCreateUser_failingWhenUserAlreadyExists() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put(FIRST_NAME_KEY, FIRST_NAME);
		userDetails.put(LAST_NAME_KEY, LAST_NAME);
		userDetails.put(EMAIL_KEY, EMAIL);
		userDetails.put(PASSWORD_KEY, PASSWORD);
			
		given().
			contentType(JSON).
			accept(JSON).
			body(userDetails).
		when().
			post(CONTEXT_PATH + "/users").
		then().
			statusCode(400);
	}
	
	@Test
	@Order(2)
	void testCreateUser_failingRequiredFields() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put(FIRST_NAME_KEY, "");
		userDetails.put(LAST_NAME_KEY, "");
		userDetails.put(EMAIL_KEY, "");
		userDetails.put(PASSWORD_KEY, "");
			
		Response response = given().
			contentType(JSON).
			accept(JSON).
			body(userDetails).
		when().
			post(CONTEXT_PATH + "/users").
		then().
			statusCode(400).
			extract().response();
		
		assertEquals(5, response.jsonPath().getList(("apierror.subErrors")).size());
	}
	
	@Test
	@Order(3)
	void testUserLogin() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put(EMAIL_KEY, EMAIL);
		userDetails.put(PASSWORD_KEY, PASSWORD);
		
		Response response = given().
				contentType(JSON).
				accept(JSON).
				body(userDetails).
			when().
				post(CONTEXT_PATH + "/login").
			then().
				statusCode(200).
				extract().response();
		
		authorizationHeader = response.header("Authorization");
		// TODO: Get the userId from token
//		userId = response.header("UserID");
		
		assertNotNull(authorizationHeader);
		assertNotNull(userId);
	}
	
	@Test
	@Order(4)
	void testGetUserDetails() {
		Response response = given().
				header("Authorization", authorizationHeader).
				contentType(JSON).
				accept(JSON).
			when().
				get(CONTEXT_PATH + "/users/" + userId).
			then().
				statusCode(200).
				extract().response();
		
		assertEquals(FIRST_NAME, response.jsonPath().getString(FIRST_NAME_KEY));
		assertEquals(LAST_NAME, response.jsonPath().getString(LAST_NAME_KEY));
		assertEquals(EMAIL, response.jsonPath().getString(EMAIL_KEY));
	}
	
	@Test
	@Order(5)
	void testUpdateUser() {
		Map<String, Object> userDetails = new HashMap<>();
		userDetails.put(FIRST_NAME_KEY, "Updated first name");
		userDetails.put(LAST_NAME_KEY, "Updated last name");
		userDetails.put(EMAIL_KEY, EMAIL);
		
		Response response = given().
				header("Authorization", authorizationHeader).
				contentType(JSON).
				accept(JSON).
				body(userDetails).
			when().
				put(CONTEXT_PATH + "/users/" + userId).
			then().
				statusCode(200).
				extract().response();
		
		assertEquals("Updated first name", response.jsonPath().getString(FIRST_NAME_KEY));
		assertEquals("Updated last name", response.jsonPath().getString(LAST_NAME_KEY));
		assertEquals(EMAIL, response.jsonPath().getString(EMAIL_KEY));
	}
	
	
	@Test
	@Order(100) // ensure this test runs last
	void testDeleteUser() {
		given().
			header("Authorization", authorizationHeader).
			contentType(JSON).
			accept(JSON).
		when().
			delete(CONTEXT_PATH + "/users/" + userId).
		then().
			statusCode(200);
	}
	
	/**
	 * Vaults related tests
	 */
	@Test
	@Order(10)
	void testCreateVault() {
		Map<String, Object> vaultDetails = new HashMap<>();
		vaultDetails.put(VAULT_NAME_KEY, VAULT_NAME);
		
		Response response = given().
				header("Authorization", authorizationHeader).
				contentType(JSON).
				accept(JSON).
				body(vaultDetails).
			when().
				post(CONTEXT_PATH + "/users/" + userId + "/vaults").
			then().
				statusCode(200).
				extract().response();
		
		vaultId = response.jsonPath().getString(VAULT_ID_KEY);
		
		assertEquals(VAULT_NAME, response.jsonPath().getString(VAULT_NAME_KEY));
		assertEquals(0.00, response.jsonPath().getDouble(VAULT_BALANCE_KEY));
		assertNotNull(vaultId);
	}
	
	@Test
	@Order(11)
	void testGetVaults() {
		Response response = given().
				header("Authorization", authorizationHeader).
				contentType(JSON).
				accept(JSON).
			when().
				get(CONTEXT_PATH + "/users/" + userId + "/vaults").
			then().
				statusCode(200).
				extract().response();
		
		List<Map<Object, Object>> vaults = response.jsonPath().getList("");
		
		assertNotNull(vaults);
		assertTrue( vaults.size() == 1 );
		assertEquals(VAULT_NAME, vaults.get(0).get(VAULT_NAME_KEY));
	}
	
	@Test
	@Order(12)
	void testUpdateVault() {
		Map<String, Object> vaultDetails = new HashMap<>();
		vaultDetails.put(VAULT_NAME_KEY, "Updated vault name");
		
		Response response = given().
				header("Authorization", authorizationHeader).
				contentType(JSON).
				accept(JSON).
				body(vaultDetails).
			when().
				put(CONTEXT_PATH + "/users/" + userId + "/vaults/" + vaultId).
			then().
				statusCode(200).
				extract().response();
		
		assertEquals("Updated vault name", response.jsonPath().getString(VAULT_NAME_KEY));
		assertEquals(0.00, response.jsonPath().getDouble(VAULT_BALANCE_KEY));
	}
	
	@Test
	@Order(20) // ensure this test runs as last of all the vaults-related tests
	void testDeleteVault() {
		given().
			header("Authorization", authorizationHeader).
			contentType(JSON).
			accept(JSON).
		when().
			delete(CONTEXT_PATH + "/users/" + userId + "/vaults/" + vaultId).
		then().
			statusCode(200);
	}

}
