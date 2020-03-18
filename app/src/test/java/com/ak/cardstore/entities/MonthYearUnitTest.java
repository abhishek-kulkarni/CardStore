package com.ak.cardstore.entities;

import com.ak.cardstore.Make;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Month;
import java.time.Year;
import java.util.stream.Stream;

public class MonthYearUnitTest {

    @Test
    public void test_WithNullMonth() {
        Assertions.assertThrows(NullPointerException.class,
                () -> MonthYear.builder().month(null).year(Make.aYear()).build());
    }

    @Test
    public void test_WithNullYear() {
        Assertions.assertThrows(NullPointerException.class,
                () -> MonthYear.builder().month(Make.aMonth()).year(null).build());
    }

    @ParameterizedTest
    @MethodSource("testArgumentsProvider")
    public void testToString(final Month month, final Year year, final String expectedMonthYear) {
        final MonthYear monthYear = MonthYear.builder()
                .month(month)
                .year(year)
                .build();

        Assertions.assertEquals(expectedMonthYear, monthYear.toString());
    }

    private static Stream<Arguments> testArgumentsProvider() {
        final String monthYearStringFormat = "%02d/%4d";

        Year year = Make.aYear();
        final Arguments janArguments = Arguments.arguments(Month.JANUARY, year,
                String.format(monthYearStringFormat, Month.JANUARY.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments febArguments = Arguments.arguments(Month.FEBRUARY, year,
                String.format(monthYearStringFormat, Month.FEBRUARY.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments marArguments = Arguments.arguments(Month.MARCH, year,
                String.format(monthYearStringFormat, Month.MARCH.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments aprArguments = Arguments.arguments(Month.APRIL, year,
                String.format(monthYearStringFormat, Month.APRIL.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments mayArguments = Arguments.arguments(Month.MAY, year,
                String.format(monthYearStringFormat, Month.MAY.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments junArguments = Arguments.arguments(Month.JUNE, year,
                String.format(monthYearStringFormat, Month.JUNE.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments julArguments = Arguments.arguments(Month.JULY, year,
                String.format(monthYearStringFormat, Month.JULY.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments augArguments = Arguments.arguments(Month.AUGUST, year,
                String.format(monthYearStringFormat, Month.AUGUST.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments sepArguments = Arguments.arguments(Month.SEPTEMBER, year,
                String.format(monthYearStringFormat, Month.SEPTEMBER.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments octArguments = Arguments.arguments(Month.OCTOBER, year,
                String.format(monthYearStringFormat, Month.OCTOBER.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments novArguments = Arguments.arguments(Month.NOVEMBER, year,
                String.format(monthYearStringFormat, Month.NOVEMBER.getValue(), year.getValue()));
        year = Make.aYear();
        final Arguments decArguments = Arguments.arguments(Month.DECEMBER, year,
                String.format(monthYearStringFormat, Month.DECEMBER.getValue(), year.getValue()));

        return Stream.of(
                janArguments, febArguments, marArguments, aprArguments, mayArguments, junArguments, julArguments, augArguments,
                sepArguments, octArguments, novArguments, decArguments
        );
    }
}