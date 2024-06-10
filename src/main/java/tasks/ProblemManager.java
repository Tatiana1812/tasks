package tasks;

import editor.i_EditorChangeListener;
import editor.ExNoAnchor;
import gui.EdtController;
import gui.dialog.SuccessDialog;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;


public class ProblemManager implements i_EditorChangeListener {
    protected EdtController _ctrl;
    protected Problem curProblem;
    protected List<Problem> problems;

    public ProblemManager(EdtController ctrl) {
        _ctrl = ctrl;
        problems = new ArrayList<Problem>() {{
            for (int i = 0; i < 18; i++) {
                add(new Problem(ctrl));
            }
        }};
    }

    public void init(int num) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _ctrl.getFocusCtrl().clearFocus();
                _ctrl.clearEditor();
                _ctrl.getMainCanvasCtrl().setDefaultMode();

                curProblem = problems.get(num - 1);
                curProblem.init(num);


                _ctrl.getLayoutController().getFrame().validate();
                _ctrl.getLayoutController().getFrame().setVisible(true);
                _ctrl.getLayoutController().getFrame().repaint();
                _ctrl.redraw();
            }
        });
    }

    @Override
    public void updateEditorState() {
        if (curProblem != null) {
            if (curProblem.check()) {
                SuccessDialog dialog = new SuccessDialog(_ctrl, problems.indexOf(curProblem), problems.size());
                dialog.setVisible(true);
            }
        }
    }
}