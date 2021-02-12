package model.entity.table;

import model.base.EntityBaseTablePage;
import model.entity.edit.VisibilityEventsEditPage;
import model.entity.view.VisibilityEventsViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class VisibilityEventsPage extends EntityBaseTablePage<VisibilityEventsPage, VisibilityEventsEditPage, VisibilityEventsViewPage> {

    public VisibilityEventsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected VisibilityEventsEditPage createEditPage() {
        return new VisibilityEventsEditPage(getDriver());
    }

    @Override
    protected VisibilityEventsViewPage createViewPage() {
        return new VisibilityEventsViewPage(getDriver());
    }

    public String getRecordTriggerClass(int rowNumber) {
        try {
            return getRows().get(rowNumber).findElement(By.xpath("//td[2]/i")).getAttribute("class");
        } catch (NoSuchElementException ignored) {
            return "";
        }
    }

    @Override
    public List<String> getRow(int rowNumber) {
        return getRows().get(rowNumber).findElements(By.tagName("td")).stream()
                .map(WebElement::getText).collect(Collectors.toList()).subList(0, 3);
    }
}
