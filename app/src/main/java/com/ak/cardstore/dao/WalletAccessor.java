package com.ak.cardstore.dao;

import com.ak.cardstore.pojo.Wallet;

/**
 * An interface to save and retrieve the Wallet.
 *
 * @author Abhishek
 */

public interface WalletAccessor {

    /**
     * Saves the {@link Wallet} to the underlying store.
     *
     * @param wallet wallet to be saved
     */
    void save(final Wallet wallet);

    /**
     * Retrieves the {@link Wallet} from the underlying store.
     *
     * @return retrieved wallet
     */
    Wallet retrieve();
}
