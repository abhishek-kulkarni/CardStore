package com.ak.cardstore;

import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.security.Provider;

/**
 * A class used to mock {@link KeyStore} methods
 *
 * @author Abhishek
 */

public class TestKeyStore extends KeyStore {

    public TestKeyStore(final KeyStoreSpi keyStoreSpi, final Provider provider, final String type) {
        super(keyStoreSpi, provider, type);
    }
}
