package net.wlfeng.test.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dal.domain.BankCode;
import net.wlfeng.test.dto.AliBankInfoDTO;
import net.wlfeng.test.dto.FileDTO;
import net.wlfeng.test.util.AliBankUtils;
import net.wlfeng.test.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author weilingfeng
 * @date 2020/9/22 17:24
 * @description
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BankCodeServiceTest {

    @Autowired
    private BankCodeService bankCodeService;

    @Autowired
    private OssService ossService;

    @Test
    public void bankInfoTest() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        String file = "C:\\Users\\86176\\Desktop\\bankInfo-master\\银行信息\\index.json";
        JSONObject json = JSON.parseObject(JSONUtils.readJsonFile(file));

        List<BankCode> bankCodeList = bankCodeService.selectList(new EntityWrapper<BankCode>());
        for (BankCode bankCode: bankCodeList) {
            if (StringUtils.isBlank(bankCode.getBank())) {
                AliBankInfoDTO bankInfo = AliBankUtils.queryCardBankInfo(bankCode.getBankNumber());
                String bankName = json.getString(bankInfo.getBank());
                bankCode.setBank(bankInfo.getBank());
                bankCode.setBankType(bankInfo.getCardType());
                bankCode.setBankName(bankName);
                bankCodeService.updateById(bankCode);
            }
        }
    }

    @Test
    public void bankCodeTest() {
        String file = "C:\\Users\\86176\\Desktop\\bankInfo-master\\银行信息\\bank.json";
        JSONObject json = JSON.parseObject(JSONUtils.readJsonFile(file));

        List<BankCode> bankCodeList = bankCodeService.selectList(new EntityWrapper<BankCode>());
        for (BankCode bankCode: bankCodeList) {
            if (StringUtils.isBlank(bankCode.getBankCode()) && StringUtils.isNotBlank(bankCode.getBankLogo())) {
                for (Map.Entry<String, Object> entry: json.entrySet()) {
                    JSONObject value = (JSONObject) JSONObject.toJSON(entry.getValue());
                    if (bankCode.getBankName().equals(value.getString("name"))) {
                        bankCode.setBankCode(entry.getKey());
                        bankCodeService.updateById(bankCode);
                        break;
                    }
                }
            }
        }
    }

    @Test
    public void uploadLogTest() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        byte[] imgBytes = AliBankUtils.queryBankLogo("NCB");
        ossService.upload(imgBytes, "NCB.png");
    }

    @Test
    public void bankLogoTest() {
        List<BankCode> bankCodeList = bankCodeService.selectList(new EntityWrapper<BankCode>());
        String logo_prefix = "bank/logo/";
        for (BankCode bankCode: bankCodeList) {
            if (StringUtils.isBlank(bankCode.getBankLogo())) {
                String logo = logo_prefix + bankCode.getBankName() + ".png";
                FileDTO fileDTO = ossService.get(logo);
                if (Objects.nonNull(fileDTO)) {
                    bankCode.setBankLogo(fileDTO.getUrl());
                    bankCodeService.updateById(bankCode);
                }
            }
        }
    }

}
