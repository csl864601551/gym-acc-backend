package com.ztl.gym.quartz.task;

import com.ztl.gym.idis.service.IIdisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * idis数据同步定时任务
 * @author zt_sly
 */
@Slf4j
@Component("idisTask")
public class IdisTask {

    @Autowired
    private IIdisService idisService;

    /**
     * 同步标识至自建企业节点
     * @param maxNum 最大同步数量
     */
    public void syncCode(Integer maxNum) {
        log.info("开始同步自建企业节点, 最大同步数量: {}", maxNum);
        Integer syncNum = idisService.syncCode(maxNum);
        log.info("结束同步自建企业节点, 实际同步数量: {}", syncNum);
    }

}
