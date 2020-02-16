package com.tomaszwasilonek.vaults.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class UserVault extends AuditModel implements Serializable {

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
	@JoinColumn(name="user_id")
	private UserEntity userDetails;

}
