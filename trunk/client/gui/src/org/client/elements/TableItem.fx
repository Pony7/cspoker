/*
 * Table.fx
 *
 * Created on 22-feb-2008, 0:09:29
 */

package org.client.elements;

import org.client.elements.TableInterface;

/**
 * @author Cedric
 */

class TableItem extends TableInterface{
    attribute Id:Integer;
    attribute nb:Integer;
    attribute bB:Integer;
    attribute sB:Integer;
    operation setId(id:Integer);
    operation setNbPlayers(n:Integer);
    operation setSmallBlind(n:Integer);
    operation setBigBlind(n:Integer);
}
operation TableItem.setId(id:Integer){
    Id=id;
}
operation TableItem.setNbPlayers(n:Integer){
    nb=n;
}
operation TableItem.setSmallBlind(n:Integer){
    sB=n;
}
operation TableItem.setBigBlind(n:Integer){
    bB=n;
}