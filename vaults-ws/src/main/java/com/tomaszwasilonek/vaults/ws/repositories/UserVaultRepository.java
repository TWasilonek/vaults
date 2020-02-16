package com.tomaszwasilonek.vaults.ws.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.entity.UserVault;

public interface UserVaultRepository extends CrudRepository<UserVault, Long> {
	List<UserVault> findAllByUserDetails(UserEntity userEntity);
	UserVault findByVaultId(String vaultId);
	UserVault findByName(String name);
}
