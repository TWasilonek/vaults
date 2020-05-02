package com.tomaszwasilonek.vaults.ws.exceptions;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class BalanceTooLowException extends CustomRuntimeException {
	
	private static final long serialVersionUID = 2335432442393897392L;
	
	public BalanceTooLowException(Class clazz, String... searchParamsMap) {
		super(BalanceTooLowException.generateMessage(clazz.getSimpleName(),
				CustomRuntimeException.searchParamsToMap(String.class, String.class, searchParamsMap)));
	}

	private static String generateMessage(String entity, Map<String, String> searchParams) {
		return "Balance too low on vault " + searchParams;
	}
}
