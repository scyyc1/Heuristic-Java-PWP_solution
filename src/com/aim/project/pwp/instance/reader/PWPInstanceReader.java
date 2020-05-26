package com.aim.project.pwp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.PWPInstance;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPInstanceReaderInterface;


public class PWPInstanceReader implements PWPInstanceReaderInterface {

	@Override
	public PWPInstanceInterface readPWPInstance(Path path, Random random) {

		BufferedReader bfr;
		try {
			bfr = Files.newBufferedReader(path);
			
			// TODO read in the PWP instance and create and return a new 'PWPInstance'
			int numberOfLocations=0;
			Location[] aoLocations = new Location[0];
			Location oPostalDepotLocation = null;
			Location oHomeAddressLocation = null;

			String line;
			while((line=bfr.readLine())!=null) {
				if(line.equals("POSTAL_OFFICE")) {
					
					line=bfr.readLine();
					String temp[]=line.split("\\s+");
					oPostalDepotLocation=new Location(Double.parseDouble(temp[0]),
							Double.parseDouble(temp[1]));
					
				}else if(line.equals("WORKER_ADDRESS")){
					
					line=bfr.readLine();
					String temp[]=line.split("\\s+");
					oHomeAddressLocation=new Location(Double.parseDouble(temp[0]),
							Double.parseDouble(temp[1]));
					
				}else if(line.equals("POSTAL_ADDRESSES")) {
					
					while(!(line=bfr.readLine()).contentEquals("EOF")) {
						String temp[]=line.split("\\s+");
						
						Location[] addr=new Location[numberOfLocations+1];
						addr[numberOfLocations]=new Location(Double.parseDouble(temp[0]),
								Double.parseDouble(temp[1]));
						System.arraycopy(aoLocations, 0, addr, 0, aoLocations.length);
						
						aoLocations=addr;
						numberOfLocations++;
					}
				}
			}
			return new PWPInstance(numberOfLocations, 
					aoLocations, oPostalDepotLocation, 
					oHomeAddressLocation, random);
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}
}
