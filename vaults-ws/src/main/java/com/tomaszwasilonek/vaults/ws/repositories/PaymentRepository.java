package com.tomaszwasilonek.vaults.ws.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tomaszwasilonek.vaults.ws.entity.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
	
}
