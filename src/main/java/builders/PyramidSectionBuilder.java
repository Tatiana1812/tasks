package builders;

import bodies.PlaneBody;
import bodies.PointBody;
import bodies.PolygonBody;
import bodies.PyramidBody;
import bodies.RibBody;
import builders.param.BuilderParam;
import builders.param.BuilderParamType;
import editor.Editor;
import editor.ExBadRef;
import editor.ExLostBody;
import editor.ExNoBody;
import editor.i_Body;
import editor.i_BodyContainer;
import error.i_ErrorHandler;
import geom.ExGeom;
import geom.ExZeroDivision;
import geom.Polygon3d;
import geom.Rib3d;
import gui.EdtController;
import java.util.HashMap;
import minjson.JsonObject;

/**
 * Constructs a section of pyramid
 *
 * @author alexeev
 */
public class PyramidSectionBuilder extends BodyBuilder {
  static public final String ALIAS = "PyramidSection";

  public PyramidSectionBuilder() {
  }

  public PyramidSectionBuilder(String id, String name) {
    super(id, name);
  }

  public PyramidSectionBuilder(String id, HashMap<String, BuilderParam> params) {
    super(id, params);
  }

  public PyramidSectionBuilder(String id, String name, JsonObject param) {
    super(id, name);
    setValue(BLDKEY_PLANE, param.get(BLDKEY_PLANE).asString());
    setValue(BLDKEY_PYRAMID, param.get(BLDKEY_PYRAMID).asString());
  }

  public PyramidSectionBuilder(HashMap<String, BuilderParam> params) {
    super(params);
  }

  @Override
  public void initParams(){
    super.initParams();
    addParam(BLDKEY_PLANE, BuilderParam.SECTION_PLANE_ALIAS, BuilderParamType.BODY, 60);
    addParam(BLDKEY_PYRAMID, "Пирамида", BuilderParamType.BODY, 61);
  }

  @Override
  public String alias() {
    return ALIAS;
  }

  @Override
  public i_Body create(Editor edt, i_ErrorHandler eh) {
    i_BodyContainer bodies = edt.bd();
    try {
      PlaneBody p = (PlaneBody) bodies.get(getValueAsString(BLDKEY_PLANE));
      if (!p.exists()) {
        throw new ExLostBody("плоскость");
      }

      PyramidBody pyram = (PyramidBody) bodies.get(getValueAsString(BLDKEY_PYRAMID));
      if (!pyram.exists()) {
        throw new ExLostBody("пирамида");
      }

      Polygon3d poly = pyram.pyramid().sectionByPlane(p.plane());
      if (poly.points().size() == 1) {
        PointBody result = new PointBody(_id, poly.points().get(0));
        edt.addAnchor(result.point(), result, "P");
        _exists = true;
        return result;
      } else if (poly.points().size() == 2) {
        Rib3d rib = new Rib3d(poly.points().get(0), poly.points().get(1));
        RibBody result = new RibBody(_id, rib);
        edt.addAnchor(result.A(), result, "A");
        edt.addAnchor(result.B(), result, "B");
        result.addRibs(edt);
        _exists = true;
        return result;
      } else {
        PolygonBody result = pyram.section(_id, p);
        for (int j = 0; j < result.polygon().vertNumber(); j++) {
          edt.addAnchor(result.polygon().points().get(j), result, String.valueOf(j),
                  title() + "_" + String.valueOf(j + 1));
        }
        result.addRibs(edt);
        result.addPlanes(edt);
        _exists = true;
        return result;
      }
    } catch (ExGeom | ExBadRef ex) {
      if (_exists) {
        eh.showMessage("Сечение пирамиды не построено: " + ex.getMessage(), error.Error.WARNING);
      }
      _exists = false;
      return new PolygonBody(_id);
    }
  }

  @Override
  public JsonObject getJSONParams() {
    JsonObject result = new JsonObject();
    result.add(BLDKEY_PYRAMID, getValueAsString(BLDKEY_PYRAMID));
    result.add(BLDKEY_PLANE, getValueAsString(BLDKEY_PLANE));
    return result;
  }

  @Override
  public String description(EdtController ctrl, int precision) {
    try {
      return String.format("<html><strong>Сечение</strong> пирамиды %s плоскостью %s",
              ctrl.getBodyTitle(getValueAsString(BLDKEY_PYRAMID)),
              ctrl.getBodyTitle(getValueAsString(BLDKEY_PLANE)));
    } catch (ExNoBody ex) {
      return "<html><strong>Сечение пирамиды</strong>";
    }
  }
}
