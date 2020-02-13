package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;
import com.tomaszwasilonek.vaults.ws.exceptions.EntityNotFoundException;
import com.tomaszwasilonek.vaults.ws.exceptions.VaultsServiceException;
import com.tomaszwasilonek.vaults.ws.repositories.UserVaultRepository;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;
import com.tomaszwasilonek.vaults.ws.ui.model.response.ErrorMessages;

@Component
public class UserVaultServiceImpl implements UserVaultService {
	
	@Autowired
	UserVaultRepository userVaultsRepository;

	@Autowired
	Utils utils;
	

	@Override
	public UserVaultDto createVault(UserEntity user, UserVaultDto vault) {
		
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
	public List<UserVaultDto> getVaults(UserEntity user) {
		List<UserVaultDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		Iterable<UserVault> vaults = userVaultsRepository.findAllByUserDetails(user);
		
		for (UserVault vaultsEntity : vaults) {
			returnValue.add(modelMapper.map(vaultsEntity, UserVaultDto.class));
		}
		
		return returnValue;
	}

	@Override
	public UserVaultDto getVault(String vaultId) {
		UserVault vault = userVaultsRepository.findByVaultId(vaultId);
		
		if (vault == null) {
			 throw new EntityNotFoundException(UserVault.class, "vault_id", vaultId);
		}
		
		return mapVaultsEntityToVaultsDto(vault);
	}

	@Override
	public UserVaultDto updateVault(String vaultId, UserVaultDto vaultDetails) {
		UserVault vault = userVaultsRepository.findByVaultId(vaultId);
		
		if (vault == null) {
			 throw new EntityNotFoundException(UserVault.class, "vault_id", vaultId);
		}
		
		vault.setName(vaultDetails.getName());
		
		return saveAndReturnStoredVaultDetails(vault);
	}

	@Override
	public void deleteVault(String vaultId) {
		UserVault vault = userVaultsRepository.findByVaultId(vaultId);
		
		if (vault == null) {
			 throw new EntityNotFoundException(UserVault.class, "vault_id", vaultId);
		}
		
		userVaultsRepository.delete(vault);
	}

	private UserVaultDto saveAndReturnStoredVaultDetails(UserVault vault) {
		UserVault storedVaultDetails = userVaultsRepository.save(vault);
		return mapVaultsEntityToVaultsDto(storedVaultDetails);
	}
	
	private UserVaultDto mapVaultsEntityToVaultsDto(UserVault vault) {
		UserVaultDto returnValue = new UserVaultDto();
		BeanUtils.copyProperties(vault, returnValue);
		return returnValue;
	}

}
