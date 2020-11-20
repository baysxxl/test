package net.wlfeng.test.dal.domain;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

@Data
@TableName("bank_code")
public class BankCode {

	@TableId(type = IdType.AUTO)
	private Long id;

    @TableField("bank_type")
    private String bankType;

    @TableField("bank_code")
    private String bankCode;

    @TableField("bank")
    private String bank;

    @TableField("bank_name")
    private String bankName;

    @TableField("bank_number")
    private String bankNumber;

    @TableField("bank_logo")
    private String bankLogo;

}
