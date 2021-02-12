package model.entity.edit;

import model.base.EntityBaseEditPage;
import model.entity.table.VisibilityEventsPage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import runner.ProjectUtils;

public class VisibilityEventsEditPage extends EntityBaseEditPage<VisibilityEventsPage> {

    @FindBy(xpath = "//span[@class='toggle']")
    private WebElement toggle;

    @FindBy(id = "test_field")
    private WebElement inputTestField;

    public VisibilityEventsEditPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected VisibilityEventsPage createPage() {
        return new VisibilityEventsPage(getDriver());
    }

    public VisibilityEventsEditPage clickToggle() {
        toggle.click();
        return this;
    }

    public boolean isTestFieldVisible() {
        try {
            getWait().until(ExpectedConditions.visibilityOf(inputTestField));
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    public VisibilityEventsEditPage fillInputTestField(String text) {
        ProjectUtils.fill(getWait(), getWait().until(ExpectedConditions.visibilityOf(inputTestField)), text);
        return this;
    }
}
