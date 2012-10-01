package com.agourlay.pomf.tools;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

	public class CustomDateTimeSerializer extends JsonSerializer<DateTime> {
	    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

	    @Override
	    public void serialize(DateTime value, JsonGenerator gen, 
	        SerializerProvider arg2) throws IOException,
	        JsonProcessingException {
	        gen.writeString(formatter.print(value));
	    }
	    @Override
	    public Class<DateTime> handledType() { 
	        return DateTime.class; 
	    }
	}