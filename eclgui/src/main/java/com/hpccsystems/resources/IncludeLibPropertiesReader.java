package com.hpccsystems.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class IncludeLibPropertiesReader {
	
	static {
		getApplicationProperties();
	}

	/* Property file read in this properties object */
	private static Properties applicationProperties;
	private static String propertiesFileLocation;
	private static String fileName = "hpccsystems";
	
	
	
	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		IncludeLibPropertiesReader.fileName = fileName;
	}

	/**
	 * Gets the applicationProperties
	 * @return Returns a Properties
	 */
	public static Properties getApplicationProperties() {
		applicationProperties = new Properties();
		try {
			String currentDir = System.getProperty("user.dir");
		    System.out.println("IncludeLibProp: Current dir using System:" +currentDir);
			propertiesFileLocation = currentDir + "\\plugins\\hpcc-common\\properties\\libraryInclude.properties";
			System.out.println("IncludeLibProp: Using file:" +propertiesFileLocation);
			//applicationProperties.load(PropertiesReader.class.getClassLoader().getResourceAsStream(propertiesFileLocation));
			applicationProperties.load(new FileInputStream(propertiesFileLocation));
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
		
		return applicationProperties;
	}

	public static String getProperty(String propertyName) {
		return applicationProperties.getProperty(propertyName) != null ? applicationProperties.getProperty(propertyName) : "";
	}
	
	/**
	 * Writes/Updates the property
	 * @param propertyKey
	 * @param propertyValue
	 */
	public static void setProperty(String propertyKey, String propertyValue) {
		applicationProperties.setProperty(propertyKey, propertyValue);
		try {
			applicationProperties.store(new FileOutputStream(propertiesFileLocation), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getAllKeys() {
		Enumeration<Object> enumer = applicationProperties.keys();
		while (enumer.hasMoreElements())
			System.out.println(enumer.nextElement()); 
	}
	
	public static void getAllValues() {
		Enumeration<Object> enumer = applicationProperties.elements();
		while (enumer.hasMoreElements())
			System.out.println(enumer.nextElement()); 
	}
	
	public static Map<String, String> getAllProperties() {
		Map<String, String> mapAllProperties = null;
		Enumeration<Object> enumer = applicationProperties.keys();
		if(enumer != null) {
			mapAllProperties = new HashMap<String, String>();
			while (enumer.hasMoreElements()) {
				String key = (String)enumer.nextElement();
				String value = getProperty(key);
				mapAllProperties.put(key, value);
			}
		}
		
		return mapAllProperties;
	}
}
