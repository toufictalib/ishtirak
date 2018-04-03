package com.aizong.ishtirak.common.misc.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SQLUtils {

    public static String sql(String file) {

	file ="sql/"+file;
	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	URL resource = classloader.getResource(file);
	try (Stream<String> stream = Files.lines(Paths.get(resource.toURI()))) {
		StringBuilder builder = new StringBuilder();
	    stream.forEach(e->builder.append(e+"\n"));
	    return builder.toString();
	} catch (IOException | URISyntaxException e) {
		e.printStackTrace();
	}
	return file;
    }
}
