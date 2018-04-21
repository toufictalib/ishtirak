package com.aizong.ishtirak.common.misc.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

public class SQLUtils {

    public static String sql(String file, Object... params) {
	file = "sql/" + file;
	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	try (Stream<String> stream = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(file))).lines();) {
	    StringBuilder builder = new StringBuilder();
	    stream.forEach(e -> builder.append(e + "\n"));
	    String s = builder.toString();

	    if (params.length > 0) {
		s = MessageFormat.format(s, params);
	    }
	    return s;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return file;
    }
    
    public static String toParameterList(List<?> uniqueContractIds) {
	StringBuilder builder = new StringBuilder();
	for(Object s:uniqueContractIds) {
	builder.append("\""+s.toString()+"\",");
	
	}
	String parameterList = builder.substring(0, builder.length()-",".length());
	return parameterList;
    }
}
