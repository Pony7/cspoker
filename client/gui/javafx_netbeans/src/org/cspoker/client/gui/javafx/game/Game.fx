/*
 * Game.fx
 *
 * Created on 20-feb-2008, 22:15:48
 */

package org.cspoker.client.gui.javafx.game;
import javafx.ui.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.game.Player;

/**
 * @author Cedric
 */

class Game {
    attribute player:Player;
    attribute currentPlayer:Player;
    attribute screen:Frame;
    operation init();
}
operation Game.init(){
    
}

  attribute Game.screen=Frame{
        	centerOnScreen:true
            title: "Login Screen"
            width: 300
            height: 400
            visible: true
            menubar: MenuBar {
                 menus: Menu {
                     text: "Options"
                     mnemonic: F
                     items:MenuItem {
                     	sizeToFitRow: true
                         text: "Exit"
                         mnemonic: C
                         action: operation() {
                             System.exit(0);
                         }
                         }
                     }
            }
};