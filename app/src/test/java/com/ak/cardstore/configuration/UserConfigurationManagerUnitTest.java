package com.ak.cardstore.configuration;

import com.ak.cardstore.Make;
import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairCipher;
import com.ak.cardstore.dao.SharedPreferencesDataAccessor;
import com.ak.cardstore.pojo.User;
import com.ak.cardstore.serialization.Serializer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Abhishek
 */

@ExtendWith(MockitoExtension.class)
public class UserConfigurationManagerUnitTest {

    private static final String CONFIGURATION_FILE_NAME = "com.ak.cardstore.user.udb";
    private static final String PREFERENCES_KEY = "Password";

    @Mock
    private Serializer<User> mockUserSerializer;

    @Mock
    private AsymmetricKeyPairCipher mockAsymmetricKeyPairCipher;

    @Mock
    private SharedPreferencesDataAccessor mockSharedPreferencesDataAccessor;

    @InjectMocks
    private UserConfigurationManager userConfigurationManager;

    @Test
    public void testSave() {
        final User user = Make.aValidUser();

        final String serializedUser = Make.aString();
        final String encryptedSerializedUser = Make.aString();

        when(this.mockUserSerializer.serialize(user)).thenReturn(serializedUser);
        when(this.mockAsymmetricKeyPairCipher.encrypt(serializedUser)).thenReturn(encryptedSerializedUser);
        doNothing().when(this.mockSharedPreferencesDataAccessor).save(CONFIGURATION_FILE_NAME, PREFERENCES_KEY, encryptedSerializedUser);

        this.userConfigurationManager.save(user);

        verify(this.mockUserSerializer).serialize(user);
        verify(this.mockAsymmetricKeyPairCipher).encrypt(serializedUser);
        verify(this.mockSharedPreferencesDataAccessor).save(CONFIGURATION_FILE_NAME, PREFERENCES_KEY, encryptedSerializedUser);
    }

    @Test
    public void testLoad() {
        final User expectedUser = Make.aValidUser();

        final String serializedUser = Make.aString();
        final String encryptedSerializedUser = Make.aString();

        when(this.mockSharedPreferencesDataAccessor.get(CONFIGURATION_FILE_NAME, PREFERENCES_KEY)).thenReturn(encryptedSerializedUser);
        when(this.mockAsymmetricKeyPairCipher.decrypt(encryptedSerializedUser)).thenReturn(serializedUser);
        when(this.mockUserSerializer.deserialize(serializedUser)).thenReturn(expectedUser);

        final User user = this.userConfigurationManager.load();
        Assertions.assertSame(expectedUser, user);

        verify(this.mockSharedPreferencesDataAccessor).get(CONFIGURATION_FILE_NAME, PREFERENCES_KEY);
        verify(this.mockAsymmetricKeyPairCipher).decrypt(encryptedSerializedUser);
        verify(this.mockUserSerializer).deserialize(serializedUser);
    }
}
