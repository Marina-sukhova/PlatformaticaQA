package model.entity.table;

import model.base.EntityBaseTablePage;
import model.entity.edit.ArithmeticFunctionEditPage;
import model.entity.view.ArithmeticFunctionViewPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public final class ArithmeticFunctionPage extends EntityBaseTablePage<ArithmeticFunctionPage, ArithmeticFunctionEditPage, ArithmeticFunctionViewPage> {

    public ArithmeticFunctionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ArithmeticFunctionEditPage createEditPage() {
        return new ArithmeticFunctionEditPage(getDriver());
    }

    @Override
    public List<String> getRow(int rowNumber) {
        return getRows().get(rowNumber).findElements(By.tagName("td")).stream()
                .map(WebElement::getText).collect(Collectors.toList()).subList(1, 7);
    }

    @Override
    protected ArithmeticFunctionViewPage createViewPage() {
        return new ArithmeticFunctionViewPage(getDriver());
    }
}
