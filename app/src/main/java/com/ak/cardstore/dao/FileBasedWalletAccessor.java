package com.ak.cardstore.dao;

import com.ak.cardstore.pojo.Wallet;

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
    }

    @Override
    public Wallet retrieve() {
        return null;
    }
}
