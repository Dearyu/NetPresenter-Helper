package ui.form;

import bean.ElementBean;
import com.intellij.ui.components.JBScrollPane;
import ui.iface.ICancelListener;
import ui.iface.IConfirmListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * @version 1.0
 * @author: liujunwei
 * @date: 2021/3/23
 * @description:
 */
public class NetEntryContent extends JPanel {

    private final ArrayList<ElementBean> mElementBeans;
    private ICancelListener mCancelListener;
    private IConfirmListener mConfirmListener;
    private NetEntryConfig mMEntryConnfig;
    private NetEntryTitle mMEntryTitle;
    private JButton mCancel;
    private JButton mConfirm;

    public NetEntryContent(ArrayList<ElementBean> beans, ICancelListener cancel, IConfirmListener confirm) {
        mElementBeans = beans;
        mCancelListener = cancel;
        mConfirmListener = confirm;
        setPreferredSize(new Dimension(620, 300));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addTitleAndMethods();
        addButton();
    }

    private void addTitleAndMethods() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mMEntryConnfig = new NetEntryConfig();
        contentPanel.add(mMEntryConnfig);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mMEntryTitle = new NetEntryTitle();
        contentPanel.add(mMEntryTitle);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.PAGE_AXIS));
        entryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        for (ElementBean mElementBean : mElementBeans) {
            NetEntry entry = new NetEntry(mElementBean);
            entryPanel.add(entry);
        }
        entryPanel.add(Box.createVerticalGlue());
        entryPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JBScrollPane scrollPane = new JBScrollPane(entryPanel);
        contentPanel.add(scrollPane);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addButton() {
        mCancel = new JButton();
        mCancel.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != mCancelListener) {
                    mCancelListener.onCancel();
                }
            }
        });
        mCancel.setPreferredSize(new Dimension(120, 30));
        mCancel.setText("Cancel");

        mConfirm = new JButton();
        mConfirm.setAction(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != mConfirmListener) {
                    mConfirmListener.onConfirm(mMEntryConnfig.getTagText(), mMEntryConnfig.getCallBack());
                }
            }
        });
        mConfirm.setPreferredSize(new Dimension(120, 30));
        mConfirm.setText("Confirm");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(mCancel);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(mConfirm);

        add(buttonPanel, BorderLayout.PAGE_END);
    }
}
