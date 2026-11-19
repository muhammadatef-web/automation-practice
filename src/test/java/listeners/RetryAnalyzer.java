package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1; // 1 retry = max 2 total attempts

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println(
                    "Retrying failed test '" + result.getName()
                    + "' (Attempt " + (retryCount + 1) + " of " + (MAX_RETRY_COUNT + 1) + ")"
            );
            return true;
        }
        return false;
    }
}