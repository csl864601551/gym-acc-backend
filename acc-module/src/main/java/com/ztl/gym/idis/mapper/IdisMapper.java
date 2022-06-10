package com.ztl.gym.idis.mapper;

import com.ztl.gym.idis.domain.IdisRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * IDIS同步Mapper接口
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Repository
public interface IdisMapper {

    /**
     * 查询未同步的码和绑定的属性
     * @param maxNum 查询的最大数量
     * @return 结果集
     */
    List<Map<String, String>> selectCodeNotSynced(Integer maxNum);

    /**
     * 给成功同步的码更新状态
     * @param codeList 需要更新状态的码
     */
    void updateStatusForSyncedCode(List<String> codeList);

}
