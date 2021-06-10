package com.ztl.gym.common.utils;

import com.ztl.gym.common.constant.AccConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物流箱拖码 规则工具类
 */
@Component
public class CodeRuleUtils {
    /**
     * 企业生码自增数REDIS KEY前缀
     */
    public final static String COMPANY_CODE_INDEX_PREFIX = "code_index_";

    /**
     * 箱码前缀
     */
    public final static String CODE_PREFIX_B = "B";
    /**
     * 单码前缀
     */
    public final static String CODE_PREFIX_S = "S";

    /**
     * 生码规则前缀对应转换值
     */
    private static Map<String, Integer> CODE_PREFIX_MAP = new HashMap<>();

    static {
        //托码
        CODE_PREFIX_MAP.put("T", 10);
        //箱码
        CODE_PREFIX_MAP.put("B", 20);
        //单码
        CODE_PREFIX_MAP.put("S", 30);
    }


    @Autowired
    public StringRedisTemplate redisTemplate;
    public static StringRedisTemplate myRedisTemplate;

    @PostConstruct
    private void setService() {
        myRedisTemplate = this.redisTemplate;
    }

    /**
     * 创建码 【生码规则：码类型前缀转换值+企业id+日期+码自增数 】
     *
     * @return
     */
    public static String buildCode(Long companyId, String codePrefix, Long codeIndex) {
        String code = "";
        if (companyId != null && StringUtils.isNotBlank(codePrefix) && codeIndex != null) {
            //注意：客户扫码时没办法知道码所属企业，无法从对应分表查询，这里设置规则的时候需要把企业id带进去
            //企业id转换
            long companyIdComplex = companyId * 5;
            code = CODE_PREFIX_MAP.get(codePrefix) + "-" + companyIdComplex + "-" + DateUtils.dateTimeNow() + codeIndex;
        }
        return code;
    }


    /**
     * 根据码获得该码类型
     *
     * @param code
     * @return
     */
    public static String getCodeType(String code) {
        String[] codes = code.split("/");
        if (codes.length > 1) {
            String prefix = codes[0];
            String prefixStr = prefix.substring(0, 1);
            if (prefixStr.equals("P")) {
                return AccConstants.CODE_TYPE_BOX;
            } else if (prefixStr.equals("S")) {
                return AccConstants.CODE_TYPE_SINGLE;
            }
        }
        return AccConstants.CODE_TYPE_ERROR;
    }

    /**
     * 根据码获得该码所属企业id
     *
     * @param code
     * @return
     */
    public static Long getCompanyIdByCode(String code) {
        String[] codes = code.split("-");
        if (codes.length > 1) {
            String prefix = codes[1];
            return Long.parseLong(prefix) / 5;
        }
        return 0L;
    }

    /**
     * 根据企业id查询企业最新生码自增数，同时更新自增数
     *
     * @param companyId      企业id
     * @param increment      需要增长的数量
     * @param codeTypePrefix 码类型
     * @return 返回起始流水号-结束流水号
     */
    public static String getCodeIndex(long companyId, long boxCount, long increment, String codeTypePrefix) {
        List<String> keys = new ArrayList<>();
        keys.add(COMPANY_CODE_INDEX_PREFIX + companyId);
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/codeIndex.lua")));
        redisScript.setResultType(String.class);

        int codeType = 0;
        if (codeTypePrefix.equals(CODE_PREFIX_B)) {
            codeType = 1;
        }
        return myRedisTemplate.execute(redisScript, keys, String.valueOf(boxCount), String.valueOf(increment), String.valueOf(codeType));
    }


    public static void main(String[] args) {
        String code = buildCode(286L, "P", 1L);
        System.out.println(code);
        System.out.println("companyId : " + getCompanyIdByCode(code));
    }
}
