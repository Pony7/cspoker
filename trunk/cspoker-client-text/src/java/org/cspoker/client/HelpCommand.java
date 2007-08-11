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
			"  	      - just press enter to get all game events quickly"+n+
			"  GAMEEVENTS [ackID] - get recent game events and acknowledge all previous ones"+n +
			"  DEAL - deal"+n +
			"  CHECK - check"+n +
			"  BET [amount] - bet a certain amount"+n+
			"  CALL - call"+n +
			"  FOLD - fold"+n +
			"  RAISE [amount] - raise with a certain amount"+n+
			"  ALLIN - go all in"+n+
			"";
    }

}
