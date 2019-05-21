package Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueId {

    private static List<Integer> ids = new ArrayList<>();
    private static final int RANGE = 10000;

    private static int index = 0;

    static {
        for(int i = 0; i <RANGE; i++){
            ids.add(i);
        }
        Collections.shuffle(ids);
    }

    public static int getIdentifier(){
        if(index > ids.size()-1)index = 0;
        return ids.get(index++);
    }


}

