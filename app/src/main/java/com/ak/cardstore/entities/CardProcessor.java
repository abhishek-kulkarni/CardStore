package com.ak.cardstore.entities;

import lombok.AllArgsConstructor;

/**
 * Specifies card processor companies
 */

@AllArgsConstructor
public enum CardProcessor {

    AmEx("American Express"),

    Visa("Visa"),

    MasterCard("Master Card"),

    Discover("Discover");

    private final String displayName;

    /**
     * Returns the card processor display name
     *
     * @return card processor display name
     */
    public String getDisplayName() {
        return this.displayName;
    }
}
