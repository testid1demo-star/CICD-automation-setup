import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class StandAloneTest1 {
    public static void main(String[] args) {
    	WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Implicit wait for all elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Explicit wait setup
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);

        // 1. Open homepage
        driver.get("https://sparklingcrown.24livehost.com/");

        // 2. Hover on login icon: div[class*="ur-user-links"]
        WebElement loginIcon = driver.findElement(By.cssSelector("div[class*='ur-user-links']"));
        actions.moveToElement(loginIcon).perform();

        // 3. Click Login link: //a[contains(text(),"Login")]
        driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();

        // 4. Select Customer radio button: //span[text()="Customer"]
        driver.findElement(By.xpath("//span[text()='Customer']")).click();

        // 5. Email & Password
        driver.findElement(By.cssSelector("input[placeholder='Email']")).sendKeys("testid1demo@gmail.com");
        driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Test@1234");

        try {
            Thread.sleep(2000); // Just to visually confirm the inputs before submission, can be removed in real tests
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 6. Submit Login: button[type="submit"]
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // 7. Wait for user name in header: p[title="testid1"]
        WebElement userName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p[title='testid1']")));

        // 8. Hover on user name
        actions.moveToElement(userName).perform();

        // 9. Click My Account: a[href*='my-account']
        driver.findElement(By.cssSelector("a[href*='my-account']")).click();

        // 10. Click My Profile: //a[normalize-space()='My Profile']
        driver.findElement(By.xpath("//a[normalize-space()='My Profile']")).click();

        // 11. Update fields
//        driver.findElement(By.id("FirstName")).clear();
//        driver.findElement(By.id("FirstName")).sendKeys("TestFN");

 //       driver.findElement(By.id("LastName")).clear();
 //       driver.findElement(By.id("LastName")).sendKeys("TestLN");

        WebElement genderDropdown = driver.findElement(By.id("Gender"));
        Select gender = new Select(genderDropdown);
        gender.selectByVisibleText("Female");

     // Click Update button
        driver.findElement(By.xpath("//div[contains(@class,' right_text')]//button[@type='submit']")).click();

        // Wait for success message to appear
        WebElement successAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[class*='alert-success']")));

        // Wait for it to disappear
        wait.until(ExpectedConditions.invisibilityOf(successAlert));

     // Re-fetch userName again to avoid stale reference
        userName = driver.findElement(By.cssSelector("p[title='testid1']"));

        // 13. Hover again to logout
        actions.moveToElement(userName).perform();

        // 14. Click Logout: //a[normalize-space()='Logout']
        driver.findElement(By.xpath("//a[normalize-space()='Logout']")).click();

        System.out.println("✅ Profile updated and user logged out.");
        driver.quit();
    }
}
