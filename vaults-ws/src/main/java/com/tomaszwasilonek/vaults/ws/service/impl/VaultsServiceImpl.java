package com.tomaszwasilonek.vaults.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.entity.VaultsEntity;
import com.tomaszwasilonek.vaults.ws.io.repositories.VaultsRepository;
import com.tomaszwasilonek.vaults.ws.service.UserService;
import com.tomaszwasilonek.vaults.ws.service.VaultsService;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.VaultsDto;

@Component
public class VaultsServiceImpl implements VaultsService {
	
	@Autowired
	VaultsRepository vaultsRepository;
	
	@Autowired
	UserService userService;
	
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
