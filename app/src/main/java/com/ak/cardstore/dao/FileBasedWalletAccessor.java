package com.ak.cardstore.dao;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.ak.cardstore.pojo.Wallet;

import java.security.KeyStore;
import java.security.spec.ECField;
import java.util.Enumeration;

import lombok.extern.log4j.Log4j2;

/**
 * Implementation of {@link WalletAccessor} backed by file storage.
 *
 * @author Abhishek
 */

@Log4j2
public class FileBasedWalletAccessor implements WalletAccessor {

    private static final String KEY_STORE_ALIAS = "com.ak.cardstore.Keys";
    private static final String WALLET_FILE_NAME = "wallet.cdb";
    private static final int AUTHENTICATION_VALIDITY_DURATION_SECONDS = 3;

    @Override
    public void save(final Wallet wallet) {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            Enumeration<String> aliases = ks.aliases();
            System.out.println(aliases);
        } catch (Exception e) {
            System.out.println();
        }


        final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(KEY_STORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_ENCRYPT)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_VALIDITY_DURATION_SECONDS)
                .build();
    }

    @Override
    public Wallet retrieve() {
        return null;
    }
}
