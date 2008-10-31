/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.common.util;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.common.jaxbcontext.ActionJAXBContext;
import org.cspoker.common.jaxbcontext.EventJAXBContext;
import org.cspoker.common.jaxbcontext.HTTPJAXBContext;
import org.cspoker.common.jaxbcontext.SocketJAXBContext;

/**
 * The Maven plugin for schemagen is infested with bugs. This is a useable alternative.
 * 
 * @author guy
 */
public class SchemaGen {

	public static void main(String[] args) throws IOException {
		final File baseDir = new File("schema");
		baseDir.mkdir();

		class MySchemaOutputResolver extends SchemaOutputResolver {
			
			private String file;
			
			public MySchemaOutputResolver(String file) {
				this.file = file;
			}

			public Result createOutput( String namespaceUri, String suggestedFileName ) throws IOException {
				//actions
				if(namespaceUri.equals("http://www.cspoker.org/api/2008-11/")){
					return new StreamResult(new File(baseDir,file+".xsd"));
				}
				return new StreamResult(new File(baseDir,suggestedFileName));
			}
		}

		EventJAXBContext.context.generateSchema(new MySchemaOutputResolver("event"));
		ActionJAXBContext.context.generateSchema(new MySchemaOutputResolver("action"));
		SocketJAXBContext.context.generateSchema(new MySchemaOutputResolver("socket"));
		HTTPJAXBContext.context.generateSchema(new MySchemaOutputResolver("http"));
		System.out.println("done");
	}

}
