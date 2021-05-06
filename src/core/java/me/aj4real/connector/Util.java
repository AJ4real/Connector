package me.aj4real.connector;

import java.io.*;
import java.nio.file.Files;

public class Util {

	public static String processCommand(String command) {
		ProcessBuilder processBuilder = new ProcessBuilder(command.replace('\'', '\"').split(" "));
		final int bufferSize = 8 * 1024;
		byte[] buffer = new byte[bufferSize];
		final StringBuilder builder = new StringBuilder();
		InputStream inputStream = null;
		try {
			Process process = processBuilder.start();
			inputStream = process.getInputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, bufferSize)) {
			while (bufferedInputStream.read(buffer) != -1) {
				builder.append(new String(buffer));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return builder.toString();
	}

	public static String readFileAsString(File file) {
		String data = "";
		try {
			data = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			me.aj4real.connector.Logger.handle(e);
		}
		return data;
	}

	public static void writeToFile(File file, String str) {
		try {
			file.createNewFile();
			FileWriter out = new FileWriter(file);
			out.write(str);
			out.flush();
			out.close();
		} catch (Exception err) {

		}
	}

}
