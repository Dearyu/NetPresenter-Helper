package ui.form;

import javax.swing.*;
import java.awt.*;

/**
 * @version 1.0
 * @author: liujunwei
 * @date: 2021/3/23
 * @description:
 */
public class NetEntryTitle extends JPanel {

    protected JLabel mChoose;
    protected JLabel mChooseTag;

    public NetEntryTitle() {
        mChoose = new JLabel("Choose");
        mChoose.setPreferredSize(new Dimension(60, 30));
        mChoose.setHorizontalTextPosition(SwingConstants.LEFT);
        mChoose.setFont(new Font(mChoose.getFont().getFontName(), Font.BOLD, mChoose.getFont().getSize()));

        mChooseTag = new JLabel("MethodTag");
        mChooseTag.setPreferredSize(new Dimension(400, 30));
        mChoose.setHorizontalTextPosition(SwingConstants.RIGHT);
        mChooseTag.setFont(new Font(mChooseTag.getFont().getFontName(), Font.BOLD, mChooseTag.getFont().getSize()));

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mChoose);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mChooseTag);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(Box.createHorizontalGlue());
    }
}
