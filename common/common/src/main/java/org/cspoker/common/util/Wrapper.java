package org.cspoker.common.util;

public interface Wrapper<T, E extends Throwable> {

	T getContent() throws E;
	
}
