package base;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import utils.Utility;



public class ExtentManager extends Utility {

	public static ExtentReports extentReport;
	public static String extentReportPrefix;
	public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	public ExtentManager() throws IOException {
		super();
	}

	// get the report instance
	public static ExtentReports getReport() {
		if (extentReport == null) {
			setupExtentReport("RyanAir_FlightSelectionTest");
		}
		return extentReport;
	}

	// setup and configure extentreports with sparkreporter
	public static ExtentReports setupExtentReport(String testName) {
		extentReport = new ExtentReports();
		String reportPath = System.getProperty("user.dir") + "/reports/" + extentReportsPrefix_Name(testName) + ".html";
        
        // Create directories explicitly
        File reportDir = new File("/automation/report/");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }
        
		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
		extentReport.attachReporter(spark);		
		
		 extentReport.setSystemInfo("Tester", "JAntony");
	        spark.config().setReportName("Regression Test");
	        spark.config().setDocumentTitle("Test Results");
	        spark.config().setTheme(Theme.DARK);

	        return extentReport;
		
	}

	// generate timestamp prefix
	public static String extentReportsPrefix_Name(String testName) {
		String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		extentReportPrefix = testName + "_" + date;
		return extentReportPrefix;
	}

	// write logs and results to the report
	public static void flushReport() {
		extentReport.flush();
	}

	// get current thread instance of extenttest, ensure each test logs its own test
	// section in report
	public synchronized static ExtentTest getTest() {
		return extentTest.get();
	}

	// create new extenttest instance and bind to current thread
	public synchronized static ExtentTest createTest(String name, String description) {
		ExtentTest test = extentReport.createTest(name, description);
		extentTest.set(test);
		return getTest();
	}

	// log info message
	public synchronized static void log(String message) {
		getTest().info(message);
	}

	// log a pass
	public synchronized static void pass(String message) {
		getTest().pass(message);
	}

	// log a fail
	public synchronized static void fail(String message) {
		getTest().fail(message);
	}

	// attach screenshot from the last screenshot capture path
	public synchronized static void attachImage() {
		getTest().addScreenCaptureFromPath(getScreenShotDestinationPath());

	}

}
