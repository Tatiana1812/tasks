package gui;

import gui.menubar.*;

import javax.swing.JMenuBar;

/**
 *
 * @author alexeev
 */
public class EdtMenuBar extends JMenuBar {

  public EdtMenuBar(EdtController ctrl){
    super();

    add(new Filemenu(ctrl));
    add(new EditMenu(ctrl));
    add(new ViewMenu(ctrl));
    add(new HelpMenu(ctrl));
    add(new TasksMenu(ctrl));
  }
}