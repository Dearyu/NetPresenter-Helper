package action;

import bean.ElementBean;
import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.PsiUtils;

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
    // 1.5 添加修改psielement功能
    private PsiElement mPsiElement;

    private boolean isAllOne = false;

    private NetPresenterWriter(@Nullable Project project, @NotNull PsiFile... files) {
        super(project, files);
    }

    NetPresenterWriter(PsiClass cls, PsiFile file, String tag, Set<String> callback, List<ElementBean> methods, PsiElement psiElement) {
        this(file.getProject(), file);
        mPsiClass = cls;
        mPsiFile = file;
        mProject = cls.getProject();
        mTag = tag;
        mCallBack = callback;
        mMethods = methods;
        mFactory = JavaPsiFacade.getElementFactory(mProject);
        mPsiElement = psiElement;
//        System.out.println("mPsiClass:" + mPsiClass.toString());
//        System.out.println("mPsiFile:" + mPsiFile.toString());
//        System.out.println("mTag:" + mTag.toString());
//        System.out.println("mCallBack:" + mCallBack.toString());
        int num = 0;
        isAllOne = methods.size() <= 1;
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
                    replaceEliment();
                    for (ElementBean element : mMethods) {
                        if (element.isCheck()) {
                            addSucMethod(element);
                        }
                    }
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

    private void replaceEliment() {
//        PsiElement psiElement =
        String annotationName = "@NetService(value = \"" + mTag + "\")";
        mPsiClass.addBefore(mFactory.createAnnotationFromText(annotationName, mPsiClass), mPsiElement);
//        try {
//            mPsiElement.add(mFactory.createAnnotationFromText(annotationName, mPsiClass));
//        } catch (Exception e) {
//            mPsiClass.add(mFactory.createAnnotationFromText(annotationName + e.toString(), mPsiElement));
//        }
//        mPsiElement.add();
//        mPsiClass.replace();
    }

    private void addSucMethod(ElementBean elementBean) {
        StringBuilder method = new StringBuilder();
//        method.append("@netpresenter.annotations.NetCallBack(");
        String methodName = PsiUtils.ClsName;
        method.append("@NetCallBack(");
        if (!"".equals(mTag)) {
            method.append("value = ").append("\"" + mTag + "\"").append(", ");
            if (!isOne || !isAllOne) { // 如果不是一个方法,则添加tag
                method.append("tag = ").append("\"" + elementBean.getMethodTag() + "\"").append(", ");
                methodName = PsiUtils.ClsName + "_" + elementBean.getMethodTag();
            }
        }
//        method.append("type = ").append("netpresenter.annotations.CallBackType.SUC").append(")")
        method.append("type = ").append("CallBackType.SUC").append(")")
//                .append("public void getNetMsgSuc(java.lang.String tag, java.lang.Object bean){");
                .append("public void get" + methodName + "Suc(java.lang.String tag, " + elementBean.getObjType() + " bean){");
        method.append("}");
        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
    }

//    private void addSucMethod() {
//        StringBuilder method = new StringBuilder();
////        method.append("@netpresenter.annotations.NetCallBack(");
//        method.append("@NetCallBack(");
//        if (!"".equals(mTag)) {
//            method.append("value = ").append("\"" + mTag + "\"").append(", ");
//        }
////        method.append("type = ").append("netpresenter.annotations.CallBackType.SUC").append(")")
//        method.append("type = ").append("CallBackType.SUC").append(")")
////                .append("public void getNetMsgSuc(java.lang.String tag, java.lang.Object bean){");
//                .append("public void get" + PsiUtils.ClsName + "Suc(java.lang.String tag, java.lang.Object bean){");
//        addSwitch(method);
//        method.append("}");
//        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
//    }

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
//                .append("public void getNetMsgFail(java.lang.String tag, java.lang.String... msgs){");
                .append("public void get" + PsiUtils.ClsName + "Fail(java.lang.String tag, java.lang.String... msgs){");
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
                .append("public void get" + PsiUtils.ClsName + "Start(java.lang.String tag){");
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
                .append("public void get" + PsiUtils.ClsName + "Finish(java.lang.String tag){");
        addSwitch(method);
        method.append("}");
        mPsiClass.add(mFactory.createMethodFromText(method.toString(), mPsiClass));
    }
}
