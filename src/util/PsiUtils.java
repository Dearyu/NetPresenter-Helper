package util;

import bean.ElementBean;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.ArrayList;
import java.util.Objects;

public class PsiUtils {

    private static final String GET = "retrofit2.http.GET";
    private static final String POST = "retrofit2.http.POST";
    public static String ClsName = "";


    public static PsiElement getPsiElementByEditor(Editor editor, PsiFile psiFile) {
        if (editor == null || psiFile == null) {
            return null;
        }
        CaretModel caret = editor.getCaretModel();
        PsiElement psiElement = psiFile.findElementAt(caret.getOffset());
        if (psiElement != null) {
            return psiElement;
        }
        return null;
    }

    public static PsiFile getFileByName(PsiElement psiElement, String fileName) {
        Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(psiElement);
        if (moduleForPsiElement != null) {
            GlobalSearchScope searchScope = GlobalSearchScope.moduleScope(moduleForPsiElement);
            Project project = psiElement.getProject();
            PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, searchScope);
            if (psiFiles.length != 0) {
                return psiFiles[0];
            }
        }
        return null;
    }

    public static PsiClass getClassByClassFile(PsiFile classFile) {
        GlobalSearchScope globalSearchScope = GlobalSearchScope.fileScope(classFile);
        String fullName = classFile.getName();
        String className = fullName.split("\\.")[0];
        ClsName = className;
        return PsiShortNamesCache.getInstance(classFile.getProject()).getClassesByName(className, globalSearchScope)[0];
    }

    public static ArrayList<ElementBean> getMethodsName(PsiClass cls) {
        ArrayList<ElementBean> beans = new ArrayList<>();
        for (PsiMethod method : cls.getMethods()) {
            if (isRetorfitServiceMethod(method)) {
                ElementBean bean = new ElementBean();
                bean.setCheck(true);
                bean.setMethodTag(method.getName());
                bean.setObjType(extractMessage(Objects.requireNonNull(method.getReturnTypeElement()).getText()));
                beans.add(bean);
            }
        }
        return beans;
    }

    public static PsiClass getPsiClassByEditor(Editor editor, PsiFile psiFile) {
        PsiElement psiElement = getPsiElementByEditor(editor, psiFile);
        if (editor != null && psiFile != null && psiElement != null) {
            String name = String.format("%s.java", psiElement.getText());
            PsiFile rootFile = PsiUtils.getFileByName(psiElement, name);
            if (null != rootFile) {
                return PsiUtils.getClassByClassFile(rootFile);
            }
        }
        return null;
    }

    private static boolean isRetorfitServiceMethod(PsiMethod psiMethod) {
        for (PsiAnnotation annotation : psiMethod.getAnnotations())
            if (GET.equals(annotation.getQualifiedName()) || POST.equals(annotation.getQualifiedName())) return true;
        return false;
    }

    public static boolean isRetorfitService(PsiClass cls) {
        for (PsiMethod method : cls.getMethods())
            if (isRetorfitServiceMethod(method)) return true;
        return false;
    }

    public static String extractMessage(String msg) {
        if (isEmpty(msg)) {
            return "";
        }
        return msg.substring(msg.indexOf('<') + 1, msg.length() - 1);
    }

    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }
}
