package com.tomaszwasilonek.vaults.ws.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class CustomRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 3659718844293332950L;
	
	 public CustomRuntimeException(String message)
    {
        super(message);
    }

    public CustomRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public CustomRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CustomRuntimeException(String message, Throwable cause, 
                                       boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

	public static <K, V> Map<K, V> searchParamsToMap(
            Class<K> keyType, Class<V> valueType, Object... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                        Map::putAll);
    }
	
}
