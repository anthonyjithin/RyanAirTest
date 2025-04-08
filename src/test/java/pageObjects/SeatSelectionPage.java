package pageObjects;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.BasePage;
import base.ExtentManager;
import utility.TestUtility;



public class SeatSelectionPage extends TestUtility {

	private static final Logger logger = LogManager.getLogger(SeatSelectionPage.class);

	public SeatSelectionPage() throws IOException {
		super();
		PageFactory.initElements(getDriver(), this);
		logger.info("SeatSelectionPage initialized");

	}


	@FindBy(xpath = "//button[@class= 'passenger-carousel__cta passenger-carousel__cta--next ry-button--gradient-yellow']")
	private WebElement nextFlight;

	@FindBy(xpath = "//button[contains(@id, 'seat')]")
	private List<WebElement> allSeats;

	@FindBy(css = ".seats-offer__buttons .ry-button--anchor:nth-of-type(1)")
	private WebElement noThanks;

	@FindBy(css = ".passenger-carousel__cta--next")
	private WebElement continueToSeat;

	@FindBy(css = "ry-enhanced-takeover-beta-desktop .ry-button--anchor.ry-button--anchor-blue")
	private WebElement noThanksFastTrack;

	@FindBy(xpath = "//div[contains(@class, 'passenger-carousel__orig-dest subtitle-s-lg passenger-carousel__orig-dest--active')]")
	private WebElement departureFlightStatus;

	@FindBy(xpath = "//div[contains(@class, 'passenger-carousel__orig-dest subtitle-s-lg passenger-carousel__orig-dest--active')]")
	private WebElement arrivalFlightStatus;

	
	// getter methods
	public List<WebElement> getAllSeats() {
		return allSeats;
	}

	// Action methods
	//method to select the departure seat selection 
	public String isDepartSeatSelectionLoaded() throws IOException {
		waitForElementClick(departureFlightStatus, Duration.ofSeconds(15));
		String deptElementClass = departureFlightStatus.getDomAttribute("class");
		return deptElementClass;

	}
	
	// Once the seat is selected for the first flight this method is called to load the second flight
	// seat selection page
	public void nextFlightSelection() throws IOException {	
		waitForElementClick(nextFlight, Duration.ofSeconds(15));
		nextFlight.click();
	}
	
	// A prompt shows asking customer if they want to select the same seat they selected for departure flight
	// as the seat for return flight as well. try catch block is used as the prompt doesn't show all the time
	// the driver waits for the presence of the prompt for 5 second and if it shows within that time then the 'No, Thanks' buttons is clicked
	// and if it doesn't show then the test execution is continued
	public void closeNoThanksPrompt() throws IOException {
		try {
			waitAlertPresence(By.cssSelector(".seats-offer__buttons .ry-button--anchor:nth-of-type(1)"),
					Duration.ofMillis(5000));

			if (noThanks.isDisplayed()) {
				noThanks.click();
				ExtentManager.pass("Prompt closed");

			}
		} catch (TimeoutException e) {
			ExtentManager.pass("No popup appeared, continuing...");

		}
	}
	
	// Method checks if the arrival seat selection page is loaded, here the class tag from the element is stored to a String which then 
	// uses assertion to find if the element is active. The class attribute would only has active text if its active, if its deactivated then
	// the class attribute doesnt have an active text
	public String isArrivalSeatSelectionLoaded() throws IOException {
		waitForElementClick(arrivalFlightStatus, Duration.ofSeconds(15));
		String arrivalElementClass = arrivalFlightStatus.getDomAttribute("class");
		return arrivalElementClass;

	}

	
	// Prompt shows up to select a premium fast track service and the prompt is closed
	public void closeFastTrackPrompt() {
		if (noThanksFastTrack.isDisplayed()) {
			noThanksFastTrack.click();
		}
	}
	
	// Mehtod is called to click the continue button after the seats of both flight have been selected
	public void selectContinueToSeat() {
		logger.info("Selecting continue to seat");
		continueToSeat.click();
	}

}
