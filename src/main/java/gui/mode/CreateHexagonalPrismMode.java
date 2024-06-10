package gui.mode;

import bodies.BodyType;
import builders.HexagonalPrismBuilder;
import static config.Config.LOG_LEVEL;
import editor.ExNoBody;
import editor.i_Body;
import geom.ExGeom;
import geom.Prism3d;
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
 * Create mode of regular tetrahedron by two points and angle.
 *
 * @author kaznin
 */
public class CreateHexagonalPrismMode extends CreateBodyBy2PointsMode {

  public CreateHexagonalPrismMode(EdtController ctrl) {
    super(ctrl);
  }

  @Override
  public void run() {
    super.run();
    _param.setUsed(ROT_ANGLE, 1);
    _ctrl.getMainCanvasCtrl().notifyModeParamChange(ROT_ANGLE);
  }

  @Override
  protected void nativeDraw1(Render ren) {
    if (_numOfChosenAnchors == 2) {
      Drawer.setObjectColor(ren, ColorGL.RED);
      try {
        Prism3d ic = Prism3d.hexagonalPrismBy2PntsAngle(_anchor.get(0).getPoint(),
                _anchor.get(1).getPoint(), valueAsDouble(ROT_ANGLE));
        Drawer.drawPrism(ren, ic, TypeFigure.WIRE);
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
  protected void setCreationMessengers() {
    _msg.setMessage("Выберите первую вершину шестиугольной призмы{MOUSELEFT}",
            "Выберите вторую вершину шестиугольной призмы{MOUSELEFT}",
            "Выберите положение{UP}{DOWN}{MOUSEWHEEL}{ENTER}");
  }

  @Override
  protected void setName() {
    _name = BodyType.PRISM.getName(_ctrl.getEditor());
  }

  @Override
  public Icon getSmallIcon() {
    return IconList.PRISM.getLargeIcon();
  }

  @Override
  public Icon getLargeIcon() {
    return IconList.PRISM.getLargeIcon();
  }

  @Override
  public String description() {
    return "<html><strong>Правильная шестиугольная призма</strong><br>по двум вершинам основания";
  }

  @Override
  public ModeList alias() {
    return ModeList.MODE_CREATE_HEXPRISM;
  }

  @Override
  protected void create() {
    _numOfChosenAnchors = 0; // prevent drawing

    String[] defaultPointTitles = new String[12];
    String[] orderedPointTitles = new String[12];

    defaultPointTitles[0] = _anchor.get(0).getTitle();
    defaultPointTitles[1] = _anchor.get(1).getTitle();
    String firstVertTitle = Util.removeSuffix(defaultPointTitles[0]);
    String secondVertTitle = Util.removeSuffix(defaultPointTitles[1]);
    for (int i = 2; i < 12; i++) {
      defaultPointTitles[i] = BodyType.POINT.getName(_ctrl.getEditor(), i - 1);
    }
    for (int i = 0; i < 6; i++) {
      orderedPointTitles[i] = firstVertTitle + "_" + String.valueOf(i + 1);
      orderedPointTitles[i + 6] = secondVertTitle + "_" + String.valueOf(i + 1);
    }
    boolean[] fixTitles = new boolean[]{
      true, true, false, false, false, false, false, false, false, false, false, false
    };

    HexagonalPrismBuilder builder = new HexagonalPrismBuilder(_name);
    builder.addA(id(0));
    builder.addB(id(1));
    builder.addAngle(valueAsDouble(ROT_ANGLE));
    _ctrl.add(builder, checker, false);

    // Show dialog for vertices renaming.
    // Show dialog with three options:
    // Choose default alphabetical naming, like ABCDEFGHIJ;
    // Choose default indexed naming, like A_1A_2A_3A_4A_5A_6B_1B_2B_3B_4B_5B_6;
    // Choose point names by user.
    ChoosePointNamesDialog cpnd = new ChoosePointNamesDialog(_ctrl,
            new String[]{ Util.concat(defaultPointTitles),
              Util.concat(orderedPointTitles) }, defaultPointTitles, fixTitles);
    cpnd.setVisible(true);

    if (cpnd.accepted) {
      if (cpnd.getChoice() != 0) {
        String[] values;
        switch (cpnd.getChoice()) {
          case 1:
            values = orderedPointTitles; // use ordered naming 1
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
          _ctrl.setUndo("Переименование точек призмы");
        } catch (ExNoBody ex) {
          if (LOG_LEVEL.value() >= 1) {
            Log.out.printf("Cannot rename prism points: %s", ex.getMessage());
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
    _ctrl.getMainCanvasCtrl().notifyModeParamChange(ROT_ANGLE);
  }

  @Override
  protected void doIfTwoPointsChosen(KeyEvent e) {
    changeValue(ROT_ANGLE, e);
    _ctrl.getMainCanvasCtrl().notifyModeParamChange(ROT_ANGLE);
    if (isChosen(ROT_ANGLE)) {
      createWithNameValidation();// создаём икосаэдр
    }
  }

  @Override
  protected void doWhenOnePointChosen(MouseEvent e) {
  }

  @Override
  protected void nativeDraw0(Render ren) {
  }

  @Override
  public boolean isNameSelectable() {
    return true;
  }
}
