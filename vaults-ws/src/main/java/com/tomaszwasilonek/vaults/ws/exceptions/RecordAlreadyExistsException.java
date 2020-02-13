package com.tomaszwasilonek.vaults.ws.exceptions;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class RecordAlreadyExistsException extends CustomRuntimeException {

	private static final long serialVersionUID = -3517165860724422142L;

	public RecordAlreadyExistsException(String message) {
		super(message);
	}

	public RecordAlreadyExistsException(Class clazz, String... searchParamsMap) {
		super(RecordAlreadyExistsException.generateMessage(clazz.getSimpleName(),
				CustomRuntimeException.searchParamsToMap(String.class, String.class, searchParamsMap)));
	}

	private static String generateMessage(String entity, Map<String, String> searchParams) {
		return "Record '" +  StringUtils.capitalize(entity) + "' where " + searchParams + " already exists.";
	}

}
