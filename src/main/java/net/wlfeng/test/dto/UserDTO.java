package net.wlfeng.test.dto;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

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

    @Min(value = 0, message = "年龄信息不正确")
    @Max(value = 100, message = "年龄信息不正确")
    private Integer age;

    @Email(message = "邮箱格式不正确")
    private String email;
    
}
