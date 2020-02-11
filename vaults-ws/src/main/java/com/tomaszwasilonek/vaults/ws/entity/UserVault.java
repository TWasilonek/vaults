package com.tomaszwasilonek.vaults.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserVault implements Serializable {

	private static final long serialVersionUID = 2278145604595676191L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable=false, length=30)
	private String name;
	
	@Column(nullable=false)
	private String vaultId;
	
	@Column(nullable=false)
	private double balance;
	
	@ManyToOne
	@JoinColumn(name="users_id")
	private UserEntity userDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVaultId() {
		return vaultId;
	}

	public void setVaultId(String vaultId) {
		this.vaultId = vaultId;
	}

	public UserEntity getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserEntity userDetails) {
		this.userDetails = userDetails;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	

}
