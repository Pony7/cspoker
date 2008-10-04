package org.cspoker.common.util.lazy;

public interface IWrapper<T, E extends Throwable> {

	T getContent() throws E;
	
}
