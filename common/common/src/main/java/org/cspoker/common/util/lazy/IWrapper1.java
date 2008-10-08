package org.cspoker.common.util.lazy;

public interface IWrapper1<T, E extends Throwable> extends IWrapper2<T, E, Throwable>{

	T getContent() throws E;
	
}
