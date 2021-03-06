package com.ak.cardstore.pojo;

import java.util.Set;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A POJO to represent a wallet with {@link Card}s
 *
 * @author Abhishek
 */

@Getter
@Builder
@EqualsAndHashCode
public class Wallet {

    private final Set<Card> cards;
}
