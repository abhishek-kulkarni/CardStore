package com.ak.cardstore.pojo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents an encrypted wallet.
 *
 * @author Abhishek
 */

@Getter
@Builder
@EqualsAndHashCode
public class EncryptedWallet {

    final String serializedAndEncryptedWallet;

    final String initialVector;
}
