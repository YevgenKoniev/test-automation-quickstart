package uk.co.opencredo.ui.acceptance.test.interaction.library;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import uk.co.opencredo.ui.acceptance.test.utils.WebPageUtils;

/**
 * Common page methods
 */
public abstract class AbstractPageObject {
    private String path;
    private final WebDriver driver;
    private final int waitTimeOutSeconds;

    public AbstractPageObject(String path, WebDriver driver, int waitTimeOutSeconds) {
        this.path = path;
        this.driver = driver;
        this.waitTimeOutSeconds = waitTimeOutSeconds;
    }

    public void deleteAllCookies() {
        getDriver().manage().deleteAllCookies();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void goTo() {
        getDriver().navigate().to(path);
    }

    /**
     * Go to page and wait until url reflects
     * expected page (or timeout reached)
     */
    public void goToAndWait() {
        goTo();
        ensure_is_current();
    }

    public void ensure_is_current() {
        wait_until_true_or_timeout(WebPageUtils.urlContains(path));
    }

    public boolean is_text_present(String text) {
        wait_until_true_or_timeout(WebPageUtils.pageContainsText(text));
        return true;
    }

    /**
     * wait until condition is true or timeout kicks in
     */
    protected <V> V wait_until_true_or_timeout(ExpectedCondition<V> isTrue) {
        Wait<WebDriver> wait = new WebDriverWait(this.driver, waitTimeOutSeconds)
                .ignoring(StaleElementReferenceException.class);
        try {
            return wait.until(isTrue);
        } catch (TimeoutException rte) {
            throw new TimeoutException(rte.getMessage() + "\n\nPageSource:\n\n" + getDriver().getPageSource());
        }
    }

    public void setText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public void submit(WebElement element) {
        element.submit();
    }

    public void selectDropdownByText(WebElement element, String visibleText){
        Select filterSelect = new Select(element);
        waitForDropdownItems(element);
        filterSelect.selectByVisibleText(visibleText);
    }

    private void waitForDropdownItems(WebElement element) {
        WebDriverWait wait = new WebDriverWait(getDriver(),waitTimeOutSeconds );
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected WebElement find(By locator) {
        try {
            return getDriver().findElement(locator);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(ex.getMessage() + "\n\nPageSource:\n\n" + getDriver().getPageSource());
        }
    }
}
