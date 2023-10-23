import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class SeleniumUtill {

//    public static void jsExecutor(WebDriver driver, WebElement element) {
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//    }

    public static List<String> getTextOfElements(List<WebElement> list) {
        List<String> result = new ArrayList<>();
        for (WebElement element : list) {
            result.add(element.getText());
        }
        return result;
    }

}
