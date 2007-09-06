package org.cspoker.server.sockets;

public class ClientContext {

    private StringBuilder buffer;
    
    private String useragent="unknown";
    private String username="John Doe";
    private String password="";
    
    public ClientContext() {
	buffer = new StringBuilder();
    }
    
    public StringBuilder getBuffer(){
	return buffer;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
