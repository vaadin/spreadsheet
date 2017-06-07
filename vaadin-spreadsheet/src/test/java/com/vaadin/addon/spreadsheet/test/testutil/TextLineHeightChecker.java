package com.vaadin.addon.spreadsheet.test.testutil;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

/**
 * Utility class to check the proper height for a single line of text.
 */
public class TextLineHeightChecker {

    // Boundaries for an acceptable line height size in terms of
    // percentage of font size
    private static final float MINIMUM_PERCENTAGE_OF_FONT_SIZE = 1.1f;
    private static final float MAXIMUM_PERCENTAGE_OF_FONT_SIZE = 1.2f;

    /**
     * Asserts that the {@code cellHeightInPoints} is acceptable for the given
     * {@code fontSize} and number of text lines ({@code expectedLines}).<br><br>
     * <p>
     * The acceptance criteria is that {@code cellHeightInPoints} must be between
     * <b>{@value #MINIMUM_PERCENTAGE_OF_FONT_SIZE}</b> and
     * <b>{@value #MAXIMUM_PERCENTAGE_OF_FONT_SIZE}</b>
     * times the <b>{@code fontSize} * {@code expectedLines}</b>
     *
     * @param fontSize
     *     The actual font size
     * @param cellHeightInPoints
     *     The actual cell height
     * @param expectedLines
     *     The expected number of text lines in the cell
     */
    public static void assertThatCellHeightIsAcceptable(
        float cellHeightInPoints, float fontSize, int expectedLines) {
        float minimumRowHeight = fontSize * MINIMUM_PERCENTAGE_OF_FONT_SIZE;
        float maximumRowHeight = fontSize * MAXIMUM_PERCENTAGE_OF_FONT_SIZE;

        // Row height must properly fit the font size with a "security" margin
        float expectedSingleTextLineHeight = cellHeightInPoints / expectedLines;
        assertThat(expectedSingleTextLineHeight,
            allOf(greaterThan(minimumRowHeight), lessThan(maximumRowHeight)));
    }

}
