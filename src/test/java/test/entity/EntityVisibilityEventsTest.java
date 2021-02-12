package test.entity;

import model.entity.common.MainPage;
import model.entity.edit.VisibilityEventsEditPage;
import model.entity.table.VisibilityEventsPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Run;
import runner.type.RunType;
import test.data.AppConstant;

import java.util.Arrays;

@Run(run = RunType.Multiple)
public class EntityVisibilityEventsTest extends BaseTest {

    @Test
    public void testFieldVisibility() {

        VisibilityEventsEditPage editPage = new MainPage(getDriver())
                .clickMenuVisibilityEvents()
                .clickNewFolder()
                .clickToggle();

        Assert.assertTrue(editPage.isTestFieldVisible());
    }

    @Test (dependsOnMethods = "testFieldVisibility")
    public void createEventTriggerONTest() {

        final String RECORD_NAME = ProjectUtils.createUUID();

        VisibilityEventsPage visibilityEventsPage = new MainPage(getDriver()).clickMenuVisibilityEvents();

        visibilityEventsPage
                .clickNewFolder()
                .clickToggle()
                .fillInputTestField(RECORD_NAME)
                .clickSaveButton();

        Assert.assertEquals(visibilityEventsPage.getRowCount(), 1);

        Assert.assertEquals(visibilityEventsPage.getRow(0), Arrays.asList("", "", RECORD_NAME));
        Assert.assertEquals(visibilityEventsPage.getRecordTriggerClass(0), AppConstant.RECORD_ICON_CLASS);
        Assert.assertEquals(visibilityEventsPage.getRowIconClass(0), AppConstant.RECORD_ICON_CLASS);
    }

    @Test(dependsOnMethods = "createEventTriggerONTest")
    public void editEventTest() {

        final String EDITED_RECORD_NAME = "Event 1";

        VisibilityEventsPage visibilityEventsPage = new MainPage(getDriver()).clickMenuVisibilityEvents();

        visibilityEventsPage
                .editRow(0)
                .fillInputTestField(EDITED_RECORD_NAME)
                .clickToggle()
                .clickSaveButton();

        Assert.assertEquals(visibilityEventsPage.getRowCount(), 1);

        Assert.assertEquals(visibilityEventsPage.getRow(0), Arrays.asList("", "", EDITED_RECORD_NAME));
        Assert.assertEquals(visibilityEventsPage.getRecordTriggerClass(0), "");
        Assert.assertEquals(visibilityEventsPage.getRowIconClass(0), AppConstant.RECORD_ICON_CLASS);
    }
}
