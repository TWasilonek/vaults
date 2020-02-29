package com.tomaszwasilonek.vaults.ws.security;

import com.tomaszwasilonek.vaults.ws.SpringApplicationContext;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 60000; // 1 min
//	public static final long EXPIRATION_TIME = 1800000; // 30 min
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/api/users";
	public static final String H2_CONSOLE = "/h2-console/**";
	
	
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
	
	
}
