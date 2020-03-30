package com.ak.cardstore.dagger;

import com.ak.cardstore.cipher.CipherOperator;
import com.ak.cardstore.cipher.CipherRetriever;
import com.ak.cardstore.cipher.KeyStoreRetriever;

import org.junit.jupiter.api.Test;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Abhishek
 */

public class CipherModuleUnitTest {

    private static final TestComponent CIPHER_MODULE_TEST_COMPONENT = DaggerCipherModuleUnitTest_TestComponent.create();

    @Test
    public void testCipherRetriever() {
        final CipherRetriever cipherRetriever = CIPHER_MODULE_TEST_COMPONENT.provideCipherRetriever();
        final CipherRetriever cipherRetrieverOther = CIPHER_MODULE_TEST_COMPONENT.provideCipherRetriever();

        assertNotNull(cipherRetriever);
        assertNotNull(cipherRetrieverOther);
        assertEquals(cipherRetriever, cipherRetrieverOther);
    }

    @Test
    public void testCipherOperator() {
        final CipherOperator cipherOperator = CIPHER_MODULE_TEST_COMPONENT.provideCipherOperator();
        final CipherOperator cipherOperatorOther = CIPHER_MODULE_TEST_COMPONENT.provideCipherOperator();

        assertNotNull(cipherOperator);
        assertNotNull(cipherOperatorOther);
        assertEquals(cipherOperator, cipherOperatorOther);
    }

    @Test
    public void testKeyStoreRetriever() {
        final KeyStoreRetriever keyStoreRetriever = CIPHER_MODULE_TEST_COMPONENT.provideKeyStoreRetriever();
        final KeyStoreRetriever keyStoreRetrieverOther = CIPHER_MODULE_TEST_COMPONENT.provideKeyStoreRetriever();

        assertNotNull(keyStoreRetriever);
        assertNotNull(keyStoreRetrieverOther);
        assertEquals(keyStoreRetriever, keyStoreRetrieverOther);
    }

    @Singleton
    @Component(modules = CipherModule.class)
    public interface TestComponent {
        CipherRetriever provideCipherRetriever();

        CipherOperator provideCipherOperator();

        KeyStoreRetriever provideKeyStoreRetriever();
    }
}