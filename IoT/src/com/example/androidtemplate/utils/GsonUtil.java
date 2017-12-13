package com.example.androidtemplate.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GsonUtil {
	private static Gson gson = new GsonBuilder()
	.setDateFormat("yyyy-MM-dd HH:mm:ss")
	.registerTypeAdapter(int.class, new IntTypeAdapter())
	.registerTypeAdapter(Integer.class, new IntTypeAdapter())
	.registerTypeAdapter(long.class, new LongTypeAdapter())
	.registerTypeAdapter(Long.class, new LongTypeAdapter())
	.create();
	
	
	private GsonUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static Gson getInstance() {
		return gson;
	}
	
	static class IntTypeAdapter extends TypeAdapter<Number> {
		
		@Override
		public void write(JsonWriter out, Number value)
				throws IOException {
			out.value(value);
		}
		
		@Override
		public Number read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			try {
				String result = in.nextString();
				if ("".equals(result)) {
					return null;
				}
				return Integer.parseInt(result);
			} catch (NumberFormatException e) {
				throw new JsonSyntaxException(e);
			}
		}
	}
	
	static class LongTypeAdapter extends TypeAdapter<Number> {
		
		@Override
		public void write(JsonWriter out, Number value)
				throws IOException {
			out.value(value);
		}
		
		@Override
		public Number read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			try {
				String result = in.nextString();
				if ("".equals(result)) {
					return null;
				}
				return Long.parseLong(result);
			} catch (NumberFormatException e) {
				throw new JsonSyntaxException(e);
			}
		}
	}
}
