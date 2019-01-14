package com.AI.NeuralNetwork;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NeuralNetworkUtils {

	private String filePath = "SavedData/";
	
	public void writeNetworkToFile(NeuralNetwork network, String fileName) {
		
		try {
			FileOutputStream out = new FileOutputStream(filePath+fileName);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			System.out.println("Writing network to file:Started");
			oos.writeObject(network);
			System.out.println("Writing network to file:Finished");
			oos.close();
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not find file:"+filePath+fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public NeuralNetwork readNetworkFromFile(String fileName) {
		NeuralNetwork nn = null;
		FileInputStream is;
		ObjectInputStream ois;
		try {
			is = new FileInputStream(filePath+fileName);
			ois = new ObjectInputStream(is);
			Object obj = ois.readObject();
			if(obj instanceof NeuralNetwork) {
				nn = (NeuralNetwork) obj;
			} else {
				System.out.println("Read object is not an insatance of NeuralNetwork class\nExiting...");
			}
			ois.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nn;
	}
	
}
