package com.tomaszwasilonek.vaults.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;
import com.tomaszwasilonek.vaults.ws.exceptions.BalanceTooLowException;
import com.tomaszwasilonek.vaults.ws.exceptions.EntityNotFoundException;
import com.tomaszwasilonek.vaults.ws.exceptions.RecordAlreadyExistsException;
import com.tomaszwasilonek.vaults.ws.repositories.UserVaultRepository;
import com.tomaszwasilonek.vaults.ws.service.UserVaultService;
import com.tomaszwasilonek.vaults.ws.shared.Utils;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;
import com.tomaszwasilonek.vaults.ws.shared.dto.UserVaultDto;

@Component
public class UserVaultServiceImpl implements UserVaultService {
	
	@Autowired
	UserVaultRepository userVaultsRepository;

	@Autowired
	Utils utils;
	

	@Override
	public UserVaultDto createVault(UserEntity user, UserVaultDto vault) {
		
		if (userVaultsRepository.findByName(vault.getName()) != null) {
			throw new RecordAlreadyExistsException(UserVault.class, "name", vault.getName());
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
	
	@Override
	public void moneyTransfer(PaymentDTO moneyTransfer) {	
		UserVault sourceVault = userVaultsRepository.findByVaultId(moneyTransfer.getSourceAccount());
		
		if (sourceVault == null) {
			 throw new EntityNotFoundException(UserVault.class, "vault_id", moneyTransfer.getSourceAccount());
		}
		
		UserVault targetVault = userVaultsRepository.findByVaultId(moneyTransfer.getDestinationAccount());
		
		if (targetVault == null) {
			 throw new EntityNotFoundException(UserVault.class, "vault_id", moneyTransfer.getDestinationAccount());
		}
		
		if (sourceVault.getBalance() <= 0 || sourceVault.getBalance() < moneyTransfer.getAmount()) {
			throw new BalanceTooLowException(UserVault.class, "vault_id", moneyTransfer.getSourceAccount());
		}
		
		sourceVault.setBalance(sourceVault.getBalance() - moneyTransfer.getAmount());
		targetVault.setBalance(targetVault.getBalance() + moneyTransfer.getAmount());
		
		userVaultsRepository.save(sourceVault);
		userVaultsRepository.save(targetVault);
	}
	
	@Override
	public void deposit(PaymentDTO theDeposit) {	
		UserVault targetVault = userVaultsRepository.findByVaultId(theDeposit.getDestinationAccount());
		
		if (targetVault == null) {
			 throw new EntityNotFoundException(UserVault.class, "vault_id", theDeposit.getDestinationAccount());
		}

		targetVault.setBalance(targetVault.getBalance() + theDeposit.getAmount());
		
		userVaultsRepository.save(targetVault);
	}
	
	@Override
	public void withdraw(PaymentDTO theWithdrawal) {
		UserVault sourceVault = userVaultsRepository.findByVaultId(theWithdrawal.getSourceAccount());
		
		if (sourceVault == null) {
			throw new EntityNotFoundException(UserVault.class, "vault_id", theWithdrawal.getSourceAccount());
		}
		
		sourceVault.setBalance(sourceVault.getBalance() - theWithdrawal.getAmount());
		
		userVaultsRepository.save(sourceVault);
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
