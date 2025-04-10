package pageObjects;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.TestUtility;

public class FlightSelectionPage extends TestUtility {
	// private WebDriver driver;
	private static final Logger logger = LogManager.getLogger(FlightSelectionPage.class);

	public FlightSelectionPage() throws IOException {
		super();
		// this.driver = getDriver();
		PageFactory.initElements(getDriver(), this);
		logger.info("FlightSelectionPage initialized");
	}

	@FindBy(xpath = "//button[contains(@class, 'flight-card-summary__select-btn') and contains(@class, 'ry-button--gradient-blue')]")
	private List<WebElement> flightSelectButtons;

	@FindBy(xpath = "//journey-container[@name='inbound']//flight-card-summary//button[contains(@class, 'flight-card-summary__select-btn') and contains(@class, 'ry-button--gradient-blue')]")
	private WebElement arrivalFlightSelectionButton;
	
	@FindBy(xpath = "//div[contains(@class,'details__bottom-bar')]")
	private WebElement section;

	By departureFlightText = By.xpath("//h3[contains(normalize-space(), 'Dublin to Naples')]");


	@FindBy(xpath = "//h3[contains(normalize-space(), 'Naples to Dublin')]")
	private WebElement arrivalFlightText;

	private By regularFlightSelector = By.cssSelector(".fare-table__fare-column-border--regu");

	@FindBy(css = ".login-touchpoint__login-later.title-m-lg.title-m-sm")
	private WebElement loginLaterBtn;

	@FindBy(css = ".login-touchpoint__title.title-m-lg.title-m-sm")
	private WebElement loginToMyRyanairText;

	@FindBy(css = ".form-wrapper.form-wrapper--disabled")
	private WebElement passengerSectionDisabled;

	@FindBy(css = ".form-wrapper")
	private WebElement passengerSectionEnabled;

	@FindBy(css = "[type='button']")
	private WebElement passengerTitleBtn;

	
	@FindBy(id = "form.passengers.ADT-0.name")
	private WebElement passengerFirstName;

	@FindBy(id = "form.passengers.ADT-0.surname")
	private WebElement passengerLastName;

	@FindBy(xpath = "//html//body//app-root//flights-root//div//div//div//div//flights-lazy-content//ng-component//div//continue-flow-container")
	private WebElement continueBtn;

	By passengerTitleSelect = By.xpath("//div[contains(@class,'dropdown-item__label') and normalize-space()='Mr']");

	// getter methods

	public WebElement getLoginToMyRyanairText() {
		return loginToMyRyanairText;
	}
	
	public WebElement getSection() {
		return section;
	}

	// Action methods

	// Method to load the departure flight text
	public String getDepartureFlightText() {
		String departureFlight = getDriver().findElement(departureFlightText).getText().trim();
		return departureFlight;
	}
	 
	// Method to select the departure flight, Driver could not click this button as two elements are returned for this selector when the DOM loads
	// this used to return WebElement cannot be located error, so I have stored both the elements which are found on dome onto a List fo WebElements
	// and then picks the first element which was found using get(0) which will return the first select button on the page
	public void selectDepartureFlight() throws IOException, InterruptedException {
		if (!flightSelectButtons.isEmpty()) {
			WebElement departureButton = flightSelectButtons.get(0);
			scrollIntoView(departureButton);
			waitForElementClick(departureButton, Duration.ofSeconds(10));
			departureButton.click();
			logger.info("Departure flight selected");
		} else {
			logger.error("No flight selection buttons found");
			throw new NoSuchElementException("Flight selection button not found");
		}

	}

	// Method to load the departure flight text
	public String getArrivalFlightText() throws IOException {
		scrollIntoView(arrivalFlightText);
		String arrivalFlight = arrivalFlightText.getText().trim();
		return arrivalFlight;
	}

	// Method to select the arrival flight button, a custom xpath locator is used to locate the select button from the outbound journey container
	// using the class name and outbound name, this custom selector would not give duplicate elements
	public void selectArrivalFlight() throws IOException {
		scrollIntoView(arrivalFlightSelectionButton);
		waitForElementClick(arrivalFlightSelectionButton, Duration.ofSeconds(10));
		arrivalFlightSelectionButton.click();
		logger.info("Arrival flight selected successfully");
	}
	
	// Method to select the regular flight option, the DOM refreshes by the time driver tries to click this button and stale element exception is
	// thrown. So a try catch block is used here to see if the element is throwing the exception, if it is stale then retry the element
 	// using Expected conditions refreshed which then locates the element 
	public void selectRegularFlight() throws IOException {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
		// try catch block to handle the stale element exception
		try {
			WebElement regular = wait.until(ExpectedConditions.elementToBeClickable(regularFlightSelector));
			regular.click();
			logger.info("Regular fare selected");
			// to re-fetch the element
		} catch (StaleElementReferenceException e) {
			logger.warn("Stale element, retrying...");
			WebElement regular = wait.until(
					ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(regularFlightSelector)));
			scrollIntoView(regular);
			regular.click();
			logger.info("Regular fare selected after refetch");
		}
	}
	
	// Method checks for the passenger section is disbaled or not, it checks the class attribute of the element using getDomAttribute method
	// which contains disabled only when the section is not active
	public boolean isPassengerDisabled() throws IOException {
		boolean isDisabled = passengerSectionDisabled.getDomAttribute("class").contains("disabled");
		return isDisabled;
	}
	
	// Method checks for the passenger section is enabled or not, it checks the class attribute of the element using getDomAttribute method
	// which contains enabled only when the section is active
	public boolean isPassengerEnabled() throws IOException {
		boolean isEnabled = passengerSectionEnabled.getDomAttribute("class").contains("enabled")
				|| passengerSectionEnabled.isEnabled();
		return isEnabled;
	}
	
	// Log in later button is clicked
	public void selectLoginLaterBtn() throws IOException {
		logger.info("Clicking 'Login later' button");
		waitForElementClick(loginLaterBtn, Duration.ofSeconds(15));
		loginLaterBtn.click();
	}

	// Passenger title, first name and last name are inserted
	public void selectPassengerDetails(String firstName, String lastName) throws IOException {
		scrollIntoView(passengerTitleBtn);
		waitForElementClick(passengerTitleBtn, Duration.ofSeconds(15));
		passengerTitleBtn.click();
		getDriver().findElement(passengerTitleSelect).click();
		passengerFirstName.sendKeys(firstName);
		passengerLastName.sendKeys(lastName);

	}
	// Clicking continue button
	public void selectContinueBtn() throws IOException {
		logger.info("Clicking continue button");
		waitForElementClick(continueBtn, Duration.ofSeconds(15));
		continueBtn.click();
	}

}
