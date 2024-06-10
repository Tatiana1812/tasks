package builders;

import bodies.CubeBody;
import bodies.SphereBody;
import builders.param.BuilderParam;
import builders.param.BuilderParamType;
import editor.Editor;
import editor.ExLostBody;
import editor.ExNoBody;
import editor.i_Body;
import editor.i_BodyContainer;
import error.i_ErrorHandler;
import geom.Sphere3d;
import geom.Vect3d;
import gui.EdtController;
import java.util.HashMap;
import minjson.JsonObject;

/**
 * Inscribes sphere into a cube
 * @author alexeev
 */
public class SphereInCubeBuilder extends BodyBuilder {
  static public final String ALIAS = "SphereInCube";
   
  public SphereInCubeBuilder() {
  }

  public SphereInCubeBuilder(String id, String name) {
    super(id, name);
  }
  
  public SphereInCubeBuilder(String id, HashMap<String, BuilderParam> params) {
    super(id, params);
  }
  
  public SphereInCubeBuilder( String id, String name, JsonObject param ){
    super(id, name);
    setValue(BLDKEY_CUBE, param.get(BLDKEY_CUBE).asString());
  }
  
  public SphereInCubeBuilder(HashMap<String, BuilderParam> params) {
    super(params);
  }
  
  @Override
  public void initParams(){
    super.initParams();
    addParam(BLDKEY_CUBE, "Куб", BuilderParamType.BODY);
  }

  @Override
  public String alias() { return ALIAS; }
  
  @Override
  public i_Body create (Editor edt, i_ErrorHandler eh){
    i_BodyContainer bodies = edt.bd();
    try {
      CubeBody cube = (CubeBody)bodies.get(getValueAsString(BLDKEY_CUBE));
      if (!cube.exists())
        throw new ExLostBody("куб");
      
      Vect3d center = cube.center();
      double radius = cube.edgeLength() / 2;
      SphereBody result = new SphereBody(_id, title(), new Sphere3d(center, radius));
      edt.addAnchor(result.sphere().center(), result, "center");
      _exists = true;
      return result;
    } catch(ExNoBody ex){
      if (_exists)
	eh.showMessage("Сфера не построена: " + ex.getMessage(),error.Error.WARNING);
      _exists = false;
      return new SphereBody(_id, title());
    }
  }
  
  @Override
  public JsonObject getJSONParams() {
    JsonObject result = new JsonObject();
    result.add(BLDKEY_CUBE, getValueAsString(BLDKEY_CUBE));
    
    return result;
  }
  
  @Override
  public String description(EdtController ctrl, int precision) {
    try {
      return String.format("<html><strong>Сфера</strong> <br>вписанная в куб %s",
        ctrl.getBodyTitle(getValueAsString(BLDKEY_CUBE)));
    } catch (ExNoBody ex) {
      return "<html><strong>Сфера</strong>";
    }
  }
}
