package net.wlfeng.test.dto;


import lombok.Data;
import net.wlfeng.test.annotation.EnumValid;
import net.wlfeng.test.enums.GenderEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

	private Long id;

    @NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "年龄信息不能为空")
    @Min(value = 0, message = "年龄信息不正确")
    @Max(value = 100, message = "年龄信息不正确")
    private Integer age;

    @Email(message = "邮箱格式不正确")
    private String email;

    @EnumValid(message = "性别类型不正确", target = GenderEnum.class)
    private Integer gender;

}
