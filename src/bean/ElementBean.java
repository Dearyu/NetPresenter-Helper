package bean;

/**
 * @version 1.0
 * @author: liujunwei
 * @date: 2021/3/23
 * @description:
 */
public class ElementBean {

    private boolean isCheck;

    private String methodTag;

    private String objType;

    public String getObjType() {
        return objType;
    }

    public void setObjType(String objType) {
        this.objType = objType;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getMethodTag() {
        return methodTag;
    }

    public void setMethodTag(String methodTag) {
        this.methodTag = methodTag;
    }

    @Override
    public String toString() {
        return "ElementBean{" +
                "isCheck=" + isCheck +
                ", methodTag='" + methodTag + '\'' +
                '}';
    }
}
