package com.tomaszwasilonek.vaults.ws.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tomaszwasilonek.vaults.ws.entity.UserEntity;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
}
