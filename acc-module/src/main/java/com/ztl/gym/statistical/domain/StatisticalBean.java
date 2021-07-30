package com.ztl.gym.statistical.domain;


import com.ztl.gym.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 *
 * @author ruoyi
 */
public class StatisticalBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 企业总数 */
    private int qyNum;
    /** 产品出货总量 */
    private int cpchlNum;
    /** 窜货总量 */
    private int chzlNum;
    /** 生码总量 */
    private int smzlNum;
    /** 累计充值费用 */
    private BigDecimal ljczfyNum;
    /** 经销商总数 */
    private int jxsNum;
    /** 剩余码量 */
    private int symlNum;
    /** 剩余费用 */
    private BigDecimal syfyNum;

    //产品总览
    /** 已下架 */
    private int yxjNum;
    /** 已上架 */
    private int ysjNum;
    /** 库存紧张 */
    private int kcjzNum;
    /** 全部产品 */
    private int qbcpNum;

    //经销商总览
    /** 今日新增 */
    private int jrxzNum;
    /** 昨日新增 */
    private int zrxzNum;
    /** 本月新增 */
    private int byxzNum;
    /** 经销商总数 */
    private int jxszs;

    /** 本月出货总数 */
    private int bychzsNum;
    /** 同比上月 */
    private BigDecimal tbsyNum;
    /** 本周出货数量 */
    private int bzchslNum;
    /** 同比上周 */
    private BigDecimal tbszNum;


    /** 今日生码总量 */
    private int jrsmNum;
    /** 累计生码总量 */
    private int ljsmNum;
    /** 今日扫码总量 */
    private int jrsmZl;
    /** 累计扫码总量 */
    private int ljsmZl;
    /** 今日查验总量 */
    private int jrcyNum;
    /** 累计查验总量 */
    private int ljcyNum;


    public int getQyNum() {
        return qyNum;
    }

    public void setQyNum(int qyNum) {
        this.qyNum = qyNum;
    }

    public int getCpchlNum() {
        return cpchlNum;
    }

    public void setCpchlNum(int cpchlNum) {
        this.cpchlNum = cpchlNum;
    }

    public int getChzlNum() {
        return chzlNum;
    }

    public void setChzlNum(int chzlNum) {
        this.chzlNum = chzlNum;
    }

    public int getSmzlNum() {
        return smzlNum;
    }

    public void setSmzlNum(int smzlNum) {
        this.smzlNum = smzlNum;
    }

    public BigDecimal getLjczfyNum() {
        return ljczfyNum;
    }

    public void setLjczfyNum(BigDecimal ljczfyNum) {
        this.ljczfyNum = ljczfyNum;
    }

    public int getJxsNum() {
        return jxsNum;
    }

    public void setJxsNum(int jxsNum) {
        this.jxsNum = jxsNum;
    }

    public int getSymlNum() {
        return symlNum;
    }

    public void setSymlNum(int symlNum) {
        this.symlNum = symlNum;
    }

    public BigDecimal getSyfyNum() {
        return syfyNum;
    }

    public void setSyfyNum(BigDecimal syfyNum) {
        this.syfyNum = syfyNum;
    }

    public int getYxjNum() {
        return yxjNum;
    }

    public void setYxjNum(int yxjNum) {
        this.yxjNum = yxjNum;
    }

    public int getYsjNum() {
        return ysjNum;
    }

    public void setYsjNum(int ysjNum) {
        this.ysjNum = ysjNum;
    }

    public int getKcjzNum() {
        return kcjzNum;
    }

    public void setKcjzNum(int kcjzNum) {
        this.kcjzNum = kcjzNum;
    }

    public int getQbcpNum() {
        return qbcpNum;
    }

    public void setQbcpNum(int qbcpNum) {
        this.qbcpNum = qbcpNum;
    }

    public int getJrxzNum() {
        return jrxzNum;
    }

    public void setJrxzNum(int jrxzNum) {
        this.jrxzNum = jrxzNum;
    }

    public int getZrxzNum() {
        return zrxzNum;
    }

    public void setZrxzNum(int zrxzNum) {
        this.zrxzNum = zrxzNum;
    }

    public int getByxzNum() {
        return byxzNum;
    }

    public void setByxzNum(int byxzNum) {
        this.byxzNum = byxzNum;
    }

    public int getJxszs() {
        return jxszs;
    }

    public void setJxszs(int jxszs) {
        this.jxszs = jxszs;
    }

    public int getBychzsNum() {
        return bychzsNum;
    }

    public void setBychzsNum(int bychzsNum) {
        this.bychzsNum = bychzsNum;
    }

    public BigDecimal getTbsyNum() {
        return tbsyNum;
    }

    public void setTbsyNum(BigDecimal tbsyNum) {
        this.tbsyNum = tbsyNum;
    }

    public int getBzchslNum() {
        return bzchslNum;
    }

    public void setBzchslNum(int bzchslNum) {
        this.bzchslNum = bzchslNum;
    }

    public BigDecimal getTbszNum() {
        return tbszNum;
    }

    public void setTbszNum(BigDecimal tbszNum) {
        this.tbszNum = tbszNum;
    }

    public int getJrsmNum() {
        return jrsmNum;
    }

    public void setJrsmNum(int jrsmNum) {
        this.jrsmNum = jrsmNum;
    }

    public int getLjsmNum() {
        return ljsmNum;
    }

    public void setLjsmNum(int ljsmNum) {
        this.ljsmNum = ljsmNum;
    }

    public int getJrsmZl() {
        return jrsmZl;
    }

    public void setJrsmZl(int jrsmZl) {
        this.jrsmZl = jrsmZl;
    }

    public int getLjsmZl() {
        return ljsmZl;
    }

    public void setLjsmZl(int ljsmZl) {
        this.ljsmZl = ljsmZl;
    }

    public int getJrcyNum() {
        return jrcyNum;
    }

    public void setJrcyNum(int jrcyNum) {
        this.jrcyNum = jrcyNum;
    }

    public int getLjcyNum() {
        return ljcyNum;
    }

    public void setLjcyNum(int ljcyNum) {
        this.ljcyNum = ljcyNum;
    }

}

