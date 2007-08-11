package org.cspoker.client;


public class HelpCommand implements CommandExecutor {

    @Override
    public String execute(String... args) throws Exception {
	
	return "Supported commands:"+n +
			"-General:"+n +
			"  HELP - what you're looking at now"+n +
			"  PING - ping the server"+n +
			"  QUIT - close the client"+n +
			"  EXIT - close the client"+n +
			"-Tables:"+n +
			"  CREATETABLE - create your table"+n +
			"  LISTTABLES - list all available tables"+n +
			"  JOINTABLE [id] - join the table with id [id]"+n +
			"-Game:"+n +
			"  STARTGAME - start a new game at your table"+n +
			"  GAMEEVENTS - get recent game events"+n +
			"";
    }

}
