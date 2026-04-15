
package com.sparklingcrown.testall;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklingcrown.tests.base.BaseTest;
import com.sparklingcrown.utils.LoginTestData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LoginTestJson extends BaseTest {

    @Test(dataProvider = "loginJsonData")
    public void loginTest(LoginTestData data) {
        loginPage.login(data.email, data.password);

        if (data.expectedEmailError != null && !data.expectedEmailError.isEmpty()) {
            Assert.assertEquals(loginPage.getEmailError(), data.expectedEmailError, data.testCaseId + " - Email validation failed");
        }

        if (data.expectedPasswordError != null && !data.expectedPasswordError.isEmpty()) {
            Assert.assertEquals(loginPage.getPasswordError(), data.expectedPasswordError, data.testCaseId + " - Password validation failed");
        }

        if (data.expectedGeneralError != null && !data.expectedGeneralError.isEmpty()) {
            Assert.assertEquals(loginPage.getGeneralError(), data.expectedGeneralError, data.testCaseId + " - General error validation failed");
        }

        if (data.testCaseId.equals("TC08")) {
            // Optional success page assertion
        	Assert.assertEquals(driver.getCurrentUrl(),"https://sparklingcrown.24livehost.com/", "Login failed");
        }
    }

    @DataProvider(name = "loginJsonData")
    public Object[][] getLoginJsonData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream input = getClass().getClassLoader().getResourceAsStream("data/loginTestData.json");

        List<LoginTestData> dataList = mapper.readValue(input, new TypeReference<List<LoginTestData>>() {});
        Object[][] testData = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            testData[i][0] = dataList.get(i);
        }
        return testData;
    }
}
