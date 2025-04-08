package resources;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BasePage;
import base.ExtentManager;
import utils.Utility;

public class Listeners extends Utility implements ITestListener {

	public Listeners() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	//callback when a test tag starts executing in testNG file
	public synchronized void onStart(ITestContext context) {
		ExtentManager.getReport();
		ExtentManager.createTest(context.getName(), context.getName());
	}
	//automatically triggered when test execution fails
	public synchronized void onTestFailure(ITestResult result) {
		ExtentManager.getTest().fail(result.getThrowable());
		try {
			System.out.println("Test failed: " + result.getName());
			takeSnapShot(result.getMethod().getMethodName());
			ExtentManager.attachImage();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	// for flushing reports so they get saved, triggers after all
	// the test tag have finished executing
	public synchronized void onFinish(ITestContext context) {
		ExtentManager.flushReport();
	}

}
