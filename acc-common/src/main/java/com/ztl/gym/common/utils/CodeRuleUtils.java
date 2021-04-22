package com.ztl.gym.common.utils;

import com.ztl.gym.common.constant.AccConstants;

/**
 * 物流箱拖码 规则工具类
 */
public class CodeRuleUtils {

    /**
     * 判断单号类型【属于箱码、单码还是流转单号】
     *
     * @param text
     * @return
     */
    public static int judgeCode(String text) {
        if (StringUtils.isNotBlank(text)) {
            if (text.startsWith("P")) {
                return AccConstants.NO_TYPE_CODE_P;
            } else if (text.startsWith("S")) {
                return AccConstants.NO_TYPE_CODE_S;
            } else if (text.startsWith("RK")) {
                return AccConstants.NO_TYPE_STORAGE_IN;
            } else if (text.startsWith("CK")) {
                return AccConstants.NO_TYPE_STORAGE_OUT;
            } else if (text.startsWith("DB")) {
                return AccConstants.NO_TYPE_STORAGE_TRANSFER;
            } else if (text.startsWith("TH")) {
                return AccConstants.NO_TYPE_STORAGE_BACK;
            } else {
                return AccConstants.NO_TYPE_UNKONW;
            }
        } else {
            return AccConstants.NO_TYPE_UNKONW;
        }
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
        String[] codes = code.split("/");
        if (codes.length > 1) {
            String prefix = codes[0];
            return Long.parseLong(prefix.substring(1));
        }
        return 0L;
    }
}
