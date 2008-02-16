package org.cspoker.common.xml;


/**
 * Interface for collecting XML messages encoded as a String.
 */
public interface XmlEventListener {

    /**
     * This method should queue up the XML for handling rather than
     * block during handling.
     *  
     * @param xml
     * 	      The XML message to collect.
     */
    public void collect(String xmlEvent);
    
}
