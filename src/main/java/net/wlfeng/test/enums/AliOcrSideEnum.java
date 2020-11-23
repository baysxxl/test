package net.wlfeng.test.enums;

/**
 * @author weilingfeng
 * @date 2019/11/18 11:10
 * @description 阿里ocr识别side(卡面正反)字段枚举
 */
public enum AliOcrSideEnum {

    FACE("face", "人像面"),
    BACK("back", "国徽面");

    private String code;
    private String remark;

    AliOcrSideEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public static boolean isFace(String side) {
        return AliOcrSideEnum.FACE.getCode().equals(side);
    }

    public static String getSideCode(boolean isFace) {
        return isFace ? AliOcrSideEnum.FACE.getCode() : AliOcrSideEnum.BACK.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
