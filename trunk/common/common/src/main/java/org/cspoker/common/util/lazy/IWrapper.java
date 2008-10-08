package org.cspoker.common.util.lazy;

public interface IWrapper<T> extends IWrapper1<T,Throwable>{

	public T getContent();
	
}
