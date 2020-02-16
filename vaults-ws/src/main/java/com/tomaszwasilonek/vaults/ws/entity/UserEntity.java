package com.tomaszwasilonek.vaults.ws.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name="user")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserEntity extends AuditModel implements Serializable {

	private static final long serialVersionUID = 956484066067704831L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false)
	private String userId;
	
	@Column(nullable=false, length=50)
	private String firstName;
	
	@Column(nullable=false, length=50)
	private String lastName;
	
	@Column(nullable=false, length=120)
	private String email;
	
	@Column(nullable=false)
	private String encryptedPassword;
	
	private String emailVerificationToken;
	
	@Column(nullable=false)
	private Boolean emailVerificationStatus = false;

	// TODO Explore if bidirectional one-to-many mapping is needed, check the ending of this article - https://www.callicoder.com/hibernate-spring-boot-jpa-one-to-many-mapping-example/
	@OneToMany(mappedBy="userDetails", cascade=CascadeType.ALL)
	private List<UserVault> vaults;
	
}
