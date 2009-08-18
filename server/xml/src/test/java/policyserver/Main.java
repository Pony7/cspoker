/*
 * Main.java
 *
 * This file is part of a tutorial on making a chat application using Flash
 * for the clients and Java for the multi-client server.
 * 
 * View the tutorial at http://www.broculos.net/
 */

package policyserver;

/**
 * Main is used to start the servers and handle the debug messages.
 *
 * @author Nuno Freitas (nunofreitas@gmail.com)
 */
public class Main {
    public static final boolean DEBUG = true;
    public static PolicyServer policyServer;
    
    /**
     *  If debug is enabled writes the message through the GUI.
     *
     * @param label the label of the message to write
     * @param msg the message to write
     */
    public static void debug(String label, String msg) {
        if (DEBUG) {
        	System.out.println(msg);
        }
    }
    
    /**
     * Starts the chat server, the policy server and the GUI for debug messages.
     *
     * @param args the command line arguments (first is the chat server port and second is the policy server port)
     */
    public static void main(String[] args) {
        try {

            int policyPort = 8081;
            
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    policyPort = Integer.parseInt(args[i]);
                }
                
                
            }
            PolicyServer policyServer = new PolicyServer(policyPort);
            policyServer.start();

            Main.policyServer = policyServer;
        }
        catch (Exception e) {
            debug("Main", "Exception (main)" + e.getMessage());
        }
    }
}
