package com.ztl.gym.idis.service;

import com.ztl.gym.idis.domain.IdisRecord;
import com.ztl.gym.idis.prop.IdisProp;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * IDIS同步Service接口
 *
 * @author zt_sly
 * @date 2021-07-21
 */
public interface IIdisService {

    /**
     * 同步标识至自建企业节点
     * @param maxNum 最大同步数量
     * @return 实际同步数量
     */
    Integer syncCode(Integer maxNum, IdisProp idisProp);

    /**
     * 执行一次同步标识请求
     * @param codeInfo 标识属性信息
     * @return 同步结果
     */
    CompletableFuture<IdisRecord> doOnceSyncCode(Map<String, String> codeInfo,IdisProp idisProp);

}
