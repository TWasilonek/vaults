package com.tomaszwasilonek.vaults.ws.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
}
