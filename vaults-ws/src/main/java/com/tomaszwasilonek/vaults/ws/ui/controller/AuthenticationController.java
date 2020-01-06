package com.tomaszwasilonek.vaults.ws.ui.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ResponseHeader;

import com.tomaszwasilonek.vaults.ws.ui.model.request.LoginRequestModel;

/**
 * 
 * @author tomek
 * This controller is a workaround to show the auth endpoints documentation in Swagger UI.
 * The real auth endpoints are implemented by Spring Security.
 */
@RestController
public class AuthenticationController {
	
	@ApiOperation("User Login")
	@ApiResponses(value = {
			@ApiResponse(code = 200,
					message="Response Headers",
					responseHeaders = {
							@ResponseHeader(name = "authorization",
									description = "Bearer <JWT value here>",
									response = String.class),
							@ResponseHeader(name = "userID",
								description = "<Public User Id value here>",
								response = String.class),
					})
	})
	@PostMapping("/login")
	public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel) {
		throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");
	}

}
