package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;
import com.tomaszwasilonek.vaults.ws.exceptions.VaultsServiceException;
import com.tomaszwasilonek.vaults.ws.repositories.UserVaultRepository;
import com.tomaszwasilonek.vaults.ws.service.UserVaultsService;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultsDto;
import com.tomaszwasilonek.vaults.ws.ui.model.response.ErrorMessages;

@Component
public class UserVaultsServiceImpl implements UserVaultsService {
	
	@Autowired
	UserVaultRepository userVaultsRepository;

	@Autowired
	Utils utils;
	

	@Override
	public UserVaultsDto createVault(UserEntity user, UserVaultsDto vault) {
		
		if (userVaultsRepository.findByName(vault.getName()) != null) {
			throw new VaultsServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
		};
		
		UserVault vaultsEntity = new UserVault();
		BeanUtils.copyProperties(vault, vaultsEntity);
		
		String publicVaultId = utils.generateVaultId(30);
		vaultsEntity.setName(vault.getName());
		vaultsEntity.setVaultId(publicVaultId);
		vaultsEntity.setUserDetails(user);
		vaultsEntity.setBalance(0.00);
		
		return saveAndReturnStoredVaultDetails(vaultsEntity);
	}
	
	@Override
	public List<UserVaultsDto> getVaults(UserEntity user) {
		List<UserVaultsDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		Iterable<UserVault> vaults = userVaultsRepository.findAllByUserDetails(user);
		
		for (UserVault vaultsEntity : vaults) {
			returnValue.add(modelMapper.map(vaultsEntity, UserVaultsDto.class));
		}
		
		return returnValue;
	}

	@Override
	public UserVaultsDto getVault(String vaultId) {
		UserVault vault = userVaultsRepository.findByVaultId(vaultId);
		
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		return mapVaultsEntityToVaultsDto(vault);
	}

	@Override
	public UserVaultsDto updateVault(String vaultId, UserVaultsDto vaultDetails) {
		UserVault vault = userVaultsRepository.findByVaultId(vaultId);
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		vault.setName(vaultDetails.getName());
		
		return saveAndReturnStoredVaultDetails(vault);
	}

	@Override
	public void deleteVault(String vaultId) {
		UserVault vault = userVaultsRepository.findByVaultId(vaultId);
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userVaultsRepository.delete(vault);
	}

	private UserVaultsDto saveAndReturnStoredVaultDetails(UserVault vault) {
		UserVault storedVaultDetails = userVaultsRepository.save(vault);
		return mapVaultsEntityToVaultsDto(storedVaultDetails);
	}
	
	private UserVaultsDto mapVaultsEntityToVaultsDto(UserVault vault) {
		UserVaultsDto returnValue = new UserVaultsDto();
		BeanUtils.copyProperties(vault, returnValue);
		return returnValue;
	}

}
