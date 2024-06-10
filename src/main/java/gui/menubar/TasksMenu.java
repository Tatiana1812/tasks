package gui.menubar;


import gui.EdtController;
import gui.IconList;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Help menu
 *
 * @author alexeev
 */
public class TasksMenu extends JMenu {
    private EdtController _ctrl;

    /**
     * @param ctrl
     */
    public TasksMenu(EdtController ctrl) {
        super("Задачи");
        _ctrl = ctrl;
        for (int i = 0; i < 18; i++) {
            initAboutDialog(i);
        }
    }

    private void initAboutDialog(int i) {
        JMenuItem aboutMI = new JMenuItem("Задача " + ++i, IconList.EMPTY.getMediumIcon());
        int finalI = i;
        aboutMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                _ctrl.getLayoutController().getProblemManager().init(finalI);
            }
        });
        add(aboutMI);
    }

}
