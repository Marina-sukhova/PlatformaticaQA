import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import org.testng.Assert;
import runner.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import runner.ProjectUtils;
import runner.type.Run;
import runner.type.RunType;

@Run(run = RunType.Single)

public class EntityDefaultTest extends BaseTest {

    private class FieldValues {
        String lineNumber;
        String fieldString;
        String fieldText;
        String fieldInt;
        String fieldDecimal;
        String fieldDate;
        String fieldDateTime;
        String fieldUser;

        private FieldValues(String lineNumber, String fieldString, String fieldText, String fieldInt, String fieldDecimal, String fieldDate,
                            String fieldDateTime, String fieldUser) {
            this.lineNumber = lineNumber;
            this.fieldString = fieldString;
            this.fieldText = fieldText;
            this.fieldInt = fieldInt;
            this.fieldDecimal = fieldDecimal;
            this.fieldDate = fieldDate;
            this.fieldDateTime = fieldDateTime;
            this.fieldUser = fieldUser;
        }
    }

    private final FieldValues defaultValues = new FieldValues(
            null,
            "DEFAULT STRING VALUE",
            "DEFAULT TEXT VALUE",
            "55",
            "110.32",
            "01/01/1970",
            "01/01/1970 00:00:00",
            "User 1 Demo");

    private final FieldValues defaultEmbeDValues = new FieldValues(
            "1",
            "Default String",
            "Default text",
            "77",
            "153.17",
            "",
            "",
            "Not selected");

    private final FieldValues changedDefaultValues = new FieldValues(
            null,
            "Changed default String",
            "Changed default Text",
             String.valueOf((int) (Math.random() * 100)),
             "33.33", //String.valueOf((int) (Math.random()*20000) / 100.0),
            "01/01/2021",
            "01/01/2021 12:34:56",
            "user115@tester.com");

    private final FieldValues changedEmbedDValues = new FieldValues(
            "1",
            "Changed EmbedD String",
            "Changed EmbedD Text",
            String.valueOf((int) (Math.random() * 100)),
            "55.55",   //String.valueOf((int) (Math.random()*20000) / 100.0),
            "12/12/2020",
            "12/12/2020 00:22:22",
            "User 4");

    private final FieldValues newValues = new FieldValues(
            null,
            UUID.randomUUID().toString(),
            "Some random text as Edited Text Value",
             String.valueOf((int) (Math.random() * 100)),
             "77.77",    //String.valueOf((int) (Math.random()*20000) / 100.0),
            "30/12/2020",
            "30/12/2020 12:34:56",
            "user100@tester.com");

    private final String[] NEW_VALUES = {null, newValues.fieldString, newValues.fieldText,
                  newValues.fieldInt, newValues.fieldDecimal,
                  newValues.fieldDate, newValues.fieldDateTime, null, null, newValues.fieldUser, null};

    private final String[] CHANGED_DEFAULT_VALUES = {changedDefaultValues.fieldString,
                   changedDefaultValues.fieldText, changedDefaultValues.fieldInt, changedDefaultValues.fieldDecimal,
                   changedDefaultValues.fieldDate, changedDefaultValues.fieldDateTime};

    private final String[] CHANGED_EMBEDD_VALUES = {changedEmbedDValues.lineNumber, changedEmbedDValues.fieldString,
                   changedEmbedDValues.fieldText, changedEmbedDValues.fieldInt, changedEmbedDValues.fieldDecimal,
                   changedEmbedDValues.fieldDate, changedEmbedDValues.fieldDateTime, null, null, changedEmbedDValues.fieldUser};

    private static final By BY_STRING = By.id("string");
    private static final By BY_TEXT = By.id("text");
    private static final By BY_INT = By.id("int");
    private static final By BY_DECIMAL = By.id("decimal");
    private static final By BY_DATE = By.id("date");
    private static final By BY_DATETIME = By.id("datetime");
    private static final By BY_USER = By.xpath("//div[@id='_field_container-user']/div/button");
    private static final By BY_EMBEDD_STRING = By.xpath("//td/textarea[@id='t-11-r-1-string']");
    private static final By BY_EMBEDD_TEXT = By.xpath("//td/textarea[@id='t-11-r-1-text']");
    private static final By BY_EMBEDD_INT = By.xpath("//td/textarea[@id='t-11-r-1-int']");
    private static final By BY_EMBEDD_DECIMAL = By.xpath("//td/textarea[@id='t-11-r-1-decimal']");
    private static final By BY_EMBEDD_DATE = By.id("t-11-r-1-date");
    private static final By BY_EMBEDD_DATETIME = By.id("t-11-r-1-datetime");
    private static final By BY_EMBEDD_USER = By.xpath("//select[@id='t-11-r-1-user']/option[@value='0']");
    private static final By BY_RECORD_HAMBURGER_MENU = By.xpath("//button[contains(@data-toggle, 'dropdown')] ");

    private void assertAndReplace(WebDriver driver, By by, String oldText, String newValue ) {
        WebElement element = driver.findElement(by);
        Assert.assertEquals(element.getAttribute("value"), oldText);
        element.clear();
        element.sendKeys(newValue);
        element.sendKeys("\t");
    }

    private void assertAndReplaceEmbedD (WebDriver driver, By by, String oldValue, String newValue) {
        WebElement element = driver.findElement(by);
        Assert.assertEquals(element.getText(), oldValue);
        element.click();
        element.clear();
        element.sendKeys(newValue);
        element.sendKeys("\t");
    }

    private void createDefaultRecord(WebDriver driver) {

        driver.findElement(By.xpath("//a[@href='#menu-list-parent']")).click();
        driver.findElement(By.xpath("//i/following-sibling::p[contains (text(), 'Default')]")).click();
        WebElement createFolder = driver.findElement(By.xpath("//i[.='create_new_folder']/ancestor::a"));
        ProjectUtils.click(driver,createFolder);

        WebElement saveBtn = driver.findElement(By.xpath("//button[.='Save']"));
        ProjectUtils.click(driver, saveBtn);
    }

    @Test
    public void checkDefaultValuesAndUpdate() throws InterruptedException {

        WebDriver driver = getDriver();

        driver.findElement(By.xpath("//p[contains (text(), 'Default')]")).click();

        WebElement createFolder = driver.findElement(By.xpath("//i[.='create_new_folder']/ancestor::a"));
        ProjectUtils.click(driver, createFolder);

        assertAndReplace(driver, BY_STRING, defaultValues.fieldString, changedDefaultValues.fieldString);
        assertAndReplace(driver, BY_TEXT, defaultValues.fieldText, changedDefaultValues.fieldText);
        assertAndReplace(driver, BY_INT, defaultValues.fieldInt, changedDefaultValues.fieldInt);
        assertAndReplace(driver, BY_DECIMAL, defaultValues.fieldDecimal, changedDefaultValues.fieldDecimal);
        assertAndReplace(driver, BY_DATE, defaultValues.fieldDate, changedDefaultValues.fieldDate);
        assertAndReplace(driver, BY_DATETIME, defaultValues.fieldDateTime, changedDefaultValues.fieldDateTime);

        WebElement fieldUser = driver.findElement(BY_USER);
        ProjectUtils.click(driver, fieldUser);
        WebElement scrollDownMenuField = driver.findElement(By.xpath("//span[text() = '" + changedDefaultValues.fieldUser + "']"));
        ProjectUtils.click(driver, scrollDownMenuField);

        WebElement greenPlus = driver.findElement(By.xpath("//button[@data-table_id='11']"));
        ProjectUtils.click(driver, greenPlus);

        WebElement lineNumber = driver.findElement(By.xpath("//input[@id='t-undefined-r-1-_line_number']"));
        Assert.assertEquals(lineNumber.getAttribute("data-row"), changedEmbedDValues.lineNumber);

        assertAndReplace(driver, BY_EMBEDD_STRING, defaultEmbeDValues.fieldString, changedEmbedDValues.fieldString);
        assertAndReplace(driver, BY_EMBEDD_TEXT, defaultEmbeDValues.fieldText, changedEmbedDValues.fieldText);
        assertAndReplace(driver, BY_EMBEDD_INT, defaultEmbeDValues.fieldInt, changedEmbedDValues.fieldInt);
        assertAndReplace(driver, BY_EMBEDD_DECIMAL, defaultEmbeDValues.fieldDecimal, changedEmbedDValues.fieldDecimal);
        assertAndReplaceEmbedD(driver, BY_EMBEDD_DATE, defaultEmbeDValues.fieldDate, changedEmbedDValues.fieldDate);
        assertAndReplaceEmbedD(driver, BY_EMBEDD_DATETIME, defaultEmbeDValues.fieldDateTime, changedEmbedDValues.fieldDateTime);

        WebElement embedDUser = driver.findElement(BY_EMBEDD_USER);
        Assert.assertEquals(embedDUser.getText(), defaultEmbeDValues.fieldUser);
        Select embedDUserSelect = new Select(driver.findElement(By.xpath("//select[@id='t-11-r-1-user']")));
        embedDUserSelect.selectByVisibleText(changedEmbedDValues.fieldUser);

        WebElement saveBtn = driver.findElement(By.xpath("//button[.='Save']"));
        ProjectUtils.click(driver, saveBtn);

        WebElement ourRecord = driver.findElement(By.xpath("//div[contains (text(), '" + changedDefaultValues.fieldString + "')]/ancestor::a"));
        ourRecord.click();

        List<WebElement> rows = driver.findElements(By.xpath("//span[@class='pa-view-field']"));
        Assert.assertEquals(rows.size(), CHANGED_DEFAULT_VALUES.length);
        for (int i =0; i < CHANGED_DEFAULT_VALUES.length; i++) {
            if (CHANGED_DEFAULT_VALUES[i] != null) {
                Assert.assertEquals(rows.get(i).getText(), CHANGED_DEFAULT_VALUES[i]);
            }
        }

        fieldUser = driver.findElement(By.xpath("//div[@class='form-group']/p"));
        Assert.assertEquals(fieldUser.getText(), changedDefaultValues.fieldUser);

        List<WebElement> embedDColumns = driver.findElements(By.xpath("//table/tbody/tr/td"));
        Assert.assertEquals(embedDColumns.size(), CHANGED_EMBEDD_VALUES.length);
        for (int i =0; i < CHANGED_EMBEDD_VALUES.length; i++) {
            if (CHANGED_EMBEDD_VALUES[i] != null) {
                Assert.assertEquals(embedDColumns.get(i).getText(), CHANGED_EMBEDD_VALUES[i]);
            }
        }
    }

    @Test
    public void deleteRecord() {

        WebDriver driver = getDriver();

        WebElement defaultBtn = driver.findElement(By.xpath("//p[contains(text(),' Default ')]"));
        ProjectUtils.click(driver,defaultBtn);

        WebElement newFolderBtn = driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]"));
        ProjectUtils.click(driver,newFolderBtn);

        WebElement newField = driver.findElement(BY_STRING);
        newField.clear();
        newField.sendKeys(changedDefaultValues.fieldString);

        WebElement saveBtn = driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']"));
        ProjectUtils.click(driver,saveBtn);

        WebElement firstColumn = driver.findElement(By.xpath("//*[@id='pa-all-entities-table']/tbody/tr/td[2]/a"));
        Assert.assertEquals(firstColumn.getText(),changedDefaultValues.fieldString);

        WebElement actionBtn = driver.findElement(BY_RECORD_HAMBURGER_MENU);
        actionBtn.click();

        WebElement deleteBtn = driver.findElement(By.xpath("//a[text() = 'delete']"));
        ProjectUtils.click(driver,deleteBtn);

        WebElement recycleBin = driver.findElement(By.xpath("//i[contains(text(),'delete_outline')]"));
        ProjectUtils.click(driver, recycleBin);

        WebElement deletedField = driver.findElement(By.xpath("//b[contains(text(),'" + changedDefaultValues.fieldString + "')]"));
        Assert.assertEquals(deletedField.getText(), changedDefaultValues.fieldString);

        WebElement deletePermanently = driver.findElement(By.xpath("//a[contains (text(), 'delete permanently')]"));
        deletePermanently.click();
    }

    @ Test
    public void editRecord() {

        WebDriver driver = getDriver();

        createDefaultRecord(driver);

        driver.findElement(By.xpath("//p[contains(text(), 'Default')]")).click();

        WebElement recordMenu = driver.findElement(BY_RECORD_HAMBURGER_MENU);
        recordMenu.click();

        WebElement editFunction = driver.findElement(By.xpath("//a[text() = 'edit']"));
        ProjectUtils.click(driver, editFunction);

        assertAndReplace(driver, BY_STRING, defaultValues.fieldString, newValues.fieldString);
        assertAndReplace(driver, BY_TEXT, defaultValues.fieldText, newValues.fieldText);
        assertAndReplace(driver, BY_INT, defaultValues.fieldInt, newValues.fieldInt);
        assertAndReplace(driver, BY_DECIMAL, defaultValues.fieldDecimal, newValues.fieldDecimal);
        assertAndReplace(driver, BY_DATE, defaultValues.fieldDate, newValues.fieldDate);
        assertAndReplace(driver, BY_DATETIME, defaultValues.fieldDateTime, newValues.fieldDateTime);

        WebElement fieldUser = driver.findElement(BY_USER);
        Assert.assertEquals(fieldUser.getText(), defaultValues.fieldUser.toUpperCase());
        ProjectUtils.click(driver, fieldUser);

        WebElement scrollDownMenuField = driver.findElement(By.xpath("//span[text() = '" + newValues.fieldUser + "']"));
        ProjectUtils.click(driver, scrollDownMenuField);

        WebElement saveButton = driver.findElement(By.xpath("//button[text() = 'Save']"));
        ProjectUtils.click(driver, saveButton);

        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='pa-all-entities-table']/tbody/tr"));
        Assert.assertEquals(rows.size(), 1);

        List<WebElement> columns = rows.get(0).findElements(By.tagName("td"));
        Assert.assertEquals(columns.size(), NEW_VALUES.length);
        for (int i =1; i < NEW_VALUES.length; i++) {
            if (NEW_VALUES[i] != null) {
                Assert.assertEquals(columns.get(i).getText(), NEW_VALUES[i]);
            }
        }
    }
}