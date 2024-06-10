package gui.mode;

import bodies.BodyType;
import builders.ElongatedDodecahedronBuilder;
import static config.Config.LOG_LEVEL;
import editor.ExNoBody;
import editor.i_Body;
import geom.ElongatedDodecahedron3d;
import geom.ExGeom;
import gui.EdtController;
import gui.IconList;
import gui.dialog.ChoosePointNamesDialog;
import static gui.mode.param.CreateModeParamType.ROT_ANGLE;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.Icon;
import opengl.Drawer;
import opengl.Render;
import opengl.TypeFigure;
import opengl.colorgl.ColorGL;
import util.Log;
import util.Util;

/**
 * Create mode of elongatedDodecahedron by two points and angle
 *
 * @author kaznin
 */
public class CreateElongatedDodecahedronMode extends CreateBodyBy2PointsMode {

  public CreateElongatedDodecahedronMode(EdtController ctrl) {
    super(ctrl);
  }

  @Override
  public void run() {
    super.run();
    _param.setUsed(ROT_ANGLE, 1);
    canvas().notifyModeParamChange(ROT_ANGLE);
    _ctrl.redraw();
  }

  @Override
  protected void setCreationMessengers() {
    _msg.setMessage("Выберите первую вершину додекаэдра{MOUSELEFT}",
            "Выберите вторую вершину додекаэдра{MOUSELEFT}",
            "Выберите положение додекаэдра{UP}{DOWN}{MOUSEWHEEL}{ENTER}");
  }

  @Override
  protected void setName() {
    _name = BodyType.ELONGATED_DODECAHEDRON.getName(_ctrl.getEditor());
  }

  @Override
  public Icon getSmallIcon() {
    return IconList.ELONGATED_DODECAHEDRON.getLargeIcon();
  }

  @Override
  public Icon getLargeIcon() {
    return IconList.ELONGATED_DODECAHEDRON.getLargeIcon();
  }

  @Override
  public String description() {
    return "<html><strong>Удлиненный додекаэдр</strong><br>по двум вершинам основания";
  }

  @Override
  public ModeList alias() {
    return ModeList.MODE_CREATE_ELONGATEDDODECAHEDRON;
  }

  @Override
  protected void create() {
    _numOfChosenAnchors = 0; // prevent drawing

    String[] defaultPointTitles = new String[18];
    String[] orderedPointTitles1 = new String[18];
    String[] orderedPointTitles2 = new String[18];
    defaultPointTitles[0] = _anchor.get(0).getTitle();
    defaultPointTitles[1] = _anchor.get(1).getTitle();
    String firstVertTitle = Util.removeSuffix(defaultPointTitles[0]);
    String secondVertTitle = Util.removeSuffix(defaultPointTitles[1]);
    for (int i = 2; i < 18; i++) {
      defaultPointTitles[i] = BodyType.POINT.getName(_ctrl.getEditor(), i - 1);
    }
    for (int i = 0; i < 18; i++) {
      orderedPointTitles1[i] = firstVertTitle + "_" + String.valueOf(i + 1);
    }
    for (int i = 0; i < 18; i++) {
      orderedPointTitles2[i] = secondVertTitle + "_" + String.valueOf(i + 1);
    }
    boolean[] fixTitles = new boolean[18];
    fixTitles[0] = true;
    fixTitles[1] = true;
    for (int i = 2; i < 18; i++) {
      fixTitles[i] = false;
    }

    ElongatedDodecahedronBuilder builder = new ElongatedDodecahedronBuilder(_name);
    builder.addA(id(0));
    builder.addB(id(1));
    builder.addAngle(valueAsDouble(ROT_ANGLE));
    _ctrl.add(builder, checker, false);

    // Show dialog for vertices renaming.
    // Show dialog with three options:
    // Choose default alphabetical naming, like ABCDEFGHIJ...;
    // Choose default indexed naming, like A_1A_2A_3A_4A_5...;
    // Choose point names by user.
    ChoosePointNamesDialog cpnd = new ChoosePointNamesDialog(_ctrl,
            new String[]{ Util.concat(defaultPointTitles),
              Util.concat(orderedPointTitles1),
              Util.concat(orderedPointTitles2) }, defaultPointTitles, fixTitles);
    cpnd.setVisible(true);

    if (cpnd.accepted) {
      if (cpnd.getChoice() != 0) {
        String[] values;
        switch (cpnd.getChoice()) {
          case 1:
            values = orderedPointTitles1; // use ordered naming 1
            break;
          case 2:
            values = orderedPointTitles2; // use ordered naming 2
            break;
          default:
            values = cpnd.getValues(); // use user-defined naming
        }
        try {
          i_Body bd = _ctrl.getBody(builder.id());
          _ctrl.renameAnchorPoint(bd.getAnchorID("A"), values[0]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("B"), values[1]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("C"), values[2]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("D"), values[3]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("E"), values[4]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("F"), values[5]);

          _ctrl.renameAnchorPoint(bd.getAnchorID("A1"), values[6]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("B1"), values[7]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("C1"), values[8]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("D1"), values[9]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("E1"), values[10]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("F1"), values[11]);

          _ctrl.renameAnchorPoint(bd.getAnchorID("G"), values[12]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("H"), values[13]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("I"), values[14]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("J"), values[15]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("K"), values[16]);
          _ctrl.renameAnchorPoint(bd.getAnchorID("L"), values[17]);
          _ctrl.setUndo("Переименование точек удлинённого додекаэдра");
        } catch (ExNoBody ex) {
          if (LOG_LEVEL.value() >= 1) {
            Log.out.printf("Cannot rename elongated dodecahedron points: %s", ex.getMessage());
          }
        }
        _ctrl.notifyEditorStateChange();
      }
    }

    reset();
  }

  @Override
  protected void doIfTwoPointsChosen() {
    showValue(ROT_ANGLE);
  }

  @Override
  protected void doIfTwoPointsChosen(MouseWheelEvent e) {
    changeValue(ROT_ANGLE, e.getWheelRotation());
    canvas().notifyModeParamChange(ROT_ANGLE);
  }

  @Override
  protected void doIfTwoPointsChosen(KeyEvent e) {
    changeValue(ROT_ANGLE, e);
    canvas().notifyModeParamChange(ROT_ANGLE);
    if (isChosen(ROT_ANGLE)) {
      createWithNameValidation();// создаём додекаэдр
    }
  }

  @Override
  protected void doWhenOnePointChosen(MouseEvent e) {
  }

  @Override
  protected void nativeDraw1(Render ren) {
    if (_numOfChosenAnchors == 2) {
      Drawer.setObjectColor(ren, ColorGL.RED);
      try {
        ElongatedDodecahedron3d dod = new ElongatedDodecahedron3d(_anchor.get(0).getPoint(),
                _anchor.get(1).getPoint(), valueAsDouble(ROT_ANGLE));
        Drawer.drawElongatedDodecahedron(ren, dod, TypeFigure.WIRE);
      } catch (ExGeom ex) {
      }
      Drawer.drawPoint(ren, _anchor.get(1).getPoint(), _anchor.get(1).getState().getFocusedPointThickness() * 2);
    }
    if (_numOfChosenAnchors == 1) {
      Drawer.setObjectColor(ren, ColorGL.RED);
      Drawer.drawPoint(ren, _anchor.get(0).getPoint(), _anchor.get(0).getState().getFocusedPointThickness() * 2);
    }
  }

  @Override
  protected void nativeDraw0(Render ren) {
  }

  @Override
  public boolean isNameSelectable() {
    return true;
  }
}
