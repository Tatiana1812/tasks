package tasks;

import bodies.BodyType;
import builders.CircleCenterRadiusBuilder;
import editor.i_Body;
import geom.*;
import gui.EdtController;

import static builders.BodyBuilder.BLDKEY_CENTER;
import static builders.BodyBuilder.BLDKEY_RADIUS;

public class Checker {
    protected EdtController _ctrl;
    protected  double eps = 0.005;

    public Checker(EdtController ctrl) {
        _ctrl = ctrl;
    }

    public boolean check(Vect3d a, Vect3d b, Vect3d a1, Vect3d b1) {
        if (Vect3d.equalsWithTolerance(a, a1, eps) && Vect3d.equalsWithTolerance(b, b1, eps) ||
                Vect3d.equalsWithTolerance(a, b1, eps) && Vect3d.equalsWithTolerance(b, a1, eps)) {
            return true;
        }
        return false;
    }

    public boolean rib(Vect3d a, Vect3d b) {
        Rib3d rib;
        boolean flag = false;
        for (i_Body bd : _ctrl.getEditor().bd().getBodiesByType(BodyType.RIB)) {
            rib = (Rib3d) bd.getGeom();
            flag = check(a, b, rib.a(), rib.b());
            if (flag) break;
        }
        return flag;
    }

    public boolean ray(Vect3d a, Vect3d b) {
        Ray3d ray;
        boolean flag = false;
        for (i_Body bd : _ctrl.getEditor().bd().getBodiesByType(BodyType.RIB)) {
            ray = (Ray3d) bd.getGeom();
            flag = check(a, b, ray.pnt(), ray.pnt2());
            if (flag) break;
        }
        return flag;
    }

    public boolean point(Vect3d a) {
        Vect3d point;
        boolean flag = false;
        for (i_Body bd : _ctrl.getEditor().bd().getBodiesByType(BodyType.POINT)) {
            point = (Vect3d) bd.getGeom();
            flag = Vect3d.equalsWithTolerance(a, point, eps);
            if (flag) break;
        }
        return flag;
    }
    public boolean circle(Vect3d a,Double radius) {
        Circle3d circle;
        boolean flag = false;
        for (i_Body bd : _ctrl.getEditor().bd().getBodiesByType(BodyType.CIRCLE)) {
            circle = (Circle3d) bd.getGeom();
            flag = (Math.abs(circle.radiusLength()- radius) < eps) &&
                    Vect3d.equalsWithTolerance(a,circle.center(),eps);
            if (flag) break;
        }
        return flag;
    }
}
