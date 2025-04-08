package utility;
import java.io.IOException;

import java.time.Duration;
import java.time.Month;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import base.BasePage;
import pageObjects.FlightSelectionPage;
import pageObjects.HomePage;
import pageObjects.SeatSelectionPage;
import utils.Utility;

public class TestUtility extends BasePage{
	
	private static final Logger logger = LogManager.getLogger(Utility.class);
	
	public TestUtility() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

		// scrolls a web element into middle of visible screen
		public static void scrollIntoView(WebElement element) throws IOException {
			JavascriptExecutor jse = (JavascriptExecutor) getDriver();
			jse.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

		}

		// wait for element to become visible
		public static void waitForElementVisible(WebElement element, Duration i) throws IOException {
			WebDriverWait wait = new WebDriverWait(getDriver(), i);
			wait.until(ExpectedConditions.visibilityOfElementLocated((By) element));

		}

		// waits for web element to become clickable
		public static void waitForElementClick(WebElement element, Duration i) throws IOException {
			WebDriverWait wait = new WebDriverWait(getDriver(), i);
			wait.until(ExpectedConditions.elementToBeClickable(element));

		}

		// waits for an alert to present
		public static void waitAlertPresence(By locator, Duration i) throws IOException {
			WebDriverWait wait = new WebDriverWait(getDriver(), i);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		}
		
		 public static void verifyTripSummary(String expDepartureDate, String expReturnDate, String expPassengerCount) throws IOException {
			 FlightSelectionPage flightPage = new FlightSelectionPage();
			 String sectionText = flightPage.getSection().getText().trim();
			 
			 String[] lines = sectionText.split("\n");
			 
			 String departureDate = lines[1].trim(); 
			 String returndate = lines[2].replace("-", "").trim().replaceAll("\\s+", " ");
			 String passengerCount = lines[3].trim();
			 
			 Assert.assertEquals(departureDate, expDepartureDate, "Departure date mismatch");
			 Assert.assertEquals(returndate, expReturnDate, "Arrival date mismatch");
			 Assert.assertEquals(passengerCount, expPassengerCount, "Passenger count mismatch");
			 
		 }

		// map to convert month names to Month enums from java time package
		static Month convertMonth(String month) {
			HashMap<String, Month> monthMap = new HashMap<String, Month>();

			monthMap.put("January", Month.JANUARY);
			monthMap.put("February", Month.FEBRUARY);
			monthMap.put("March", Month.MARCH);
			monthMap.put("April", Month.APRIL);
			monthMap.put("May", Month.MAY);
			monthMap.put("June", Month.JUNE);
			monthMap.put("July", Month.JULY);
			monthMap.put("August", Month.AUGUST);
			monthMap.put("September", Month.SEPTEMBER);
			monthMap.put("October", Month.OCTOBER);
			monthMap.put("November", Month.NOVEMBER);
			monthMap.put("December", Month.DECEMBER);

			Month vmonth = monthMap.get(month);
			if (vmonth == null) {
				System.out.println("Invalid Month");
			}
			return vmonth;

		}

		// This method accepts the required day and month as parameters and select the specified date from 
		// the calendar on the specified month
		public static void datePicker(String requiredDate, String requiredMonth) throws IOException, InterruptedException {
			HomePage home = new HomePage();

			// it identifies the displayed month which is loaded in the calendar using locator and compares it with the required month passed
			// as parameter, loop until the displayed month matches the required month
			while (true) {
				WebElement monthElement = home.getMonthDatePicker();
				String fullText = monthElement.getText();
				//splitting the text by space to extract the month value
				String displayMonth = fullText.split(" ")[0];

				// convert required month & display month into month objects using month enum from java time package
				Month expectedMonth = convertMonth(requiredMonth);
				Month currentMonth = convertMonth(displayMonth);

				// compare the month
				int result = expectedMonth.compareTo(currentMonth);

				// 0 months are equal
				// >0 then future month
				// <0 then past month, past month logic not applied here as its out of scope

				if (result > 0) {
					// future month
					home.goToNextMonth(); // click to navigate forward
					Thread.sleep(1000); 
				} else {
					break;// reached the desired month
				}
			}
			// after the desired month has been found the date needs to be selected, all the dates visible on the calendar is stored to
			// a List of webelements, using a for loop the text value of every month is passed to date string and this date is 
			// checked against the required date using if condition, once the required date is reached the loop breaks 
			List<WebElement> allDates = home.getAllDateSelect();
			int total_nodes = allDates.size();

			for (int i = 0; i < total_nodes; i++) {
				String date = allDates.get(i).getText();
				if (date.equals(requiredDate)) {
					allDates.get(i).click();
					break;
				}
			}
		}

		// This method finds all the available seats on the seat selection page, java util function random will then set
		// a random seat number to the random index and then the seat is selected
		public static void seatAllocator() throws IOException, InterruptedException {
			SeatSelectionPage sPage = new SeatSelectionPage();

			try {

				// All available seats found using a custom xpath locator are stored into a List of webelements 
				List<WebElement> availableSeats = sPage.getAllSeats();
				if (!availableSeats.isEmpty()) {
					// Generate a random index for the seats
					Random random = new Random();
					int randomIndex = random.nextInt(availableSeats.size());
					// select a random available seat
					WebElement seatToSelect = availableSeats.get(randomIndex);

					scrollIntoView(seatToSelect);
					waitForElementClick(seatToSelect, Duration.ofMillis(7000));

					seatToSelect.click();
					logger.info("Selected " + seatToSelect.getDomAttribute("id"));
					

				} else {
					logger.info("Seat selection failed");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Note: a further improvement can be added to the seat allocator method to select a seat from economy(included in fare). From the available seats which
		// are stored in the List a filter would then only select the seats from rows 18-33 which are the seats included in regular fare. Then a 
		// random index is used to select a seat from the economy seats

}
