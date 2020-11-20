package net.wlfeng.test.enums;

/**
 * @author weilingfeng
 * @date 2020/8/12 10:44
 * @description 性别枚举
 */
public enum GenderEnum {

    MAN(1, "男"),
    WOMAN(2, "女");

    private Integer code;
    private String name;

    GenderEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
