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
package org.cspoker.client.bots.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PlotAvgProfit {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Scanner s = new Scanner(System.in);
		FileWriter f = new FileWriter("output/avgprofit.csv");
		int lastDeal = 0;
		while(s.hasNextLine()){
			String line = s.nextLine();
			//System.out.println("next line is "+line);
			if(line.contains("deal #")){
				int start = line.indexOf("deal #")+6;
				lastDeal = Integer.parseInt(line.substring(start, line.indexOf(" ", start)));
				System.out.println("Deal "+lastDeal);
				f.write("\n"+lastDeal);
				f.flush();
			}else if(line.contains("wins ")){
					int start = line.indexOf("wins ")+5;
					double avgProfit = Double.parseDouble(line.substring(start, line.indexOf(" ", start)));
					System.out.println("Won "+avgProfit+" after "+lastDeal+" deals");
					f.write(","+avgProfit);
					f.flush();
			}else{
				
			}
		}
	}

}
