package org.cspoker.client;

public class HelpCommand implements CommandExecutor {

    @Override
    public String send(String... args) throws Exception {
	
	return "Supported commands:"+n +
			"-General:"+n +
			"  HELP"+n +
			"  PING"+n +
			"-Tables:"+n +
			"  CREATETABLE"+n +
			"  LISTTABLES"+n +
			"  JOINTABLE"+n +
			"";
    }

}
