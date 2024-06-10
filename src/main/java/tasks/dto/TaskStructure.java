package tasks.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskStructure {
    public List<DrawObject> initObjects;
    public List<DrawObject> checkObjects;
    public HashMap<String, ArrayList<String>> instruments;
    public String description;
    public Integer number;

    @Override
    public String toString() {
        return "DataStructure{" +
                "initObjects=" + initObjects +
                ", instruments=" + instruments +
                ", description='" + description + '\'' +
                '}';
    }
}

