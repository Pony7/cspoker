package org.cspoker.common.util.lazy;

public interface IWrapper2<T, E extends Throwable, F extends Throwable> {

	T getContent() throws E,F;
	
}
