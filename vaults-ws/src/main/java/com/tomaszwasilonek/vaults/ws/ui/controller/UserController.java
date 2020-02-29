package com.tomaszwasilonek.vaults.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomaszwasilonek.vaults.ws.service.UserService;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;
import com.tomaszwasilonek.vaults.ws.ui.model.request.SignUpRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.UserDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.UserVaultDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserRest;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserVaultRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@RequestMapping("/api/users") // http://localhost:8888/api/users
public class UserController {

	@Autowired
	UserService userService;

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@GetMapping()
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		ModelMapper modelMapper = new ModelMapper();

		for (UserDto userDto : users) {
			returnValue.add(modelMapper.map(userDto, UserRest.class));
		}

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@GetMapping(path = "/{id}")
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto user = userService.getUserByUserId(id);
		BeanUtils.copyProperties(user, returnValue);

		return returnValue;
	}

	@PostMapping()
	public UserRest createUser(@Valid @RequestBody SignUpRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@PutMapping(path = "/{id}")
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
			throws Exception {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@DeleteMapping(path = "/{id}")
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@PostMapping(path = "/{userId}/vaults")
	public UserVaultRest createVault(
			@PathVariable String userId,
			@Valid @RequestBody UserVaultDetailsRequestModel vaultDetails) {
		
		UserVaultRest returnValue = new UserVaultRest();

		UserVaultDto vaultsDto = new UserVaultDto();
		BeanUtils.copyProperties(vaultDetails, vaultsDto);

		UserVaultDto createdVault = userService.createVault(userId, vaultsDto);
		BeanUtils.copyProperties(createdVault, returnValue);

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@GetMapping(path = "/{userId}/vaults")
	public List<UserVaultRest> getVaults(@PathVariable String userId) {
		List<UserVaultRest> returnValue = new ArrayList<>();
		List<UserVaultDto> vaults = userService.getVaults(userId);

		if (vaults != null && !vaults.isEmpty()) {
			ModelMapper modelMapper = new ModelMapper();
			for (UserVaultDto vaultsDto : vaults) {
				returnValue.add(modelMapper.map(vaultsDto, UserVaultRest.class));
			}
		}

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@GetMapping(path = "/{userId}/vaults/{vaultId}")
	public UserVaultRest getVault(@PathVariable String userId, @PathVariable String vaultId) {
		UserVaultRest returnValue = new UserVaultRest();

		UserVaultDto vault = userService.getVaultByVaultId(userId, vaultId);
		BeanUtils.copyProperties(vault, returnValue);

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@PutMapping(path = "/{userId}/vaults/{vaultId}")
	public UserVaultRest updateVault(
			@PathVariable String userId,
			@PathVariable String vaultId,
			@Valid @RequestBody UserVaultDetailsRequestModel vaultDetails) throws Exception {
		UserVaultRest returnValue = new UserVaultRest();

		UserVaultDto vaultsDto = new UserVaultDto();
		BeanUtils.copyProperties(vaultDetails, vaultsDto);

		UserVaultDto updatedVault = userService.updateVault(userId, vaultId, vaultsDto);
		BeanUtils.copyProperties(updatedVault, returnValue);

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${authorizationHeader.description}", paramType="header")
	})
	@DeleteMapping(path = "/{userId}/vaults/{vaultId}")
	public OperationStatusModel deleteVault(@PathVariable String userId, @PathVariable String vaultId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteVault(userId, vaultId);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
}
