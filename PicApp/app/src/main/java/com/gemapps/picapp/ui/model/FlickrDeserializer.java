package com.gemapps.picapp.ui.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by edu on 10/26/16.
 */

public class FlickrDeserializer<T> implements JsonDeserializer<T> {

    public static final String PHOTO_KEY = "photo";

    private static final String TAG = "FlickrDeserializer";
    private String mKey;

    public FlickrDeserializer(String key) {
        mKey = key;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonElement jsonElement = json.getAsJsonObject().get(mKey);

        return new Gson().fromJson(jsonElement, typeOfT);
    }
}
