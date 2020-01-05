package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.exceptions.VaultsServiceException;
import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.entity.UserVaultsEntity;
import com.tomaszwasilonek.vaults.ws.io.repositories.VaultsRepository;
import com.tomaszwasilonek.vaults.ws.service.VaultsService;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultsDto;
import com.tomaszwasilonek.vaults.ws.ui.model.response.ErrorMessages;

@Component
public class VaultsServiceImpl implements VaultsService {
	
	@Autowired
	VaultsRepository vaultsRepository;

	@Autowired
	Utils utils;
	

	@Override
	public UserVaultsDto createVault(UserEntity user, UserVaultsDto vault) {
		
		// TODO: check if vault name is already taken
//		if (user.getVaultByName(vault.getName()));
		
		
		UserVaultsEntity vaultsEntity = new UserVaultsEntity();
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
		
		Iterable<UserVaultsEntity> vaults = vaultsRepository.findAllByUserDetails(user);
		
		for (UserVaultsEntity vaultsEntity : vaults) {
			returnValue.add(modelMapper.map(vaultsEntity, UserVaultsDto.class));
		}
		
		return returnValue;
	}

	@Override
	public UserVaultsDto getVault(String vaultId) {
		UserVaultsEntity vault = vaultsRepository.findByVaultId(vaultId);
		
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		return mapVaultsEntityToVaultsDto(vault);
	}

	@Override
	public UserVaultsDto updateVault(String vaultId, UserVaultsDto vaultDetails) {
		UserVaultsEntity vault = vaultsRepository.findByVaultId(vaultId);
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		vault.setName(vaultDetails.getName());
		
		return saveAndReturnStoredVaultDetails(vault);
	}

	@Override
	public void deleteVault(String vaultId) {
		UserVaultsEntity vault = vaultsRepository.findByVaultId(vaultId);
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		vaultsRepository.delete(vault);
	}

	private UserVaultsDto saveAndReturnStoredVaultDetails(UserVaultsEntity vault) {
		UserVaultsEntity storedVaultDetails = vaultsRepository.save(vault);
		return mapVaultsEntityToVaultsDto(storedVaultDetails);
	}
	
	private UserVaultsDto mapVaultsEntityToVaultsDto(UserVaultsEntity vault) {
		UserVaultsDto returnValue = new UserVaultsDto();
		BeanUtils.copyProperties(vault, returnValue);
		return returnValue;
	}

}
