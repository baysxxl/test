package net.wlfeng.test.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dal.domain.User;
import net.wlfeng.test.dto.UserExcelDTO;
import net.wlfeng.test.service.UserService;
import net.wlfeng.test.util.EasyPoiUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weilingfeng
 * @date 2019/10/29 10:14
 * @description
 */
@Slf4j
@Controller
@RequestMapping("excel")
public class ExcelTestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            //模拟从数据库获取需要导出的数据
            List<User> userList = userService.selectList(new EntityWrapper<>());
            List<UserExcelDTO> userExcelDtosList = new ArrayList<>(userList.size());
            for (User user: userList) {
                UserExcelDTO userExcelDto = new UserExcelDTO();
                BeanUtils.copyProperties(user, userExcelDto);
                userExcelDtosList.add(userExcelDto);
            }
            EasyPoiUtils.exportExcel(UserExcelDTO.class, userExcelDtosList, "用户列表.xls", response);
        } catch (Exception e) {
            log.error("===导出excel异常，异常信息:{}===", e.getMessage());
        }
    }
}
