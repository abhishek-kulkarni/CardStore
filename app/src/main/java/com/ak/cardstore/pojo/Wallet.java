package com.ak.cardstore.pojo;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;

/**
 * A POJO to represent a wallet with bunch of {@link Card}s
 *
 * @author Abhishek
 */

@Getter
@Builder
public class Wallet {

    private final Set<Card> cards;
}
