package com.tomaszwasilonek.vaults.ws.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;

import com.tomaszwasilonek.vaults.ws.shared.constants.PaymentType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Payment extends AuditModel implements Serializable {/**
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
	
	@NotBlank(message = "source account is required")
	@Column(nullable=false)
	private String sourceAccount;
	
	@NotBlank(message = "source subject is required")
	@Column(nullable=false)
	private String sourceSubject;
	
	@NotBlank(message = "destination account is required")
	@Column(nullable=false)
	private String destinationSubject;
	
	@NotBlank(message = "destination subject is required")
	@Column(nullable=false)
	private String destinationAccount;
	
	private String description;
	
	@NotBlank(message = "paymentType must be set")
	@Column(nullable=false)
	private PaymentType paymentType;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;
	
}
