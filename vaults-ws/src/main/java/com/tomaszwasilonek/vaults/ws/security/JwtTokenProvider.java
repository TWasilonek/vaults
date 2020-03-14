package com.tomaszwasilonek.vaults.ws.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.service.UserService;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	
	@Autowired
	UserService userService;

	public String generateToken(Authentication authentication) {
		String userName = ((User) authentication.getPrincipal()).getUsername();

		UserDto userDto = userService.getUser(userName);

		Date now = new Date(System.currentTimeMillis());
		Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
		String userId = userDto.getUserId();

		Map<String, Object> claims = new HashMap<>();
		claims.put("id", userId);
		claims.put("username", userDto.getEmail());
		claims.put("fullName", userDto.getFullName());

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
	}

	public boolean validateToken(String token) {
		try {

			Jwts.parser()
				.setSigningKey(SecurityConstants.getTokenSecret())
				.parseClaimsJws(token);

			return true;

		} catch (SignatureException ex) {
			System.out.println("Invalid JWT Signature");
		} catch (MalformedJwtException ex) {
			System.out.println("Invalid JWT Token");
		} catch (ExpiredJwtException ex) {
			System.out.println("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			System.out.println("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			System.out.println("JWT claims string is empty");
		}
		return false;
	}

	public String getUserIdFromJWT(String token) {
		return Jwts.parser()
				.setSigningKey(SecurityConstants.getTokenSecret())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}
