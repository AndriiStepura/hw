package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class isSorted {
    public static boolean isAlphabeticallySorted(List<String> stringListForVerification){
        List<String> alphabeticallySortedList = new ArrayList<>(stringListForVerification);
        Collections.sort(alphabeticallySortedList);
        return stringListForVerification.equals(alphabeticallySortedList);
    }
}
