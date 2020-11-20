package net.wlfeng.test.util;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Objects;

/**
 * @author weilingfeng
 * @date 2020/9/22 11:33
 * @description
 */
@Slf4j
public class ScriptUtils {

    private static ScriptEngine jse = null;

    private ScriptUtils(){}

    private static synchronized ScriptEngine getInstance() {
        if (jse == null) {
            jse = new ScriptEngineManager().getEngineByName("JavaScript");
        }
        return jse;
    }

    public static long eval(String mathTemplate, String... params) {
        if (StringUtils.isBlank(mathTemplate)) {
            throw new BizException(-1, "公式计算参数不能为空");
        }
        if (Objects.nonNull(params) && params.length > 0) {
            mathTemplate = String.format(mathTemplate, params);
        }
        try {
            return Long.valueOf(String.valueOf(getInstance().eval(mathTemplate)));
        } catch (Exception e) {
            log.error("===公式计算异常,异常信息:{}===", e);
            throw new BizException(-1, "公式计算异常:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String str = "%s > 5000000 ? 600 : 100";
        long amount = 60000000;
        int round = 10;
        for (int i = 0; i < round; i++) {
            int num = 10000;
            long startTime = System.currentTimeMillis();
            for (int j = 0; j < num; j++) {
                eval(str, String.valueOf(amount));
            }
            System.out.println("===end,spend time:" + (System.currentTimeMillis() - startTime));
        }
    }

}
