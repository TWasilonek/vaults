package com.tomaszwasilonek.vaults.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tomaszwasilonek.vaults.ws.io.entity.VaultsEntity;

@Repository
public interface VaultsRepository extends CrudRepository<VaultsEntity, Long> {

}
