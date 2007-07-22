package org.cspoker.client.xmlrpc.exceptions;

public class TableFullException extends Exception {

    private static final long serialVersionUID = 2536253103149632849L;

    public TableFullException(String tableName) {
	super(tableName);
    }
    
}
