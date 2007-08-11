package org.cspoker.client;

public interface CommandExecutor {
    
    public static String n=System.getProperty("line.separator");
    
    public String execute(String... args) throws Exception;
	
}
