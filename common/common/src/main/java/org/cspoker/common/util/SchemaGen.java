package org.cspoker.common.util;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.common.jaxbcontext.ActionJAXBContext;

/**
 * The Maven plugin for schemagen is infested with bugs. This is a useable alternative.
 * 
 * @author guy
 */
public class SchemaGen {

	public static void main(String[] args) throws IOException {
		final File baseDir = new File("schema");
		baseDir.mkdir();
		final File actionDir = new File(baseDir, "actions");
		actionDir.mkdir();
		final File elementsDir = new File(baseDir, "elements");
		elementsDir.mkdir();

		class MySchemaOutputResolver extends SchemaOutputResolver {
			public Result createOutput( String namespaceUri, String suggestedFileName ) throws IOException {
				//actions
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/action")){
					return new StreamResult(new File(actionDir,"actions.xsd"));
				}
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/lobby/action")){
					return new StreamResult(new File(actionDir,"lobby.xsd"));
				}
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/holdemtable/action")){
					return new StreamResult(new File(actionDir,"holdemtable.xsd"));
				}
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/holdemtable/holdemplayer/action")){
					return new StreamResult(new File(actionDir,"holdemplayer.xsd"));
				}
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/chat/action")){
					return new StreamResult(new File(actionDir,"chat.xsd"));
				}
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/cashier/action")){
					return new StreamResult(new File(actionDir,"cashier.xsd"));
				}
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/account/action")){
					return new StreamResult(new File(actionDir,"account.xsd"));
				}
				//elements
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-9/elements/table")){
					return new StreamResult(new File(elementsDir,"table.xsd"));
				}
				return new StreamResult(new File(baseDir,suggestedFileName));
			}
		}

		ActionJAXBContext.context.generateSchema(new MySchemaOutputResolver());
	}

}
