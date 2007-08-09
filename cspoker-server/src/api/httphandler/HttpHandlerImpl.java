package api.httphandler;

import java.io.IOException;
import java.io.PrintStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class HttpHandlerImpl implements HttpHandler {

    public HttpHandlerImpl() {
	super();
    }

    protected void throwException(HttpExchange http, Exception e)
    throws IOException {
	System.out.println("Exception occured:");
	e.printStackTrace();
	http.sendResponseHeaders(500, 0);
	e.printStackTrace(new PrintStream(http.getResponseBody()));
	http.close();
    }

}