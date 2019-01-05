package com.vaadin.addon.spreadsheet.test.testutil;

import org.openqa.selenium.WebDriver;

/**
 * SeleniumHelper
 */
public class SeleniumHelper {
	
	/**
	 * driver
	 */
	protected WebDriver driver;
	
	/**
	 * @param driver
	 */
	public SeleniumHelper(WebDriver driver) {
		this.driver = driver;
	}
}
