package ui.form;


import util.PsiUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @version 1.0
 * @author: liujunwei
 * @date: 2021/3/23
 * @description:
 */
public class NetEntryConfig extends JPanel {

    private JLabel mTagLabel;
    private JTextField mTag;
    private JCheckBox mSuc;
    private JCheckBox mFail;
    private JCheckBox mStart;
    private JCheckBox mFinish;
    private Set<String> mCallBack = new LinkedHashSet<>();

    public NetEntryConfig() {
        mTagLabel = new JLabel("Tag");
        mTagLabel.setPreferredSize(new Dimension(40, 30));

        mTag = new JTextField(PsiUtils.ClsName);
        mTag.setPreferredSize(new Dimension(200, 26));

        mSuc = new JCheckBox("onSuc");
        mSuc.setPreferredSize(new Dimension(70, 30));
        mSuc.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (mSuc.isSelected()) {
                    mCallBack.add("onSuc");
                } else {
                    mCallBack.remove("onSuc");
                }
            }
        });
        mSuc.setSelected(true);

        mFail = new JCheckBox("onFail");
        mFail.setPreferredSize(new Dimension(70, 30));
        mFail.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (mFail.isSelected()) {
                    mCallBack.add("onFail");
                } else {
                    mCallBack.remove("onFail");
                }
            }
        });
        mFail.setSelected(true);

        mStart = new JCheckBox("onStart");
        mStart.setPreferredSize(new Dimension(80, 30));
        mStart.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (mStart.isSelected()) {
                    mCallBack.add("onStart");
                } else {
                    mCallBack.remove("onStart");
                }
            }
        });

        mFinish = new JCheckBox("onFinish");
        mFinish.setPreferredSize(new Dimension(80, 30));
        mFinish.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (mFinish.isSelected()) {
                    mCallBack.add("onFinish");
                } else {
                    mCallBack.remove("onFinish");
                }
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        add(Box.createRigidArea(new Dimension(1, 0)));
        add(mTagLabel);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mTag);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mSuc);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mFail);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mStart);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mFinish);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(Box.createHorizontalBox());
    }

    public String getTagText() {
        return mTag.getText();
    }

    public Set<String> getCallBack() {
        return mCallBack;
    }
}
