package action;

import bean.ElementBean;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.lang.jvm.JvmClassKind;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ui.form.NetEntryContent;
import ui.iface.ICancelListener;
import ui.iface.IConfirmListener;
import util.NotifyUtils;
import util.PsiUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;

public class NetPresenter extends BaseGenerateAction implements ICancelListener, IConfirmListener {

    private JFrame mDialog;
    private ArrayList<ElementBean> mElementBeans;
    private Editor mEditor;
    private PsiFile mPsiFile;
    private PsiClass mPsiCls;

    public NetPresenter() {
        super(null);
    }

    public NetPresenter(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return super.isValidForClass(targetClass);
    }

    @Override
    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        PsiClass cls = PsiUtils.getPsiClassByEditor(editor, file);
        return null != cls && cls.getClassKind() == JvmClassKind.INTERFACE && PsiUtils.isRetorfitService(cls) && super.isValidForFile(project, editor, file);
    }

    @Override
    public void actionPerformed(AnActionEvent action) {
        mEditor = action.getData(DataKeys.EDITOR);
        mPsiFile = action.getData(DataKeys.PSI_FILE);
        mPsiCls = PsiUtils.getPsiClassByEditor(mEditor, mPsiFile);
        if (null != mPsiCls) {
            mElementBeans = PsiUtils.getMethodsName(mPsiCls);
            showNetDialog();
        } else {
            assert mPsiFile != null;
            NotifyUtils.showError(mPsiFile.getProject(), "No Service Found");
        }
    }

    private void showNetDialog() {
        JPanel panel = new NetEntryContent(mElementBeans, this, this);
        mDialog = new JFrame();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mDialog.getContentPane().add(panel);
        mDialog.pack();
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);
    }

    private void closeNetDialog() {
        if (mDialog == null) {
            return;
        }
        mDialog.setVisible(false);
        mDialog.dispose();
    }

    @Override
    public void onCancel() {
        closeNetDialog();
    }

    @Override
    public void onConfirm(String tag, Set<String> callback) {
        new NetPresenterWriter(getTargetClass(mEditor, mPsiFile), mPsiFile, tag, callback, mElementBeans).execute();
        closeNetDialog();
    }
}
