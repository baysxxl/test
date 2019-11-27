package net.wlfeng.test.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author weilingfeng
 * @date 2019/10/29 14:31
 * @description
 */
@Data
public class UserExcelDTO implements Serializable {
    @Excel(name = "id", orderNum = "0")
    private Long id;
    @Excel(name = "姓名", orderNum = "1")
    private String name;
    @Excel(name = "年龄", orderNum = "2")
    private Integer age;
    @Excel(name = "邮箱", orderNum = "3")
    private String email;
}
