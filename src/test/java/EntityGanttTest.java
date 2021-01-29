import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.TestUtils;
import runner.type.Run;
import runner.type.RunType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Run(run = RunType.Multiple)
public class EntityGanttTest extends BaseTest {

    private static final By ACTIONS_BUTTON = By.xpath("//tr[@data-index='0']/td/div/button");
    private static final By GANTT = By.xpath("//p[contains(text(),'Gantt')]");
    private static final By DRAFT_BUTTON = By.xpath("//button[@id='pa-entity-form-draft-btn']");
    private static final By SAVE_BUTTON = By.xpath("//button[@id='pa-entity-form-save-btn']");
    private static final By CANCEL_BUTTON = By.xpath("//button[contains(text(),'Cancel')]");
    private static final By TABLE = By.xpath("//div[contains(@class,'card-body')]");
    private String[] record_values = new String[6];

    private void createGanttChart(WebDriver driver, String mode) {
        if (mode.equals("cancel")) {
            ProjectUtils.click(driver, driver.findElement(CANCEL_BUTTON));
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String time = dateFormat.format(new Date());

            record_values = new String[]
                    {ProjectUtils.createUUID(), ProjectUtils.createUUID(), "15", "25.75", "", "", "apptest1@tester.com"};

            List<WebElement> record_fields = driver.findElements
                    (By.xpath("//div[@class='card-body']//input[@type='text']|//textarea[@id='text']"));
            for (int i = 0; i < record_values.length - 1; i++) {
                if (record_fields.get(i).toString().toLowerCase().contains("date")) {
                    ProjectUtils.click(getWebDriverWait(), record_fields.get(i));
                } else {
                    if (!record_fields.get(i).getAttribute("value").isEmpty()) {
                        record_fields.get(i).clear();
                    }
                    record_fields.get(i).sendKeys(record_values[i]);
                }
            }

            driver.findElement(By.xpath("//div[@class='filter-option-inner-inner']")).click();
            ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//span[contains(text(),'apptest1@tester.com')]"))));

            if (mode.equals("draft")) {
                ProjectUtils.click(driver, driver.findElement(DRAFT_BUTTON));
            } else if (mode.equals("save")) {
                ProjectUtils.click(driver, driver.findElement(SAVE_BUTTON));
            }
            record_values[4] = time;
            record_values[5] = time;
        }
    }

    private void actionsClick(WebDriver driver, String mode) {
        ProjectUtils.click(driver, driver.findElement(ACTIONS_BUTTON));
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath(String.format("//a[contains(text(),'%s')]", mode)))));
    }

    private void assertRecordView(WebDriver driver) {
        actionsClick(driver, "view");
        List<WebElement> actual_record = driver.findElements
                (By.xpath("//span[@class='pa-view-field']|//label[text()='User']/../p"));
        for (int i = 0; i < record_values.length; i++) {
            if (i == 5) {
                Assert.assertEquals(actual_record.get(i).getText().substring(0, 10), record_values[i]);
            } else {
                Assert.assertEquals(actual_record.get(i).getText(), record_values[i]);
            }
        }
    }

    private void resetAccount(WebDriver driver) {
        ProjectUtils.click(driver, driver.findElement(By.id("navbarDropdownProfile")));
        ProjectUtils.click(driver, driver.findElement(By.xpath("//a[contains(text(), 'Reset')]")));
    }

    @Test
    public void cancelGanttTest() {
        WebDriver driver = getDriver();
        ProjectUtils.click(driver, driver.findElement(GANTT));
        driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]")).click();
        createGanttChart(driver, "cancel");

        WebElement record_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//td[@colspan='3']")));
        Assert.assertEquals(record_table.getText(), "No records to display");
    }

    @Test (dependsOnMethods = "cancelGanttTest")
    public void draftGanttTest() {
        WebDriver driver = getDriver();
        ProjectUtils.click(driver, driver.findElement(GANTT));
        driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]")).click();
        createGanttChart(driver, "draft");

        WebElement string_table_element = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//td[@class='e-rowcell e-ellipsistooltip e-lastrowcell'][1]")));
        Assert.assertEquals(string_table_element.getText(), record_values[0]);
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[@class='d-flex justify-content-between']//li[2]"))).click();
        Assert.assertEquals(driver.findElement(By.xpath("//tr/td/i")).getAttribute("class"), AppConstant.DRAFT_ICON_CLASS);
        resetAccount(driver);
    }

    @Test (dependsOnMethods = "draftGanttTest")
    public void createGanttTest() {
        WebDriver driver = getDriver();
        ProjectUtils.click(driver, driver.findElement(GANTT));
        driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]")).click();
        createGanttChart(driver, "save");

        WebElement string_field = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//td[@class='e-rowcell e-ellipsistooltip e-lastrowcell'][1]")));
        Assert.assertEquals(string_field.getText(), record_values[0]);

        WebElement date_field = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//td[@class='e-rowcell e-ellipsistooltip e-lastrowcell'][2]")));
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        String date = dateFormat.format(new Date());
        Assert.assertEquals(date_field.getText(), date);

        WebElement hoverElement = driver.findElement
                (By.xpath("//div[contains(@class,'e-gantt-child-taskbar-inner-div e-gantt-child-taskbar')]"));
        Assert.assertNotEquals(hoverElement.getAttribute("data-tooltip-id"), "GanttContainer_0_content");
        Actions builder = new Actions(driver);
        builder.moveToElement(hoverElement).perform();
        Assert.assertEquals(hoverElement.getAttribute("data-tooltip-id"), "GanttContainer_0_content");
    }

    @Test (dependsOnMethods = "createGanttTest")
    public void viewGanttTest() {
        WebDriver driver = getDriver();
        ProjectUtils.click(driver, driver.findElement(GANTT));

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[@class='d-flex justify-content-between']//li[2]"))).click();
        Assert.assertEquals(driver.findElement(By.xpath("//tr/td/i")).getAttribute("class"), AppConstant.RECORD_ICON_CLASS);
        assertRecordView(driver);
    }

    @Test (dependsOnMethods = "viewGanttTest")
    public void editGanttTest() {
        WebDriver driver = getDriver();
        ProjectUtils.click(driver, driver.findElement(GANTT));

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[@class='d-flex justify-content-between']//li[2]"))).click();
        actionsClick(driver, "edit");
        createGanttChart(driver, "save");
        assertRecordView(driver);
    }

    @Test(dependsOnMethods = "editGanttTest")
    public void deleteGanttTest() {
        WebDriver driver = getDriver();
        ProjectUtils.click(driver, driver.findElement(GANTT));

        driver.findElement(By.xpath("//a[@class='nav-link ']/i")).click();
        driver.findElement(By.xpath("//button/i[@class='material-icons']")).click();
        getWebDriverWait().until(TestUtils.movingIsFinished(By.xpath("//a[text()='delete']"))).click();
        Assert.assertTrue(driver.findElement(TABLE).getText().isEmpty());

        driver.findElement(By.xpath("//i[text() = 'delete_outline']/..")).click();
        Assert.assertEquals(driver.findElements(By.xpath("//tbody/tr")).size(), 1);
    }
}