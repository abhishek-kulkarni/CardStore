package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairCipher;
import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairGenerator;
import com.ak.cardstore.cipher.asymmetric.AsymmetricKeyPairRetriever;

import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Abhishek
 */
public class AsymmetricCipherModuleUnitTest {

    private static final TestComponent ASYMMETRIC_CIPHER_TEST_COMPONENT = DaggerAsymmetricCipherModuleUnitTest_TestComponent.create();

    @Test
    public void testProvideAsymmetricKeyCipher() {
        final AsymmetricKeyPairCipher asymmetricKeyPairCipher = ASYMMETRIC_CIPHER_TEST_COMPONENT.provideAsymmetricKeyPairCipher();
        final AsymmetricKeyPairCipher asymmetricKeyPairCipherOther = ASYMMETRIC_CIPHER_TEST_COMPONENT.provideAsymmetricKeyPairCipher();

        assertNotNull(asymmetricKeyPairCipher);
        assertNotNull(asymmetricKeyPairCipherOther);
        assertEquals(asymmetricKeyPairCipher, asymmetricKeyPairCipherOther);
    }

    @Test
    public void testProvideAsymmetricKeyRetriever() {
        final AsymmetricKeyPairRetriever asymmetricKeyPairRetriever = ASYMMETRIC_CIPHER_TEST_COMPONENT.provideAsymmetricKeyPairRetriever();
        final AsymmetricKeyPairRetriever asymmetricKeyPairRetrieverOther = ASYMMETRIC_CIPHER_TEST_COMPONENT.provideAsymmetricKeyPairRetriever();

        assertNotNull(asymmetricKeyPairRetriever);
        assertNotNull(asymmetricKeyPairRetrieverOther);
        assertEquals(asymmetricKeyPairRetriever, asymmetricKeyPairRetrieverOther);
    }

    @Test
    public void testProvideAsymmetricKeyGenerator() {
        final AsymmetricKeyPairGenerator asymmetricKeyPairGenerator = ASYMMETRIC_CIPHER_TEST_COMPONENT.provideAsymmetricKeyPairGenerator();
        final AsymmetricKeyPairGenerator asymmetricKeyPairGeneratorOther = ASYMMETRIC_CIPHER_TEST_COMPONENT.provideAsymmetricKeyPairGenerator();

        assertNotNull(asymmetricKeyPairGenerator);
        assertNotNull(asymmetricKeyPairGeneratorOther);
        assertEquals(asymmetricKeyPairGenerator, asymmetricKeyPairGeneratorOther);
    }

    @Singleton
    @Component(modules = AsymmetricCipherModule.class)
    public interface TestComponent {
        AsymmetricKeyPairCipher provideAsymmetricKeyPairCipher();

        AsymmetricKeyPairRetriever provideAsymmetricKeyPairRetriever();

        AsymmetricKeyPairGenerator provideAsymmetricKeyPairGenerator();
    }
}
