package com.pep.ProxyEntryPoint.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class Base64Utils {

    private Base64Utils() {
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                    LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonSerializationContext) ->
                    jsonSerializationContext.serialize(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                    LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (dateTime, type, jsonSerializationContext) ->
                    jsonSerializationContext.serialize(dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .create();

    public static String encodeToBase64(Object object) {
        String jsonOutput = gson.toJson(object);
        return encodeToBase64(jsonOutput);
    }

    public static String encodeToBase64(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeFromBase64(String base64) {
        return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    }

    public static <T> T decodeFromBase64(String base64, Class<T> clazz) {
        String jsonInput = decodeFromBase64(base64);
        return gson.fromJson(jsonInput, clazz);
    }

    public static boolean isBase64(String input) {
        String trimmedInput = input.trim();

        if (trimmedInput.length() % 4 != 0) {
            return false;
        }

        for (char c : trimmedInput.toCharArray()) {
            if (!(Character.isLetterOrDigit(c) || c == '+' || c == '/' || c == '=')) {
                return false;
            }
        }

        try {
            Base64.getDecoder().decode(trimmedInput);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
