package org.cspoker.server.api.httphandler;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CrossDomain implements HttpHandler {

    @Override
    public void handle(HttpExchange http) throws IOException {
	if(http.getRequestURI().getPath()!=null && http.getRequestURI().getPath().endsWith("crossdomain.xml")){
	    byte[] result="<?xml version=\"1.0\"?><!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\"><cross-domain-policy><allow-access-from domain=\"*\" /></cross-domain-policy>".getBytes();
	    http.sendResponseHeaders(200, result.length);
	    http.getResponseBody().write(result);
	    http.getResponseBody().flush();
	    http.getResponseBody().close();
	}else{
	    byte[] result="Page not found.".getBytes();
	    http.sendResponseHeaders(404, result.length);
	    http.getResponseBody().write(result);

	    http.getResponseBody().flush();
	    http.getResponseBody().close();
	}
    }

}
