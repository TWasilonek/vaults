package com.tomaszwasilonek.vaults.ws.exceptions;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class EntityNotFoundException extends CustomRuntimeException {

	private static final long serialVersionUID = 2335432448856069750L;

	public EntityNotFoundException(Class clazz, String... searchParamsMap) {
		super(EntityNotFoundException.generateMessage(clazz.getSimpleName(),
				CustomRuntimeException.searchParamsToMap(String.class, String.class, searchParamsMap)));
	}

	private static String generateMessage(String entity, Map<String, String> searchParams) {
		return StringUtils.capitalize(entity) + " was not found for parameters " + searchParams;
	}

}
