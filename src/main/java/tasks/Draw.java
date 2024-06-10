package tasks;

import bodies.BodyType;
import builders.*;
import builders.param.BuilderParam;
import editor.ExNoAnchor;
import editor.ExNoBody;
import editor.i_BodyBuilder;
import geom.*;
import gui.EdtController;
import gui.mode.CreateBodyMode;
import gui.mode.CreateRibMode;
import minjson.JsonObject;


import java.util.HashMap;

import static bodies.PointBody.BODY_KEY_POINT;
import static builders.BodyBuilder.*;

public class Draw {
    protected EdtController _ctrl;
    private int count = 0;

    public Draw(EdtController ctrl) {
        _ctrl = ctrl;
    }

    public PointBuilder point(double x, double y) {
        i_BodyBuilder pt = AllBuildersManager.create(PointBuilder.ALIAS,BLDKEY_P + count, new Vect3d(x, y, 0));
        count++;
        _ctrl.addSilently(pt);
        return (PointBuilder) pt;
    }

    public RibBuilder rib(Vect2d a, Vect2d b) {
        RibBuilder rb = new RibBuilder();
        try {
            String rbA = _ctrl.getAnchorID(point(a.x(), a.y()).id(), BODY_KEY_POINT);
            String rbB = _ctrl.getAnchorID(point(b.x(), b.y()).id(), BODY_KEY_POINT);
            rb.addA(rbA);
            rb.addB(rbB);
        } catch (ExNoAnchor | ExNoBody e) {
            e.printStackTrace();
        }
        _ctrl.addSilently(rb);
        return rb;
    }

    public RayTwoPointsBuilder ray(Vect2d a, Vect2d b) {
        RayTwoPointsBuilder ray = new RayTwoPointsBuilder();
        try {
            String rbA = _ctrl.getAnchorID(point(a.x(), a.y()).id(), BODY_KEY_POINT);
            String rbB = _ctrl.getAnchorID(point(b.x(), b.y()).id(), BODY_KEY_POINT);
            ray.addA(rbA);
            ray.addB(rbB);
        } catch (ExNoAnchor | ExNoBody e) {
            e.printStackTrace();
        }
        _ctrl.addSilently(ray);
        return ray;
    }

    public RectangleBuilder rectangle(Vect2d a, Vect2d b) {
        RectangleBuilder rectangle = new RectangleBuilder();
        try {
            String rbA1 = _ctrl.getAnchorID(point(a.x(), a.y()).id(), BODY_KEY_POINT);
            String rbB1 = _ctrl.getAnchorID(point(b.x(), b.y()).id(), BODY_KEY_POINT);
            rectangle.addA(rbA1);
            rectangle.addB(rbB1);
            Vect2d c = b.duplicate();
            c.set_y(-b.y());
            rectangle.addHeight(Vect2d.dist(b, c));
            rectangle.addRotationAngle(0);
        } catch (ExNoAnchor | ExNoBody e) {
            e.printStackTrace();
        }
        _ctrl.addSilently(rectangle);
        return rectangle;
    }
    public CircleCenterRadiusBuilder circle(Vect2d a, Double radius) {
        CircleCenterRadiusBuilder circle = new CircleCenterRadiusBuilder();
        try {
            String rbA = _ctrl.getAnchorID(point(a.x(),a.y()).id(), BODY_KEY_POINT);
            circle.addNormal();
            circle.addCenter(rbA);
            circle.addRadius(radius);
        } catch (ExNoAnchor | ExNoBody e) {
            e.printStackTrace();
        }
        _ctrl.addSilently(circle);
        return circle;
    }

    public AngleBetweenRibsBuilder angel(Vect2d a, Vect2d b, Vect2d c) {
        AngleBetweenRibsBuilder angel = new AngleBetweenRibsBuilder();
        angel.addRib1(rib(b, a).id());
        angel.addRib2(rib(b, c).id());
        angel.addDirection1(true);
        angel.addDirection2(true);
        _ctrl.addSilently(angel);
        return angel;
    }
}
