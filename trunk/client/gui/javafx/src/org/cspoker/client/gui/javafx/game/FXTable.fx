/*
 * FXTable.fx
 *
 * Created on 9-mrt-2008, 16:06:58
 */

package org.cspoker.client.gui.javafx.game;

import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.GameProperty;
import java.lang.*;

/**
 * @author guy
 */

class FXTable {
    attribute name:String;
    attribute id:TableId;
    attribute nbPlayers:Integer;
    attribute smallBlind:Integer;
    attribute bigBlind:Integer;
    
    function toFXTable(tables:Table*):FXTable*;
}

function FXTable.toFXTable(tables:Table*){
    return foreach(t in tables)
        FXTable{
            bigBlind: t.getGameProperty().getBigBlind()
            smallBlind: t.getGameProperty().getSmallBlind()
            id: t.getId()
            name: t.getName()
            nbPlayers: t.getNbPlayers()
        };
    
}