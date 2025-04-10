package pageObjects;

import static utils.Utility.*;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.BasePage;
import utility.TestUtility;

public class HomePage extends TestUtility {
	
	private static final Logger logger = LogManager.getLogger(HomePage.class);

	public HomePage() throws IOException {
		super();
		PageFactory.initElements(getDriver(), this); // Initialize all elements
		logger.info("Homepage initialized");

	}
	@FindBy(css = "._container.icon-inherit-width > svg")
	private WebElement logo;
	
	@FindBy(css = "input#input-button__departure")
	private WebElement departure;

	@FindBy(xpath = "//input[@id='input-button__destination']")
	private WebElement destination;

	@FindBy(css = ".datepicker__calendar.datepicker__calendar--left > calendar-body > div:nth-of-type(3) > div:nth-of-type(4) > .calendar-body__cell")
	private WebElement departDate;

	@FindBy(css = ".datepicker__calendar.datepicker__calendar--left > calendar-body > div:nth-of-type(5) > div:nth-of-type(16) > .calendar-body__cell.calendar-body__cell--weekend")
	private WebElement returnDate;

	@FindBy(css = ".passengers__confirm-button.ry-button--anchor.ry-button--anchor-blue")
	private WebElement passengersDone;

	@FindBy(css = "[class='flight-search-widget__start-search-cta ng-tns-c2866684587-3 ry-button--gradient-yellow'] [class]")
	private WebElement searchBtn;

	@FindBy(css = "[data-ref='cookie\\.accept-all']")
	private WebElement cookie;

	@FindBy(css = ".datepicker__calendar.datepicker__calendar--left > .calendar__month-name")
	private WebElement monthDatePicker;


	@FindBy(css = "ry-datepicker-desktop > div > div:nth-of-type(2)")
	private WebElement futureMonth;

	@FindBy(xpath = "//calendar[@class='datepicker__calendar datepicker__calendar--left']//calendar-body//div//div//div")
	private List<WebElement> allDateSelect;

	// getter methods
	
	public WebElement getMonthDatePicker() {return monthDatePicker;}
	public List<WebElement> getAllDateSelect() {return allDateSelect;}
	
	//Action methods
	
	// Method to load the title of website
	public String titleLoaded() {
		String title = getDriver().getTitle();
		return title;
	}
	
	//Method to select departure city
	public void selectDeparture(String city) throws IOException {
		waitForElementClick(departure, Duration.ofSeconds(15));
		departure.click();
		departure.clear();
		departure.sendKeys(city);
		logger.info("Departure selected");
	}
	//To select the destination
	public void selectDestination(String city) throws IOException {
		waitForElementClick(destination, Duration.ofSeconds(15));
		destination.click();
		destination.clear();
		destination.sendKeys(city);
		destination.sendKeys(Keys.RETURN);
		logger.info("Destination inserted");
	}
	//to accept the cookie
	public void acceptCookie() throws IOException {
		if(cookie.isDisplayed()) {
			waitForElementClick(cookie, Duration.ofSeconds(15));
			cookie.click();		
		}
	}
	//before selecting the depart date, driver wait for element to be ready for click
	public void selectDepartDate() throws IOException, InterruptedException {
		for (int i = 0; i < 2; i++) {
			try {
				waitForElementClick(departDate, Duration.ofSeconds(15));
				break;
			}catch(StaleElementReferenceException e) {
				logger.info("Stale element caught, retrying ");
			}
		}
	}
	//driver wait is given for the element to be ready for click
	public void selectReturnDate() throws IOException {
		waitForElementClick(returnDate, Duration.ofSeconds(15));
	}
	//driver wait is given for the element to be ready for click and the action is performed here
	public void selectPassenger() throws IOException {
		waitForElementClick(passengersDone, Duration.ofSeconds(15));
		passengersDone.click();
	}
	//clicks the search button to submit the search
	public void selectSearchBtn() throws IOException {	
		waitForElementClick(searchBtn, Duration.ofSeconds(15));
		searchBtn.click();
	}
	// This method is called when the datepicker method from TestUtility asks the driver to select the future month
	// and the action is performed here
	public void goToNextMonth() throws IOException {
		waitForElementClick(futureMonth, Duration.ofSeconds(15));
		futureMonth.click();
	}
	

	

}
