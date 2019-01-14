package com.UniverseSim.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

	private static PropertiesLoader obj = new PropertiesLoader();
	
	private static Properties univSimProperties;
	
	private static final String PROP_PATH="./properties";	
	private static final String PROP_FILE=PROP_PATH+"/UnivSim.properties";
	
	private PropertiesLoader() {
		readPropertiesFile();
	}
	
	private static void readPropertiesFile() {
		FileReader reader;
		try {
			reader = new FileReader(PROP_FILE);
			univSimProperties = new Properties();  
			univSimProperties.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return univSimProperties.getProperty(key);
	}
	
}
