import javafx.ui.*;
import java.lang.*;

class Game{
        	attribute tableID: 	Integer;
        	attribute pot: Integer;
        	attribute players: Player*;
        	function getNumberPlayers(): Integer;
        	operation addPlayer(newPlayer: Player);
        	operation removePlayer(player: Player);
        }
        var currentPlayer=Player{};
        var currentGame=Game{
        	tableID: 0
        	pot: 0
        	
        };
        var gameFrame=Frame{
        	width: 800
        	height: 700
        	centerOnScreen: true
        	visible: false
        	title: bind "Player {currentPlayer.name} at table {currentGame.tableID}"
        };