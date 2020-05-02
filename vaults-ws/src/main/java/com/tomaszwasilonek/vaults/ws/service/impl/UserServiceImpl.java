package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.exceptions.EntityNotFoundException;
import com.tomaszwasilonek.vaults.ws.exceptions.RecordAlreadyExistsException;
import com.tomaszwasilonek.vaults.ws.repositories.UserRepository;
import com.tomaszwasilonek.vaults.ws.service.UserService;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserDto;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserVaultService userVaultService;

	@Override
	public UserDto createUser(UserDto user) {

		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RecordAlreadyExistsException(UserEntity.class, "email", user.getEmail());
		}

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(UUID.randomUUID().toString());

		return saveAndReturnStoredUserDetails(userEntity);
	}

	@Override
	public UserDto updateUser(String userId, UserDto user) {

		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			 throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		return saveAndReturnStoredUserDetails(userEntity);
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();

		// paging should start with 1
		if (page > 0)
			page--;

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "email", email);
		}

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "email", email);
		}

		return mapUserEntityToUserDto(userEntity);
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		return mapUserEntityToUserDto(userEntity);
	}

	@Override
	public UserVaultDto createVault(String userId, UserVaultDto vault) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		UserVaultDto createdVault = userVaultService.createVault(userEntity, vault);
		System.out.println("Vault " + createdVault);
		return createdVault;
	}

	@Override
	public List<UserVaultDto> getVaults(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		List<UserVaultDto> returnValue = userVaultService.getVaults(userEntity);
		return returnValue;
	}

	@Override
	public UserVaultDto getVaultByVaultId(String userId, String vaultId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		UserVaultDto returnValue = userVaultService.getVault(vaultId);
		return returnValue;
	}

	@Override
	public UserVaultDto updateVault(String userId, String vaultId, UserVaultDto vaultDetails) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		UserVaultDto returnValue = userVaultService.updateVault(vaultId, vaultDetails);
		return returnValue;
	}

	@Override
	public void deleteVault(String userId, String vaultId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) {
			throw new EntityNotFoundException(UserEntity.class, "id", userId);
		}

		userVaultService.deleteVault(vaultId);
	}

	private UserDto saveAndReturnStoredUserDetails(UserEntity user) {
		UserEntity storedUserDetails = userRepository.save(user);
		return mapUserEntityToUserDto(storedUserDetails);
	}

	private UserDto mapUserEntityToUserDto(UserEntity user) {
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(user, returnValue);
		return returnValue;
	}
}
