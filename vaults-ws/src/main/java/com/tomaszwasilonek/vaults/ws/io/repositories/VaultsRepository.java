package com.tomaszwasilonek.vaults.ws.io.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.entity.UserVaultsEntity;

@Repository
public interface VaultsRepository extends CrudRepository<UserVaultsEntity, Long> {
	List<UserVaultsEntity> findAllByUserDetails(UserEntity userEntity);
	UserVaultsEntity findByVaultId(String vaultId);
}
