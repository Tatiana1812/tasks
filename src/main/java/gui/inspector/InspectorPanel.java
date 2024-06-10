package gui.inspector;

import editor.i_EditorChangeListener;
import editor.i_FocusChangeListener;
import gui.EdtController;
import gui.ui.EdtFramedPanel;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Панель инспектора объектов. Содержит список тел и панель информации об объекте.
 *
 * @author alexeev
 */
public class InspectorPanel extends JPanel implements i_EditorChangeListener, i_FocusChangeListener {

  private EdtController _ctrl;
  private BodyInfoPanel _bodyInfoPanel;
  private BodyTaskPanel _bodyTaskPanel;
  private BodyPanel _bodyTask;
  private EdtFramedPanel _infoPanelWrapper;
  private EdtFramedPanel _inspectorWrapper;
  private EdtFramedPanel _taskPanelWrapper;
  private BodyTree _bodiesTree;

  /**
   *
   * @param ctrl
   */
  public InspectorPanel(EdtController ctrl) {
    super();
    _ctrl = ctrl;
    _ctrl.addEditorStateChangeListener(this);
    _ctrl.getFocusCtrl().addFocusChangeListener(this);
    setLayout(new BorderLayout());

    init();
  }

  public final void init() {
    _bodiesTree = new BodyTree(_ctrl, true);
    _ctrl.addBodyStateChangeListener(_bodiesTree);

    _bodyInfoPanel = new BodyInfoPanel(_ctrl);
    _bodyTaskPanel = new BodyTaskPanel(_ctrl);
    _bodyTask = new BodyPanel(_ctrl);

    _ctrl.addBodyStateChangeListener(_bodyInfoPanel);
    _ctrl.addBodyStateChangeListener(_bodyTaskPanel);
    _ctrl.addBodyStateChangeListener(_bodyTask);

    _infoPanelWrapper = new EdtFramedPanel("Информация");
    _inspectorWrapper = new EdtFramedPanel("Инспектор объектов");
    _taskPanelWrapper = new EdtFramedPanel("Результат");
    _infoPanelWrapper.setLayout(new BoxLayout(_infoPanelWrapper, BoxLayout.PAGE_AXIS));
    _taskPanelWrapper.setLayout(new BoxLayout(_taskPanelWrapper,BoxLayout.PAGE_AXIS));
    _inspectorWrapper.setLayout(new BorderLayout());
    JScrollPane inspectorScrollPane = new JScrollPane(_inspectorWrapper);
    inspectorScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    inspectorScrollPane.setBorder(null);

    _infoPanelWrapper.add(_bodyInfoPanel, BorderLayout.CENTER);
    _inspectorWrapper.add(_bodiesTree, BorderLayout.NORTH);
    _taskPanelWrapper.add(_bodyTaskPanel,BorderLayout.CENTER);

    add(_infoPanelWrapper, BorderLayout.NORTH);
    add(inspectorScrollPane, BorderLayout.SOUTH);
    add(_taskPanelWrapper,BorderLayout.CENTER);
  }

  public void updateTaskInfo(int number,String description){
    setEnabled(false);
    _bodyTaskPanel.update(number,description);
    setEnabled(true);
  }

  @Override
  public void updateEditorState() {
    _bodiesTree.update();
    _bodiesTree.updateFocus();
    //TODO: Модель обновляется долго. Нужно придумать тонкое решение.
    _bodiesTree.updateModel();
    _bodiesTree.updateFocus();
    _bodyInfoPanel.update();

    revalidate();
    repaint();
  }

  @Override
  public void focusCleared(Collection<String> bodyIDs) {
    setEnabled(false);  // синхронизируем отображение
    _bodiesTree.focusCleared();
    _bodyInfoPanel.clear();
    setEnabled(true);   // синхронизируем отображение
  }

  @Override
  public void focusLost(String id, boolean isBody) {
    if (isBody) {
      setEnabled(false);  // синхронизируем отображение
      _bodiesTree.focusLost(id);
      _bodyInfoPanel.update();
      setEnabled(true);   // синхронизируем отображение
    }
  }

  @Override
  public void focusApply(String id, boolean isBody) {
    if (isBody) {
      setEnabled(false);  // синхронизируем отображение
      _bodiesTree.focusApply(id);
      _bodyInfoPanel.update();
      setEnabled(true);   // синхронизируем отображение
    }
  }
}
