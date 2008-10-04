package org.cspoker.common.util.lazy;

public interface ISimpleWrapper<T> extends IWrapper<T,Throwable>{

	public T getContent();
	
}
