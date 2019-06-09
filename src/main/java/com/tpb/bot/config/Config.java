package com.tpb.bot.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@SuppressWarnings("unused")
public class Config {

	private static JSONArray jsonArrayConfig = null;
	private static Properties properties = null;

	public static void main(String[] args) {

//		loadConfig();
//
//		JSONObject flow = getFlowById("INDIVIDUAL"); // INDIVIDUAL |
//														// ORGANISATION
//
//		System.out.println("flow: " + flow);
//		System.out.println("flowName: " + getValue(flow, "flowName"));
//
//		JSONObject step = getStep(flow, "2");
//		System.out.println("step: " + step);
//		System.out.println("stepName: " + getValue(step, "stepName"));
	}
	
	static {
//		loadProperties();
		loadConfig();
	}

	public static void loadConfig() {
		JSONParser parser = new JSONParser();

		try {

			InputStream is = Config.class.getClassLoader().getResourceAsStream(
					"flow.json");
			Object obj = parser.parse(new InputStreamReader(is));

			jsonArrayConfig = (JSONArray) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void loadProperties() {
		try {

			InputStream is = Config.class.getClassLoader().getResourceAsStream(
					"config.properties");
//			properties.load(is);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static JSONArray getJsonConfig(){
		return jsonArrayConfig;
	}
	
	public static String getParam(String param){
		if(properties == null)
			return null;
		
		return properties.getProperty(param);
		
	}

}
