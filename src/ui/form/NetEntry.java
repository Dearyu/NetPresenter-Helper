package ui.form;

import bean.ElementBean;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @version 1.0
 * @author: liujunwei
 * @date: 2021/3/23
 * @description:
 */
public class NetEntry extends JPanel {

    private JCheckBox mCheckBox;
    private JLabel mMethodLabel;

    public NetEntry(ElementBean bean) {

        mCheckBox = new JCheckBox();
        mCheckBox.setPreferredSize(new Dimension(50, 30));
        mCheckBox.setSelected(bean.isCheck());
        mCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                bean.setCheck(mCheckBox.isSelected());
            }
        });

        mMethodLabel = new JLabel(bean.getMethodTag());
        mMethodLabel.setPreferredSize(new Dimension(400, 30));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        add(Box.createRigidArea(new Dimension(20, 0)));
        add(mCheckBox);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mMethodLabel);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(Box.createHorizontalGlue());
    }
}
