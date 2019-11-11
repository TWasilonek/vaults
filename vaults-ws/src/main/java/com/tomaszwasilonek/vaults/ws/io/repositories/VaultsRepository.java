package com.tomaszwasilonek.vaults.ws.io.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.io.entity.VaultsEntity;

@Repository
public interface VaultsRepository extends CrudRepository<VaultsEntity, Long> {
	List<VaultsEntity> findAllByUserDetails(UserEntity userEntity);
	VaultsEntity findByVaultId(String vaultId);
}
