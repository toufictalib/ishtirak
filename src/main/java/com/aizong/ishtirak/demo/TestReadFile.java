package com.aizong.ishtirak.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestReadFile {

    public static void main(String args[]) {

	String fileName = "/home/ubility/MB.csv";

	// read file into stream, try-with-resources
	try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

	    StringBuilder builder = new StringBuilder();
	    stream.forEach(e -> {
		String ss[] = e.split(",");
		String s = "insert into counter_history(insert_date,consumption,contract_unique_code) "
			+ "values('2018-03-08 00:00:00'," + ss[1].trim() + ",\"" + ss[0].trim() + "\");";
		builder.append(s+"\n");
		
	    });

	    System.out.println(builder.toString());
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

}
