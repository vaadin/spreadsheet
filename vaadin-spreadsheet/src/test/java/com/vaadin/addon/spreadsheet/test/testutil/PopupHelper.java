package com.vaadin.addon.spreadsheet.test.testutil;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchTestCase;

/**
 * PopupHelper
 */
public class PopupHelper extends SeleniumHelper {

	private String mainWindowHandle;
	
	/**
	 * @param driver
	 */
	public PopupHelper(WebDriver driver) {
		super(driver);
		mainWindowHandle = driver.getWindowHandle();
	}

	/**
	 * switchToPopup
	 */
	public void switchToPopup () {
		Set<String> handleSet = driver.getWindowHandles();
		
		Iterator<String> ite = handleSet.iterator();
		while (ite.hasNext()) {
			String popupHandle = ite.next();
			if (!popupHandle.contains(mainWindowHandle)) {
				driver.switchTo().window(popupHandle);
				TestBenchTestCase.testBench(driver).waitForVaadin();
				return;
			}
		}
	}
	
	/**
	 * backToMainWindow
	 */
	public void backToMainWindow () {
		driver.switchTo().window(mainWindowHandle);
		TestBenchTestCase.testBench(driver).waitForVaadin();
	}
}
