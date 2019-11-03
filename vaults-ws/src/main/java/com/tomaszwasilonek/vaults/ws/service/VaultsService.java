package com.tomaszwasilonek.vaults.ws.service;

import com.tomaszwasilonek.vaults.ws.io.entity.UserEntity;
import com.tomaszwasilonek.vaults.ws.shared.dto.VaultsDto;

public interface VaultsService {
	VaultsDto createVault(UserEntity user, VaultsDto vault);
}
