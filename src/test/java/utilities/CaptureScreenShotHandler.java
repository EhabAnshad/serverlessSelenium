package utilities;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

/**
 * This Class designed to handle screenshot capture
 * Contains single method that utilizes the driver capture screenshot
 * It uses stream to write down the captured image to minimize IO operations
 */
public class CaptureScreenShotHandler {
	public static void captureScreenshot(WebDriver driver, String screenshotname) {
		try {
			Path dest = Paths.get(".", "test-output", "ScreenShots", LocalDateTime.now() + "-" + screenshotname + ".png");
			Files.createDirectories(dest.getParent());
			FileOutputStream out = new FileOutputStream(dest.toString());
			out.write(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
			out.close();
		} catch (Exception e) {
			Reporter.log("Exception while taking screenshot" + e.getMessage());
		}
	}
}
