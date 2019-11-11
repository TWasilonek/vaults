package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.exceptions.VaultsServiceException;
import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.entity.VaultsEntity;
import com.tomaszwasilonek.vaults.ws.io.repositories.VaultsRepository;
import com.tomaszwasilonek.vaults.ws.service.VaultsService;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.VaultsDto;
import com.tomaszwasilonek.vaults.ws.ui.model.response.ErrorMessages;

@Component
public class VaultsServiceImpl implements VaultsService {
	
	@Autowired
	VaultsRepository vaultsRepository;

	@Autowired
	Utils utils;
	

	@Override
	public VaultsDto createVault(UserEntity user, VaultsDto vault) {
		
		// TODO: check if vault name is already taken
//		if (user.getVaultByName(vault.getName()));
		
		
		VaultsEntity vaultsEntity = new VaultsEntity();
		BeanUtils.copyProperties(vault, vaultsEntity);
		
		String publicVaultId = utils.generateVaultId(30);
		vaultsEntity.setName(vault.getName());
		vaultsEntity.setVaultId(publicVaultId);
		vaultsEntity.setUserDetails(user);
		
		return saveAndReturnStoredVaultDetails(vaultsEntity);
	}
	
	@Override
	public List<VaultsDto> getVaults(UserEntity user) {
		List<VaultsDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		Iterable<VaultsEntity> vaults = vaultsRepository.findAllByUserDetails(user);
		
		for (VaultsEntity vaultsEntity : vaults) {
			returnValue.add(modelMapper.map(vaultsEntity, VaultsDto.class));
		}
		
		return returnValue;
	}

	@Override
	public VaultsDto getVault(String vaultId) {
		VaultsEntity vault = vaultsRepository.findByVaultId(vaultId);
		
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		return mapVaultsEntityToVaultsDto(vault);
	}

	@Override
	public VaultsDto updateVault(String vaultId, VaultsDto vaultDetails) {
		VaultsEntity vault = vaultsRepository.findByVaultId(vaultId);
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		vault.setName(vaultDetails.getName());
		
		return saveAndReturnStoredVaultDetails(vault);
	}

	@Override
	public void deleteVault(String vaultId) {
		VaultsEntity vault = vaultsRepository.findByVaultId(vaultId);
		if (vault == null) throw new VaultsServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		vaultsRepository.delete(vault);
	}

	private VaultsDto saveAndReturnStoredVaultDetails(VaultsEntity vault) {
		VaultsEntity storedVaultDetails = vaultsRepository.save(vault);
		return mapVaultsEntityToVaultsDto(storedVaultDetails);
	}
	
	private VaultsDto mapVaultsEntityToVaultsDto(VaultsEntity vault) {
		VaultsDto returnValue = new VaultsDto();
		BeanUtils.copyProperties(vault, returnValue);
		return returnValue;
	}

}
