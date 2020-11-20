package net.wlfeng.test.dto;

import lombok.Data;

import java.util.List;

/**
 * @author weilingfeng
 * @date 2020/10/13 17:54
 * @description
 */
@Data
public class AliBankInfoDTO {

    private String cardType;

    private String bank;

    private String key;

    private List<String> messages;

    private String validated;

    private String stat;
}
