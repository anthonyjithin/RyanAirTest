package pageObjects;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.BasePage;


public class BagSelectionPage extends BasePage {

	//private WebDriver driver;
	private static final Logger logger = LogManager.getLogger(BagSelectionPage.class);
	
	public BagSelectionPage() throws IOException {
		super();
		//this.driver = getDriver();
		PageFactory.initElements(getDriver(), this);
		logger.info("BagSelectionPage initialized");

	}

	@FindBy(css = ".checkin-bag .card__header > span")
	private WebElement cabinBagText;

	// method to return element text
	public WebElement getCabinBagText() {
		return cabinBagText;
	}
}
