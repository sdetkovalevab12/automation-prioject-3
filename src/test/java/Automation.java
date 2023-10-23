import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;

public class Automation {
    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = null;
        try {
            driver=  new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.manage().window().maximize();

            driver.get("https://www.edmunds.com/");
            driver.findElement(By.linkText("Shop Used")).click();

            driver.findElement(By.cssSelector("input[id ^= 'zip-input']")).sendKeys(Keys.chord(Keys.CONTROL, "A", Keys.DELETE), "22031", Keys.ENTER);
            Thread.sleep(1000);
            WebElement checkBox = driver.findElement(By.xpath("//label[@data-tracking-value='deliveryType~local~Only show local listings']//span"));
            if(!checkBox.isSelected())
                checkBox.click();

            Select make = new Select(driver.findElement(By.cssSelector("#usurp-make-select")));
            make.selectByVisibleText("Tesla");
            Select model = new Select(driver.findElement(By.cssSelector("#usurp-model-select")));
            Assert.assertEquals( model.getFirstSelectedOption().getText(),"Add Model");

            Assert.assertEquals(driver.findElement(By.cssSelector("input[aria-label='Min Year value']")).getAttribute("value"), "2013");
            Assert.assertEquals(driver.findElement(By.cssSelector("#max-value-input-Year")).getAttribute("value"), "2023");

            List<String> expectedModelsList = SeleniumUtill.getTextOfElements(model.getOptions());
            List<String> expectedModelList = List.of("Add Model", "Model 3", "Model S", "Model X", "Model Y", "Cybertruck", "Roadster");
            Assert.assertEquals(expectedModelsList, expectedModelList);
            model.selectByValue("Model 3");
            Thread.sleep(1500);
            driver.findElement(By.xpath("//input[@id='min-value-input-Year']")).sendKeys(Keys.chord(Keys.CONTROL, "A"), Keys.BACK_SPACE,"2020",Keys.ENTER );
            Thread.sleep(2000);
            List<WebElement> searchResults = driver.findElements(By.xpath("//div[@class='size-16 font-weight-bold mb-0_5 text-blue-30']"));
            Assert.assertEquals(searchResults.size(), 21);

            List<Integer> years= new ArrayList<>();
            for( WebElement year : searchResults){
                years.add(Integer.parseInt(year.getText().substring(0,4)));
            }

            for(Integer year: years){
                Assert.assertTrue(year<=2023 && year>=2020);
            }

            Select  filter = new Select(driver.findElement(By.xpath("//select[@data-tracking-id='select_sort_by']")));
            filter.selectByVisibleText(("Price: Low to High"));
            Thread.sleep(1000);

            List<WebElement> sortedPrices = driver.findElements(By.xpath("//div[@class='pricing-details d-flex flex-column']//span[not(@class='font-weight-bold text-gray-darkest')]"));
            List<Integer> sorted$ =new ArrayList<>();
            for(WebElement price : sortedPrices ){
                sorted$.add(Integer.parseInt(price.getText().replace("$", "").replace(",","")));
            }
            List<Integer> copy = new ArrayList<>(sorted$);
            Collections.sort(copy);
            Assert.assertEquals(sorted$, copy);

//            for(int i=0; i < sorted$.size()-1; i++){
//                if(sorted$.get(i)> sorted$.get(i+1))
//                    throw new RuntimeException("Prices are not sorted in ascending order");
//            }

            filter.selectByValue("price:desc");
            Thread.sleep(500);
            List<WebElement>  sortedElementsDescendingOrder = driver.findElements(By.xpath("//div[@class='pricing-details d-flex flex-column']//span[@class='heading-3']"));
            List<Integer> sortedPriceDecendingOrder = new ArrayList<>();
            for(WebElement element: sortedElementsDescendingOrder){
                sortedPriceDecendingOrder.add(Integer.parseInt(element.getText().replace("$","").replace(",","")));
            }
            List<Integer> copy2 = new ArrayList<>(sortedPriceDecendingOrder);
            Collections.sort(copy2, Comparator.reverseOrder());
            Assert.assertEquals(sortedPriceDecendingOrder, copy2);

            filter.selectByVisibleText("Mileage: Low to High");
            Thread.sleep(500);

            List<WebElement> sortedMillage = driver.findElements(By.xpath("//div[@class='key-point size-14 d-flex align-items-baseline mt-0_5 col-12'][1]//span[@class='text-cool-gray-30']"));
            List<Integer> sortedMilageNumbers = new ArrayList<>();
            for(WebElement element: sortedMillage){
                sortedMilageNumbers.add(Integer.parseInt(element.getText().replace(",","").replace(" miles", "")));
            }
            List<Integer> copy3 = new ArrayList<>(sortedMilageNumbers);
            Collections.sort(copy3);
            Assert.assertEquals(sortedMilageNumbers, copy3);

            List<WebElement> latestSearchResults = driver.findElements(By.xpath("//div[@class='visible-vehicle-info d-flex flex-column']"));
            WebElement lastElement= latestSearchResults.get(latestSearchResults.size()-1);
            String title=lastElement.findElement(By.xpath("(//div[@class='size-16 font-weight-bold mb-0_5 text-blue-30'])["+latestSearchResults.size()+"]")).getText();
            //System.out.println("Title: "+title);

            String price=lastElement.findElement(By.xpath("(//div[@class='pricing-details d-flex flex-column']//div[not (@class='text-lowercase') and not( @class='size-12')])["+latestSearchResults.size()+"]")).getText();
           // System.out.println("Price: "+ price);

            String millage =lastElement.findElement(By.xpath("(//div[@class='key-point size-14 d-flex align-items-baseline mt-0_5 col-12'][1])["+latestSearchResults.size()+"]")).getText().replace(" miles", "");
            //System.out.println("Millage: "+ millage);

            lastElement.click();
            Thread.sleep(1000);
            Assert.assertEquals((driver.findElement(By.cssSelector("h1[class='d-inline-block mb-0 heading-2 mt-0_25']"))).getText(), title);
            Assert.assertEquals(driver.findElement(By.xpath("//span[@data-testid='vdp-price-row']")).getText(), price);
            Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='pr-0 font-weight-bold text-right ml-1 col'])[1]")).getText(), millage);

            driver.navigate().back();

            Assert.assertTrue(driver.findElement(By.xpath("(//div[@class='d-flex flex-column usurp-inventory-card h-100 w-100 srp-expanded srp-border border-0 bg-white'])["+latestSearchResults.size()+"]//div[@class='bg-white text-gray-darker']")).isDisplayed());



        }finally {
            Thread.sleep(1000);
            driver.quit();
        }

    }
}
