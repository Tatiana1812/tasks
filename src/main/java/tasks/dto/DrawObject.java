package tasks.dto;

import java.util.ArrayList;

public class DrawObject {
    public String type;
    public ArrayList<Double> args;

    @Override
    public String toString() {
        return "InitObject{" +
                "type='" + type + '\'' +
                ", args=" + args +
                '}';
    }
}
