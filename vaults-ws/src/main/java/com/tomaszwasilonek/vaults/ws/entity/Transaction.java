package com.tomaszwasilonek.vaults.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Transaction extends AuditModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5521698599323885396L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false)
	private String transactionId;
	
	@NotBlank(message = "amount is required")
	@Column(nullable=false)
	private double amount;
	
	@NotBlank(message = "sourceVaultId is required")
	@Column(nullable=false)
	private String sourceVaultId;
	
	@NotBlank(message = "targetVaultId is required")
	@Column(nullable=false)
	private String targetVaultId;
}
