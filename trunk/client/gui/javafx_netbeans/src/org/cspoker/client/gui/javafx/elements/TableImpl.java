/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cspoker.client.gui.javafx.elements;

/**
 *
 * @author Cedric
 */
public class TableImpl implements TableInterface{

    public TableImpl(int a,int b,int c,int d){
        Id=a;
        nbPlayers=b;
        smallBlind=c;
        bigBlind=d;
    }
    public int Id;
    public int nbPlayers;
    public int smallBlind;
    public int bigBlind;

    public int getId() {
        return Id;
    }

    public int getNbPlayers() {
       return nbPlayers;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }
}
