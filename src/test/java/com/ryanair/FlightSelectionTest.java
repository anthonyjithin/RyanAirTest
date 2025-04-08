package com.ryanair;


import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.ExtentManager;

import pageObjects.BagSelectionPage;
import pageObjects.FlightSelectionPage;
import pageObjects.HomePage;
import pageObjects.SeatSelectionPage;
import utility.TestUtility;


@Listeners(resources.Listeners.class)

public class FlightSelectionTest extends TestUtility {
	private static final Logger logger = LogManager.getLogger(FlightSelectionTest.class);

	public FlightSelectionTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Test
	public void searchFlightTest() throws IOException, InterruptedException {
		logger.info("====== Starting Flight Selection Test ======");
		ExtentManager.log("Starting flight search test");
		HomePage home = new HomePage();

		// handle cookie
		logger.info("Accepting cookie popup");
		home.acceptCookie();
		
		//Assertion to check whether https://www.ryanair.com is loaded successfully		
		Assert.assertTrue(home.titleLoaded().contains("Ryanair"), "Page title does not contain 'Ryanair'");
		logger.info("RyanAir Website loaded successfully");

		// Select the departure flight
		// Action method is called
		logger.info("Selecting departure airport");
		home.selectDeparture("Dublin");
		ExtentManager.pass("Departure selected successfully");

		// Select destination, Naples in this case
		logger.info("Selecting destination airport");
		home.selectDestination("Naples");
		ExtentManager.pass("Destination selected successfully");

		// To select the depart date
		// datepicker a utility method is called
		logger.info("Selecting departure date");
		home.selectDepartDate();
		datePicker("10", "September");
		ExtentManager.pass("Departure date selected");

		// selecting return date
		// datepicker method is called to select the date
		logger.info("Selecting return date");
		home.selectReturnDate();
		datePicker("14", "September");
		ExtentManager.pass("Return date selected");

		// close the passenger pop-up to 
		// select the number of passengers
		logger.info("Selecting passengers");
		home.selectPassenger();

		// submit search button
		logger.info("Clicking search button");
		home.selectSearchBtn();
		ExtentManager.pass("Search button clicked successfully");
		ExtentManager.log("searchFlightTest completed");
		logger.info("searchFlightTest completed");

	}

	@Test(dependsOnMethods = { "searchFlightTest" })
	public void selectFlightTest() throws IOException, InterruptedException {

		logger.info("====== Starting selectFlightTest ======");
		ExtentManager.log("Starting flight selection test");
		FlightSelectionPage flightPage = new FlightSelectionPage();
		
		// Assertion to validate the details on trip summary, the departure date, arrival date
		// and the number of passengers are passed to method verifyTripSummary which is from TesUtility class
		verifyTripSummary("10 Sept", "14 Sept", "1");
		logger.info("Trip details validated successfully");
		
		
		//Assertion to check whether DUB-NAP flight has been selected	
		Assert.assertTrue(flightPage.getDepartureFlightText().contains("Dublin to Naples"), "Dublin to Naples flight has not been selected");
		logger.info("Dublin to Naples flight has been selected successfully");
		
		// select departure flight
		logger.info("Selecting departure flight");
		flightPage.selectDepartureFlight();
		ExtentManager.pass("Departure flight selection completed");

		//Assertion to check whether NAP-DUB flight has been selected	
		Assert.assertTrue(flightPage.getArrivalFlightText().contains("Naples to Dublin"), "Naples to Dublin flight has not been selected");
		logger.info("Naples to Dublin flight has been selected successfully");	
		
		// Select arrival flight
		logger.info("Selecting arrival flight");
		flightPage.selectArrivalFlight();
		ExtentManager.pass("Arrival flight selection completed");

		// select regular option
		logger.info("Selecting regular flight option");
		flightPage.selectRegularFlight();
		ExtentManager.pass("Regular selection completed");

		// Assertion to check Log in to MyRyanAir is visible
		logger.info("Verifying login text visibility");
		try {
			Assert.assertEquals(flightPage.getLoginToMyRyanairText().getText(), "Log in to myRyanair");
			logger.info("Assertion passed: Login text is visible");
			ExtentManager.pass("Log in text is visible");
		} catch (AssertionError e) {
			Assert.fail("Failed to locate text");
			logger.error("Assertion failed: Login text is not visible", e);
			ExtentManager.fail("Log in to MyRyanair text is not visible");
		}

		// Assertion to check if the passenger section is disabled
		logger.info("Verifying passenger section is initially disabled");
		Assert.assertTrue(flightPage.isPassengerDisabled(), "Passenger section is disabled");
		logger.info("Assertion passed: Passenger section is disabled");

		// Log in later button is clicked
		logger.info("Clicking 'Log in later' button");
		flightPage.selectLoginLaterBtn();
		ExtentManager.pass("Suuceessfully clicked on log in later button");

		// Assertion to check if the passenger section is enabled
		// boolean condition return the value  
		logger.info("Verifying passenger section is now enabled");
		Assert.assertTrue(flightPage.isPassengerEnabled(), "Passenger section is enabled");
		logger.info("Assertion passed: Passenger section is enabled after clicking button");
		ExtentManager.pass("Passenger section is enabled");

		// selecting passenger title
		logger.info("Entering passenger details");
		flightPage.selectPassengerDetails("Jarlath", "O'Brien");
		ExtentManager.pass("Passenger title is selected and name is entered successfully");

		// clicking continue button
		// once after all the details are entered the button is clicked
		logger.info("Clicking continue button");
		flightPage.selectContinueBtn();
		ExtentManager.pass("Continue button clicked successfuly");
		logger.info("selectFlightTest completed");
		ExtentManager.log("selectFlightTest completed");

	}

	@Test(dependsOnMethods = { "selectFlightTest" })
	public void selectSeatTest() throws IOException, InterruptedException {

		logger.info("====== Starting selectSeatTest ======");
		ExtentManager.log("Starting selectSeatTest test");
		SeatSelectionPage seatPage = new SeatSelectionPage();

		// Assertion to check seat selection for the first flight is loaded, it checks against the active text from class attribute
		logger.info("Asserting departure flight seat selection is active");
		Assert.assertTrue(seatPage.isDepartSeatSelectionLoaded()
				.contains("passenger-carousel__orig-dest subtitle-s-lg passenger-carousel__orig-dest--active"));
		logger.info("Assertion passed: Departure flight DUB - NAP is active");

		// method to assign a departure seat, seatAllocator method from TestUtility is called which then 
		// assigns a random seat number 
		logger.info("Selecting departure seat");
		seatAllocator();
		logger.info("Seat selected successfully for departure flight");
		ExtentManager.pass("Departure seat selected");

		// Next flight button is clicked after selecting the departure flight seat
		logger.info("Clicking next flight button");
		seatPage.nextFlightSelection();
		ExtentManager.pass("Next Flight button clicked");

		// to close prompt
		logger.info("Closing 'No Thanks' prompt");
		seatPage.closeNoThanksPrompt();

		// Assertion to check seat selection for the second flight is loaded, which uses the class attribute which only shows in case of 
		// the element becomes active
		logger.info("Asserting arrival flight seat selection is active");
		Assert.assertTrue(seatPage.isArrivalSeatSelectionLoaded()
				.contains("passenger-carousel__orig-dest subtitle-s-lg passenger-carousel__orig-dest--active"));
		logger.info("Assertion passed: Arrival flight NAP - DUB is active");

		// seatAllocator method is called to select the seat from return flight, a random seat is assigned
		logger.info("Selecting arrival seat");
		seatAllocator();
		logger.info("Seat selected successfully for arrival flight");
		ExtentManager.pass("Arrival seat selected");

		// clicking continue button
		logger.info("Clicking continue button after seat selection");
		seatPage.selectContinueToSeat();
		ExtentManager.pass("Continue button clicked successfully");

		// handling fast track pop-up which is a premium service
		logger.info("Closing fast track prompt");
		seatPage.closeFastTrackPrompt();
		logger.info("selectSeatTest completed");
		ExtentManager.log("selectSeatTest completed");
	}

	@Test(dependsOnMethods = { "selectSeatTest" })
	public void bagSelectionTest() throws IOException, InterruptedException {
		logger.info("====== Starting bagSelectionTest ======");
		ExtentManager.log("Starting bagSelection test");
		BagSelectionPage bagPage = new BagSelectionPage();

		// Assertion to check if the check in bag page is loaded
		logger.info("Asserting bag selection page is loaded");
		try {
			Assert.assertEquals(bagPage.getCabinBagText().getText(), "Need to check in any bags?");
			logger.info("Assertion passed: Bag selection page loaded");
			ExtentManager.pass("Bag selection page is loaded");
		} catch (AssertionError e) {
			logger.error("Assertion failed: Bag selection page did not load", e);
			Assert.fail("Failed to load page");
			ExtentManager.fail("failed to load page");
		}
		logger.info("bagSelectionTest completed");
		ExtentManager.log("bagSelectionTest completed");

	}

}
