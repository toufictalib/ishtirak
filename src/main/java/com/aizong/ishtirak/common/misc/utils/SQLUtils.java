package com.aizong.ishtirak.common.misc.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

public class SQLUtils {

    public static String sql(String file, Object... params) {
	file = "sql/" + file;
	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	URL resource = classloader.getResource(file);
	try (Stream<String> stream = Files.lines(Paths.get(resource.toURI()))) {
	    StringBuilder builder = new StringBuilder();
	    stream.forEach(e -> builder.append(e + "\n"));
	    String s = builder.toString();

	    if (params.length > 0) {
		s = MessageFormat.format(s, params);
	    }
	    return s;
	} catch (IOException | URISyntaxException e) {
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
