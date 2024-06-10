package tasks;


import com.fasterxml.jackson.databind.ObjectMapper;
import geom.Vect2d;
import geom.Vect3d;
import gui.EdtController;
import gui.action.EdtAction;
import gui.mode.ModeList;
import tasks.dto.DrawObject;
import tasks.dto.TaskStructure;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Problem {
    protected EdtController _ctrl;
    protected HashMap<String, ArrayList<EdtAction>> _panel;
    protected Draw _draw;
    protected List<DrawObject> _bodies;
    protected Checker _checker;
    protected int _number;

    public Problem(EdtController ctrl) {
        _ctrl = ctrl;
        _panel = new HashMap<>();
        _draw = new Draw(ctrl);
        _checker = new Checker(ctrl);
    }

    protected void initPanel(HashMap<String, ArrayList<String>> panel) {
        ArrayList<String> instruments = new ArrayList<>();
        Collections.addAll(instruments, "point", "line", "projection", "angle", "polygon", "circle",
                "curves", "arc", "section", "transform", "function");
        for (String instrument : instruments) {
            ArrayList<EdtAction> actionList = new ArrayList<>();
            if (panel.containsKey(instrument)) {
                for (String action : panel.get(instrument)) {
                    actionList.add(ModeList.valueOf(action.toUpperCase()).getAction(_ctrl));
                }
            }
            _panel.put(instrument, actionList);
        }
        _ctrl.getLayoutController().getTools().getScene2d().setScene2dToolBar(_panel);
    }

    protected void initObjects(List<DrawObject> drawObjects) {
        for (DrawObject drawObject : drawObjects) {
            switch (drawObject.type) {
                case "point":
                    _draw.point(drawObject.args.get(0), drawObject.args.get(1));
                    break;
                case "rib":
                    _draw.rib(
                            new Vect2d(drawObject.args.get(0), drawObject.args.get(1)),
                            new Vect2d(drawObject.args.get(2), drawObject.args.get(3))
                    );
                    break;
                case "ray":
                    _draw.ray(
                            new Vect2d(drawObject.args.get(0), drawObject.args.get(1)),
                            new Vect2d(drawObject.args.get(2), drawObject.args.get(3))
                    );
                    break;
                case "rectangle":
                    _draw.rectangle(
                            new Vect2d(drawObject.args.get(0), drawObject.args.get(1)),
                            new Vect2d(drawObject.args.get(2), drawObject.args.get(3))
                    );
                    break;
                case "circle":
                    _draw.circle(
                            new Vect2d(drawObject.args.get(0), drawObject.args.get(1)),
                            drawObject.args.get(2)
                    );
                    break;
                case "angel":
                    _draw.angel(
                            new Vect2d(drawObject.args.get(0), drawObject.args.get(1)),
                            new Vect2d(drawObject.args.get(2), drawObject.args.get(3)),
                            new Vect2d(drawObject.args.get(4), drawObject.args.get(5))
                    );
                    break;
                default:
                    break;
            }

        }
    }

    protected void initDescription(Integer number, String description) {
        _ctrl.getLayoutController().getInspector().updateTaskInfo(
                number,
                description
        );
    }

    public void fromFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Чтение JSON файла и десериализация в объект
            TaskStructure data = mapper.readValue(new File(fileName), TaskStructure.class);
            // Печать объекта для проверки
            initDescription(_number, data.description);
            initPanel(data.instruments);
            initObjects(data.initObjects);
            _bodies = data.checkObjects;
            System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int i) {
        _ctrl.getMainCanvasCtrl().setDefaultMode();
        _number = i;
        fromFile("TaskCondition\\Problem" + i + ".json");
    }

    public boolean check() {
        boolean success = true;
        for (DrawObject body : _bodies) {
            if (!success) break;
            switch (body.type) {
                case "point":
                    success = _checker.point(
                            new Vect3d(body.args.get(0), body.args.get(1), 0)
                    );
                    break;
                case "rib":
                    success = _checker.rib(
                            new Vect3d(body.args.get(0), body.args.get(1), 0),
                            new Vect3d(body.args.get(2), body.args.get(3), 0)
                    );
                    break;
                case "ray":
                    success = _checker.ray(
                            new Vect3d(body.args.get(0), body.args.get(1), 0),
                            new Vect3d(body.args.get(2), body.args.get(3), 0)
                    );
                    break;
                case "rectangle":

                    break;
                case "circle":
                    success = _checker.circle(
                            new Vect3d(body.args.get(0), body.args.get(1), 0),
                            body.args.get(2)
                    );
                    break;
                case "angel":

                    break;
                default:
                    break;
            }

        }
        return success;
    }
}