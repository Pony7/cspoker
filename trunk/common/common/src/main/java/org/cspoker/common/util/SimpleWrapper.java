package org.cspoker.common.util;

public interface SimpleWrapper<T> extends Wrapper<T,Throwable>{

	public T getContent();
	
}
