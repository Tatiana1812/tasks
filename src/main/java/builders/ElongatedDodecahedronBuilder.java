package builders;

import bodies.ElongatedDodecahedronBody;
import builders.param.BuilderParam;
import builders.param.BuilderParamType;
import editor.Editor;
import editor.ExBadRef;
import editor.ExNoAnchor;
import editor.i_AnchorContainer;
import editor.i_Body;
import error.i_ErrorHandler;
import geom.ExGeom;
import gui.EdtController;
import java.util.HashMap;
import minjson.JsonObject;

public class ElongatedDodecahedronBuilder extends BodyBuilder {
  static public final String ALIAS = "ElongatedDodecahedron";
  
  public ElongatedDodecahedronBuilder() {
  }

  public ElongatedDodecahedronBuilder(String name) {
    super(name);
  }

  public ElongatedDodecahedronBuilder(String id, String name) {
    super(id, name);
  }

  public ElongatedDodecahedronBuilder(String id, HashMap<String, BuilderParam> params) {
    super(id, params);
  }

  public ElongatedDodecahedronBuilder(String id, String name, JsonObject param) {
    super(id, name);
    setValue(BLDKEY_A, param.get(BLDKEY_A).asString());
    setValue(BLDKEY_B, param.get(BLDKEY_B).asString());
    setValue(BLDKEY_ANGLE, param.get(BLDKEY_ANGLE).asDouble());
  }

  public ElongatedDodecahedronBuilder(HashMap<String, BuilderParam> params) {
    super(params);
  }

  @Override
  public void initParams(){
    super.initParams();
    addParam(BLDKEY_A, "Первая вершина удлиненного додекаэдра", BuilderParamType.ANCHOR, 101);
    addParam(BLDKEY_B, "Вторая вершина удлиненного додекаэдра", BuilderParamType.ANCHOR, 100);
    addParam(BLDKEY_ANGLE, "Угол поворота", BuilderParamType.ANGLE_ROTATION);
  }

  @Override
  public String alias() {
    return ALIAS;
  }
  
  public final void addA(String id) {
    setValue(BLDKEY_A, id);
  }

  public final void addB(String id) {
    setValue(BLDKEY_B, id);
  }

  public final void addAngle(double angle) {
    setValue(BLDKEY_ANGLE, angle);
  }

  @Override
  public i_Body create (Editor edt, i_ErrorHandler eh){
    i_AnchorContainer anchors = edt.anchors();
    try {

      ElongatedDodecahedronBody result = new ElongatedDodecahedronBody(_id, title(),
              anchors.getVect(getValueAsString(BLDKEY_A)),
              anchors.getVect(getValueAsString(BLDKEY_B)),
              getValueAsDouble(BLDKEY_ANGLE));

      result.addAnchor("A", anchors.get(getValueAsString(BLDKEY_A)).id());
      result.addAnchor("B", anchors.get(getValueAsString(BLDKEY_B)).id());

      edt.addAnchor(result.C(), result, "C");
      edt.addAnchor(result.D(), result, "D");
      edt.addAnchor(result.E(), result, "E");
      edt.addAnchor(result.F(), result, "F");
       
      edt.addAnchor(result.A1(), result, "A1");
      edt.addAnchor(result.B1(), result, "B1");
      edt.addAnchor(result.C1(), result, "C1");
      edt.addAnchor(result.D1(), result, "D1");
      edt.addAnchor(result.E1(), result, "E1");
      edt.addAnchor(result.F1(), result, "F1");

      edt.addAnchor(result.H(), result, "H");
      edt.addAnchor(result.I(), result, "I");
      edt.addAnchor(result.J(), result, "J");
      edt.addAnchor(result.K(), result, "K");
      edt.addAnchor(result.L(), result, "L");
      edt.addAnchor(result.G(), result, "G");

      result.addRibs(edt);
      result.addPlanes(edt);
      _exists = true;
      return result;
    } catch (ExGeom | ExBadRef ex) {
      if (_exists) {
        eh.showMessage("Удлиненный додекаэдр не построен: " + ex.getMessage(), error.Error.WARNING);
        _exists = false;
      }
      return new ElongatedDodecahedronBody(_id, title());
    }
  }

  @Override
  public JsonObject getJSONParams() {
    JsonObject result = new JsonObject();
    result.add(BLDKEY_A, getValueAsString(BLDKEY_A));
    result.add(BLDKEY_B, getValueAsString(BLDKEY_B));
    result.add(BLDKEY_ANGLE, getValueAsDouble(BLDKEY_ANGLE));

    return result;
  }

  @Override
  public String description(EdtController ctrl, int precision) {
    try {
      return String.format("<html><strong>Удлиненный додекаэдр</strong><br> со стороной основания %s%s",
              ctrl.getAnchorTitle(getValueAsString(BLDKEY_A)),
              ctrl.getAnchorTitle(getValueAsString(BLDKEY_B)));
    } catch (ExNoAnchor ex) {
      return "<html><strong>Удлиненный додекаэдр</strong>";
    }
  }
}
