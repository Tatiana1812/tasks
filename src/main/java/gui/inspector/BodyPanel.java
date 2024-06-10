package gui.inspector;


import bodies.BodyType;
import editor.ExNoBody;
import editor.i_Body;
import editor.state.i_BodyStateChangeListener;
import geom.Polygon3d;
import geom.Rib3d;
import geom.Vect3d;
import gui.EdtController;
import gui.IconList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Representation of the body on the inspector panel.
 *
 * @author alexeev
 */
public class BodyPanel extends JPanel implements i_BodyStateChangeListener {

    private final EdtController _ctrl;
    private final JLabel _iconLabel = new JLabel();
    private final JLabel _descriptionLabel = new JLabel();
    private final BodyToolPanel _toolPanel;
    private DefaultMutableTreeNode _ribs = new DefaultMutableTreeNode();

    public BodyPanel(EdtController ctrl) {
        super(new MigLayout(new LC().gridGap("0", "0").insetsAll("0").fillX()));
        _ctrl = ctrl;
        _toolPanel = new BodyToolPanel(_ctrl);

        JPanel iconPanel = new JPanel(new MigLayout(new LC().insetsAll("0")));
        iconPanel.add(_iconLabel, new CC().alignY("top"));
        add(iconPanel, new CC().cell(0, 0, 1, 1).width("250!").height("250!"));

        JPanel descriptionPanel = new JPanel(new MigLayout(new LC().insetsAll("0")));
        descriptionPanel.add(_descriptionLabel, new CC().alignY("top"));
        add(descriptionPanel, new CC().cell(0, 1, 1, 1).width("260::").height("60!").gapX("8", "0")
                .alignX("left").alignY("bottom").growX());

        add(_toolPanel, new CC().cell(0, 2, 2, 1).width("300::").height("20!").growX());
    }

    public final void clear() {
        _iconLabel.setIcon(null);
        _descriptionLabel.setText("");
        revalidate();
        repaint();
    }

    /**
     * Окончание слова "тело" в зависимости от числительного.
     */
    private static String getEnding(int number) {
        int digit = number % 10;
        int twoDigits = number % 100;

        if (twoDigits / 10 == 1) {
            return "";
        } else if (digit == 1) {
            return "о";
        } else if (digit >= 2 && digit <= 4) {
            return "а";
        } else {
            return "";
        }
    }

    @Override
    public void updateBodyState(String bodyID){

    }

}