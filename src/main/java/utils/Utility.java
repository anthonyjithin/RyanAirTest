package utils;

import java.io.File;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import base.BasePage;

public class Utility extends BasePage {
	public static String screenShotDestinationPath;

	public Utility() throws IOException {
		super();
	}

	// method to take screenshot, save the screenshot with name passed in a parameter
	public static String takeSnapShot(String name) throws WebDriverException, IOException {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);

		String destFile = System.getProperty("user.dir") + File.separator + "target" + File.separator + "screenshots"
				+ File.separator + timestamp() + ".png";
		screenShotDestinationPath = destFile;

		try {
			FileUtils.copyFile(srcFile, new File(destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	// generates a timestamp string used in snapshot filename
	public static String timestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
	}

	// returns screenshot path
	public static String getScreenShotDestinationPath() {
		return screenShotDestinationPath;
	}

}
