package com.ak.cardstore.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * Implementation of {@link Serializer} backed by {@link com.google.gson.Gson}
 *
 * @author Abhishek
 */

@AllArgsConstructor
public class GsonSerializer<OBJECT_TYPE> implements Serializer<OBJECT_TYPE> {

    private static final Gson GSON_CONVERTER = new GsonBuilder()
            .create();

    @NonNull
    private final Class<OBJECT_TYPE> objectTypeClass;

    @Override
    public String serialize(final OBJECT_TYPE objectToSerialize) {
        if (null == objectToSerialize) {
            return null;
        }

        final String serializedObject = GSON_CONVERTER.toJson(objectToSerialize);
        return serializedObject;
    }

    @Override
    public OBJECT_TYPE deserialize(final String serializedObject) {
        if (null == serializedObject) {
            return null;
        }

        final OBJECT_TYPE object = GSON_CONVERTER.fromJson(serializedObject, this.objectTypeClass);
        return object;
    }
}
