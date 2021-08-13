package com.ztl.gym.common.domain;

/**
 * @author : wujinhao
 * @Description: TODO
 * @date Date : 2021年08月11日 14:04
 */
public enum GeneratorEnum {

    /**
     * 码属性表id
     */
    ATTR (1);

    private int type;

    private GeneratorEnum(int value)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }
}
