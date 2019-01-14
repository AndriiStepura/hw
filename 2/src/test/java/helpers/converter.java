package helpers;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class converter {
    public static List<String> getTitles(List<WebElement> listOfEleents){
        List<String> stringsList = new ArrayList<String>();
        for(WebElement e : listOfEleents){
            stringsList.add(e.getText());
        }
        return stringsList;
    }
}

