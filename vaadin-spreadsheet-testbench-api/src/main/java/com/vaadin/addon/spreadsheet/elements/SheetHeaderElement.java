package com.vaadin.addon.spreadsheet.elements;

/*
 * #%L
 * Vaadin Spreadsheet Testbench API
 * %%
 * Copyright (C) 2013 - 2016 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * 
 * See the file license.html distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import org.openqa.selenium.By;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.AbstractElement;

/**
 * This class represents a header (either row- or column) within the currently
 * active sheet of a Spreadsheet.
 * 
 * @author Vaadin Ltd.
 */
public class SheetHeaderElement extends AbstractElement {

    public TestBenchElement getResizeHandle() {
        return wrapElement(
                findElement(By.className("header-resize-dnd-second")),
                getCommandExecutor());
    }
    

    /**
     * Override to allow cell context menu tests to pass for PhantomJS.
     * See the linked issue below for code.  PhantomJS doesn't support context
     * menu directly with Selenium.
     * 
     * @see com.vaadin.testbench.TestBenchElement#contextClick()
     * @see "https://github.com/ariya/phantomjs/issues/14005"
     */
    @Override
    public void contextClick() {
        // for PhantomJS, this won't open the context menu
        super.contextClick();
        ElementUtil.phantomJSContextClick(getDriver(), getWrappedElement());
    }
}
