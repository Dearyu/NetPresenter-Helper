package action;

import bean.ElementBean;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * @version 1.0
 * @author: liujunwei
 * @date: 2021/3/22
 * @description:
 */
public class NetPresenterWriter extends WriteCommandAction {

    private PsiClass mPsiClass;
    private PsiFile mPsiFile;
    private Project mProject;
    private String mTag;
    private Set<String> mCallBack;
    private List<ElementBean> mMethods;
    private PsiElementFactory mFactory;
    private boolean isOne = true;

    private NetPresenterWriter(@Nullable Project project, @NotNull PsiFile... files) {
        super(project, files);
    }

    NetPresenterWriter(PsiClass cls, PsiFile file, String tag, Set<String> callback, List<ElementBean> methods) {
        this(file.getProject(), file);
        mPsiClass = cls;
        mPsiFile = file;
        mProject = cls.getProject();
        mTag = tag;
        mCallBack = callback;
        mMethods = methods;
        mFactory = JavaPsiFacade.getElementFactory(mProject);
//        System.out.println("mPsiClass:" + mPsiClass.toString());
//        System.out.println("mPsiFile:" + mPsiFile.toString());
//        System.out.println("mTag:" + mTag.toString());
//        System.out.println("mCallBack:" + mCallBack.toString());
        int num = 0;
        for (ElementBean method : methods) {
            if (method.isCheck()) {
                num++;
            }
            if (num > 1) {
                isOne = false;
                break;
            }
//            System.out.println("mMethods:" + method.toString());
        }
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        if (null == mCallBack || mCallBack.isEmpty()) {
            return;
        }
        for (String callback : mCallBack) {
            switch (callback) {
                case "onSuc":
                    addSucMethod();
                    break;
                case "onFail":
                    addFailMethod();
                    break;
                case "onStart":
                    addStartMethod();
                    break;
                case "onFinish":
                    addFinishMethod();
                    break;
            }
        }

        // reformat class
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        styleManager.optimizeImports(mPsiFile);
        styleManager.shortenClassReferences(mPsiFile);
        new ReformatCodeProcessor(mProject, mPsiClass.getContainingFile(), null, false).runWithoutProgress();
    }

    private void addSucMethod() {
        StringBuilder method = new StringBuilder();
//        method.append("@netpresenter.annotations.NetCallBack(");
        method.append("@NetCallBack(");
        if (!"".equals(mTag)) {
            method.append("value = ").append("\"" + mTag + "\"").append(", ");
        }
//        method.append("type = ").append("netpresenter.annotations.CallBackType.SUC").append(")")
        method.append("type = ").append("CallBackType.SUC").append(")")
                .append("public void getNetMsgSuc(java.lang.String tag, java.lang.Object bean){");
        addSwitch(method);
        method.append("}");
        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
    }

    private void addSwitch(StringBuilder method) {
        if (null != mMethods && !isOne) {
            method.append("switch (tag){");
            for (ElementBean element : mMethods) {
                if (element.isCheck()) {
                    method.append("case ").append("\"" + element.getMethodTag() + "\"").append(": break;");
                }
            }
            method.append("}");
        }
    }

    private void addFailMethod() {
        StringBuilder method = new StringBuilder();
//        method.append("@netpresenter.annotations.NetCallBack(");
        method.append("@NetCallBack(");
        if (!"".equals(mTag)) {
            method.append("value = ").append("\"" + mTag + "\"").append(", ");
        }
//        method.append("type = ").append("netpresenter.annotations.CallBackType.FAIL").append(")")
        method.append("type = ").append("CallBackType.FAIL").append(")")
                .append("public void getNetMsgFail(java.lang.String tag, java.lang.String... msgs){");
        addSwitch(method);
        method.append("}");
        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
    }

    private void addStartMethod() {
        StringBuilder method = new StringBuilder();
//        method.append("@netpresenter.annotations.NetCallBack(");
        method.append("@NetCallBack(");
        if (!"".equals(mTag)) {
            method.append("value = ").append("\"" + mTag + "\"").append(", ");
        }
//        method.append("type = ").append("netpresenter.annotations.CallBackType.START").append(")")
        method.append("type = ").append("CallBackType.START").append(")")
                .append("public void getNetMsgStart(java.lang.String tag){");
        addSwitch(method);
        method.append("}");
        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
    }

    private void addFinishMethod() {
        StringBuilder method = new StringBuilder();
//        method.append("@netpresenter.annotations.NetCallBack(");
        method.append("@NetCallBack(");
        if (!"".equals(mTag)) {
            method.append("value = ").append("\"" + mTag + "\"").append(", ");
        }
//        method.append("type = ").append("netpresenter.annotations.CallBackType.FINISH").append(")")
        method.append("type = ").append("CallBackType.FINISH").append(")")
                .append("public void getNetMsgFinish(java.lang.String tag){");
        addSwitch(method);
        method.append("}");
        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
    }
}
