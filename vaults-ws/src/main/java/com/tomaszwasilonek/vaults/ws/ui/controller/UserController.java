package com.tomaszwasilonek.vaults.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomaszwasilonek.vaults.ws.exceptions.MissingRequiredFieldsException;
import com.tomaszwasilonek.vaults.ws.service.UserService;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultsDto;
import com.tomaszwasilonek.vaults.ws.ui.model.request.UserDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.VaultsDetailsRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.ErrorMessages;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserRest;
import com.tomaszwasilonek.vaults.ws.ui.model.response.UserVaultsRest;

@RestController
@RequestMapping("/users") // http://localhost:8888/rest/v1/users
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);

		// TODO change to modelmapper
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

		return returnValue;
	}

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto user = userService.getUserByUserId(id);
		BeanUtils.copyProperties(user, returnValue);

		return returnValue;
	}

	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();

		if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty()
				|| userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty()) {
			throw new MissingRequiredFieldsException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;
	}

	@PutMapping(path = "/{id}",
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
			throws Exception {
		UserRest returnValue = new UserRest();

		if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty()
				|| userDetails.getEmail().isEmpty()) {
			throw new MissingRequiredFieldsException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@PostMapping(path = "/{userId}/vaults",
			consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public UserVaultsRest createVault(
			@PathVariable String userId,
			@RequestBody VaultsDetailsRequestModel vaultDetails) {
		
		if (vaultDetails.getName().isEmpty()) {
			throw new MissingRequiredFieldsException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}
		
		UserVaultsRest returnValue = new UserVaultsRest();

		UserVaultsDto vaultsDto = new UserVaultsDto();
		BeanUtils.copyProperties(vaultDetails, vaultsDto);

		UserVaultsDto createdVault = userService.createVault(userId, vaultsDto);
		BeanUtils.copyProperties(createdVault, returnValue);

		return returnValue;
	}

	@GetMapping(path = "/{userId}/vaults",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserVaultsRest> getVaults(@PathVariable String userId) {

		List<UserVaultsRest> returnValue = new ArrayList<>();

		List<UserVaultsDto> vaults = userService.getVaults(userId);

		// TODO change to modelmapper
		if (vaults != null && !vaults.isEmpty()) {
			for (UserVaultsDto vaultsDto : vaults) {
				UserVaultsRest vaultModel = new UserVaultsRest();
				BeanUtils.copyProperties(vaultsDto, vaultModel);
				returnValue.add(vaultModel);
			}
		}

		return returnValue;
	}

	@GetMapping(path = "/{userId}/vaults/{vaultId}",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserVaultsRest getVault(@PathVariable String userId, @PathVariable String vaultId) {
		UserVaultsRest returnValue = new UserVaultsRest();

		UserVaultsDto vault = userService.getVaultByVaultId(userId, vaultId);
		BeanUtils.copyProperties(vault, returnValue);

		return returnValue;
	}

	@PutMapping(path = "/{userId}/vaults/{vaultId}",
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserVaultsRest updateVault(
			@PathVariable String userId,
			@PathVariable String vaultId,
			@RequestBody VaultsDetailsRequestModel vaultDetails) throws Exception {
		UserVaultsRest returnValue = new UserVaultsRest();

		UserVaultsDto vaultsDto = new UserVaultsDto();
		BeanUtils.copyProperties(vaultDetails, vaultsDto);

		UserVaultsDto updatedVault = userService.updateVault(userId, vaultId, vaultsDto);
		BeanUtils.copyProperties(updatedVault, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{userId}/vaults/{vaultId}",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteVault(@PathVariable String userId, @PathVariable String vaultId) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteVault(userId, vaultId);

		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
}
