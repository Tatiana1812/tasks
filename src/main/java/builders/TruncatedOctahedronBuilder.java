package builders;

import bodies.TruncatedOctahedronBody;
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

public class TruncatedOctahedronBuilder extends BodyBuilder {
  static public final String ALIAS = "TruncatedOctahedron";
  
  public TruncatedOctahedronBuilder() {
  }

  public TruncatedOctahedronBuilder(String name) {
    super(name);
  }

  public TruncatedOctahedronBuilder(String id, String name) {
    super(id, name);
  }

  public TruncatedOctahedronBuilder(String id, HashMap<String, BuilderParam> params) {
    super(id, params);
  }

  public TruncatedOctahedronBuilder(String id, String name, JsonObject param) {
    super(id, name);
    addA(param.get(BLDKEY_A).asString());
    addB(param.get(BLDKEY_B).asString());
    addAngle(param.get(BLDKEY_ANGLE).asDouble());
  }

  public TruncatedOctahedronBuilder(HashMap<String, BuilderParam> params) {
    super(params);
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
  public void initParams(){
    super.initParams();
    addParam(BLDKEY_A, "Первая вершина октаэдра", BuilderParamType.ANCHOR, 101);
    addParam(BLDKEY_B, "Вторая вершина октаэдра", BuilderParamType.ANCHOR, 100);
    addParam(BLDKEY_ANGLE, BuilderParam.ROT_ANGLE_ALIAS, BuilderParamType.ANGLE_ROTATION);
  }

  @Override
  public String alias() {
    return ALIAS;
  }

  @Override
  public i_Body create (Editor edt, i_ErrorHandler eh){
    i_AnchorContainer anchors = edt.anchors();
    try {

      TruncatedOctahedronBody result = new TruncatedOctahedronBody(_id, title(),
              anchors.getVect(getValueAsString(BLDKEY_A)),
              anchors.getVect(getValueAsString(BLDKEY_B)),
              (double) getValue(BLDKEY_ANGLE));

      result.addAnchor("A", anchors.get(getValueAsString(BLDKEY_A)).id());
      result.addAnchor("B", anchors.get(getValueAsString(BLDKEY_B)).id());

      edt.addAnchor(result.C(), result, "C");
      edt.addAnchor(result.D(), result, "D");
      edt.addAnchor(result.E(), result, "E");
      edt.addAnchor(result.F(), result, "F");
      edt.addAnchor(result.G(), result, "G");
      edt.addAnchor(result.H(), result, "H");

      edt.addAnchor(result.I(), result, "I");
      edt.addAnchor(result.J(), result, "J");
      edt.addAnchor(result.K(), result, "K");
      edt.addAnchor(result.L(), result, "L");
      edt.addAnchor(result.M(), result, "M");
      edt.addAnchor(result.N(), result, "N");
      edt.addAnchor(result.O(), result, "O");
      edt.addAnchor(result.P(), result, "P");

      edt.addAnchor(result.Q(), result, "Q");
      edt.addAnchor(result.R(), result, "R");
      edt.addAnchor(result.S(), result, "S");
      edt.addAnchor(result.T(), result, "T");
      edt.addAnchor(result.U(), result, "U");
      edt.addAnchor(result.V(), result, "V");
      edt.addAnchor(result.W(), result, "W");
      edt.addAnchor(result.X(), result, "X");

      result.addRibs(edt);
      result.addPlanes(edt);
      _exists = true;
      return result;
    } catch (ExGeom | ExBadRef ex) {
      if (_exists) {
        eh.showMessage("Усеченный октаэдр не построен: " + ex.getMessage(), error.Error.WARNING);
        _exists = false;
      }
      return new TruncatedOctahedronBody(_id, title());
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
      return String.format("<html><strong>Усечённый октаэдр</strong><br> со стороной основания %s%s",
              ctrl.getAnchorTitle(getValueAsString(BLDKEY_A)),
              ctrl.getAnchorTitle(getValueAsString(BLDKEY_B)));
    } catch (ExNoAnchor ex) {
      return "<html><strong>Усечённый октаэдр</strong>";
    }
  }
}
