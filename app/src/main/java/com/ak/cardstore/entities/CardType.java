package com.ak.cardstore.entities;

import lombok.AllArgsConstructor;

/**
 * Specifies the card type
 */

@AllArgsConstructor
public enum CardType {

    CREDIT("Credit"),

    DEBIT("Debit");

    private final String displayName;

    /**
     * Returns the card type display name
     *
     * @return card type display name
     */
    public String getDisplayName() {
        return this.displayName;
    }
}
