package com.SpaceWars.GameData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {

	public void updatePlayerDataInFile(String name, String shipType) {

		writeToFile(name, shipType);

	}

	public String getPlayerNameFromFile() {

		Map<String, String> data = getPlayerDataInFile();

		return data.get("Name").toString();

	}

	public String getPlayerShipTypeFromFile() {

		Map<String, String> data = getPlayerDataInFile();

		return data.get("LastShipSelected").toString();

	}

	private Map<String, String> getPlayerDataInFile() {

		File file = null;
		BufferedReader brBufferedReader = null;
		Map<String, String> dataMap = new HashMap<>();
		try {
			file = new File(System.getProperty("user.dir") + "/PlayerData/player_data.dat");
			brBufferedReader = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = brBufferedReader.readLine()) != null) {
				String[] data = str.split(":");
				String name = data[0];
				String value = data[1];
				dataMap.put(name, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				brBufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dataMap;

	}

	private void writeToFile(String name, String type) {

		FileWriter fw = null;
		try {
			fw = new FileWriter(System.getProperty("user.dir") + "/PlayerData/player_data.dat");
			fw.write("Name:" + name + "\n");
			fw.write("LastShipSelected:" + type);
			fw.close();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
