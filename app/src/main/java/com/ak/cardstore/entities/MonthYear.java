package com.ak.cardstore.entities;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * An entity to represent Month/Year
 *
 * @author Abhishek K.
 */

@Getter
@Builder
public final class MonthYear {

    private static final String TO_STRING_FORMAT = "%02d/%4d";

    @NonNull
    final Month month;

    @NonNull
    final Year year;

    public boolean isBefore(final ZonedDateTime dateTime) {
        final YearMonth expiryYearMonth = YearMonth.of(this.year.getValue(), this.month);
        final YearMonth dateTimeYearMonth = YearMonth.from(dateTime.toLocalDateTime());

        return expiryYearMonth.isBefore(dateTimeYearMonth);
    }

    public static MonthYear of(final Month month, final int year) {
        return MonthYear.builder()
                .month(month)
                .year(Year.of(year))
                .build();
    }

    @Override
    public String toString() {
        final String monthYearString = String.format(TO_STRING_FORMAT, this.month.getValue(), this.year.getValue());
        return monthYearString;
    }
}
