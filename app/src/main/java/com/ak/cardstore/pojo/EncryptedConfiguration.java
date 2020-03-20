package com.ak.cardstore.pojo;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents an encrypted configuration consisting of serialized and encrypted {@link Wallet} and initial vector needed for decryption.
 *
 * @author Abhishek
 */

@Getter
@Builder
@EqualsAndHashCode
public class EncryptedConfiguration {

    final String serializedAndEncryptedWallet;

    final String initialVector;
}
