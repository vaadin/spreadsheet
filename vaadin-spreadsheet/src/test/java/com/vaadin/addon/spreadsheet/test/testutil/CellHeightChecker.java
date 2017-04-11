package com.vaadin.addon.spreadsheet.test.testutil;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import com.vaadin.addon.spreadsheet.Spreadsheet;

/**
 * Utility class to check the proper height for a single cell of text.
 */
public class CellHeightChecker {

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
     * @param securityMarginPercentage
     *     The margin in terms of percentage of the full cell width/height
     *     that must be taken into account when autofitting.
     * @param expectedLines
     *     The expected number of text lines in the cell
     */
    public static void assertThatCellHeightIsAcceptable(
        float cellHeightInPoints, float fontSize,
        float securityMarginPercentage, int expectedLines) {
        float minimumLineHeight = fontSize * MINIMUM_PERCENTAGE_OF_FONT_SIZE;
        float maximumLineHeight = fontSize * MAXIMUM_PERCENTAGE_OF_FONT_SIZE;

        float minimumRowHeight = minimumLineHeight
            + minimumLineHeight * Spreadsheet.CSS_LINE_HEIGHT_PERCENTAGE * (
            expectedLines - 1);
        minimumRowHeight += minimumRowHeight * securityMarginPercentage;

        float maximumRowHeight = maximumLineHeight
            + maximumLineHeight * Spreadsheet.CSS_LINE_HEIGHT_PERCENTAGE * (
            expectedLines - 1);
        maximumRowHeight += maximumRowHeight * securityMarginPercentage;

        // Row height must properly fit the font size with a "security" margin
        assertThat(cellHeightInPoints,
            allOf(greaterThan(minimumRowHeight), lessThan(maximumRowHeight)));
    }

}
