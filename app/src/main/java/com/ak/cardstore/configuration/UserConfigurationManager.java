package com.ak.cardstore.configuration;

import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairCipher;
import com.ak.cardstore.dao.SharedPreferencesDataAccessor;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.serialization.Serializer;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * A class to manage the user configuration.
 *
 * @author Abhishek
 */

@Log4j2
@AllArgsConstructor
public class UserConfigurationManager {

    private static final String CONFIGURATION_FILE_NAME = "com.ak.cardstore.user.udb";
    private static final String PREFERENCES_KEY = "Password";

    private final Serializer<User> userSerializer;
    private final AsymmetricKeyPairCipher asymmetricKeyPairCipher;
    private final SharedPreferencesDataAccessor sharedPreferencesDataAccessor;

    /**
     * Saves the application configuration by executing the following steps
     * 1. Serialize the user
     * 2. Encrypt the user
     * 3. Save the serialized encrypted user
     *
     * @param user user to save
     */
    public void save(final User user) {
        final String serializedUser = this.userSerializer.serialize(user);
        final String encryptedSerializedUser = this.asymmetricKeyPairCipher.encrypt(serializedUser);
        this.sharedPreferencesDataAccessor.save(CONFIGURATION_FILE_NAME, PREFERENCES_KEY, encryptedSerializedUser);
    }

    /**
     * Loads the application configuration by executing the following steps
     * 1. Read the encrypted serialized user
     * 2. Decrypt the encrypted serialized user
     * 3. Deserialize the serialized user
     *
     * @return User
     */
    public User load() {
        final String encryptedSerializedUser = this.sharedPreferencesDataAccessor.get(CONFIGURATION_FILE_NAME, PREFERENCES_KEY);
        final String serializedUser = this.asymmetricKeyPairCipher.decrypt(encryptedSerializedUser);

        final User user = this.userSerializer.deserialize(serializedUser);
        return user;
    }
}
