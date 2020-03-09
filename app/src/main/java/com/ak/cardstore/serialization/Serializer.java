package com.ak.cardstore.serialization;

/**
 * An interface to serialize and deserialize the objects.
 *
 * @param <OBJECT_TYPE> type of the object to be serialized
 */

public interface Serializer<OBJECT_TYPE> {

    /**
     * Serializes the passed object and returns the serialized object.
     *
     * @param objectToSerialize object to serialize
     * @return serialized (nullable) object
     */
    String serialize(final OBJECT_TYPE objectToSerialize);

    /**
     * Resurrects the serialized object and returns it.
     *
     * @param serializedObject object to deserialize
     * @return resurrected (nullable) object
     */
    OBJECT_TYPE deserialize(final String serializedObject);
}
