package net.wlfeng.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dal.dao.BankCodeMapper;
import net.wlfeng.test.dal.domain.BankCode;
import net.wlfeng.test.service.BankCodeService;
import org.springframework.stereotype.Service;

/**
 * @author weilingfeng
 * @date 2019/11/27 14:58
 * @description
 */
@Slf4j
@Service
public class BankCodeServiceImpl extends ServiceImpl<BankCodeMapper, BankCode> implements BankCodeService {

}
