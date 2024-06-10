package gui.dialog;

import gui.EdtController;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuccessDialog extends JDialog {
    JButton _checkVersionButton;

    public SuccessDialog(final EdtController ctrl, int num, int n) {
        super(ctrl.getFrame(), "Поздравляем!");
        _checkVersionButton = new JButton("OK");

        _checkVersionButton.addActionListener(e -> {
            ctrl.getLayoutController().getProblemManager().init((num + 2) % n);
            dispose();
        });

        setLayout(new MigLayout(new LC().insets("10")));
        add(new JLabel("Задача выполнена успешно!"),
                new CC().cell(0, 0));
        add(_checkVersionButton, new CC().cell(0, 2).alignX("right").gapY("30", ""));

        setResizable(false);
        setModal(true);
        pack();
        setLocationRelativeTo(ctrl.getFrame());
    }
}
