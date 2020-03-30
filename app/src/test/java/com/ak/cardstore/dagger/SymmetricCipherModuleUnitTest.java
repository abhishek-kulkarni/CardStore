package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.symmetric.SymmetricKeyCipher;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyGenerator;
import com.ak.cardstore.cipher.symmetric.SymmetricKeyRetriever;

import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Abhishek
 */
public class SymmetricCipherModuleUnitTest {

    private static final TestComponent SYMMETRIC_CIPHER_TEST_COMPONENT = DaggerSymmetricCipherModuleUnitTest_TestComponent.create();

    @Test
    public void testProvideSymmetricKeyCipher() {
        final SymmetricKeyCipher symmetricKeyCipher = SYMMETRIC_CIPHER_TEST_COMPONENT.provideSymmetricKeyCipher();
        final SymmetricKeyCipher symmetricKeyCipherOther = SYMMETRIC_CIPHER_TEST_COMPONENT.provideSymmetricKeyCipher();

        assertNotNull(symmetricKeyCipher);
        assertNotNull(symmetricKeyCipherOther);
        assertEquals(symmetricKeyCipher, symmetricKeyCipherOther);
    }

    @Test
    public void testProvideSymmetricKeyRetriever() {
        final SymmetricKeyRetriever symmetricKeyRetriever = SYMMETRIC_CIPHER_TEST_COMPONENT.provideSymmetricKeyRetriever();
        final SymmetricKeyRetriever symmetricKeyRetrieverOther = SYMMETRIC_CIPHER_TEST_COMPONENT.provideSymmetricKeyRetriever();

        assertNotNull(symmetricKeyRetriever);
        assertNotNull(symmetricKeyRetrieverOther);
        assertEquals(symmetricKeyRetriever, symmetricKeyRetrieverOther);
    }

    @Test
    public void testProvideSymmetricKeyGenerator() {
        final SymmetricKeyGenerator symmetricKeyGenerator = SYMMETRIC_CIPHER_TEST_COMPONENT.provideSymmetricKeyGenerator();
        final SymmetricKeyGenerator symmetricKeyGeneratorOther = SYMMETRIC_CIPHER_TEST_COMPONENT.provideSymmetricKeyGenerator();

        assertNotNull(symmetricKeyGenerator);
        assertNotNull(symmetricKeyGeneratorOther);
        assertEquals(symmetricKeyGenerator, symmetricKeyGeneratorOther);
    }

    @Singleton
    @Component(modules = SymmetricCipherModule.class)
    public interface TestComponent {
        SymmetricKeyCipher provideSymmetricKeyCipher();

        SymmetricKeyRetriever provideSymmetricKeyRetriever();

        SymmetricKeyGenerator provideSymmetricKeyGenerator();
    }
}
