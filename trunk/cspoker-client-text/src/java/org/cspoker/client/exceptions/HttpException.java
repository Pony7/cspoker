package org.cspoker.client.exceptions;

public class HttpException extends RuntimeException implements StackTraceWrapper{
    
    private static final long serialVersionUID = 6555631439629401358L;

    public HttpException(String msg) {
	super(msg);
    }
    
    private String stackTraceString="";

    public String getStackTraceString() {
        return stackTraceString;
    }

    public void setStackTraceString(String stackTraceString) {
        this.stackTraceString = stackTraceString;
    }
    
}
