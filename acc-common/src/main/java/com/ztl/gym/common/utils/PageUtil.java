package com.ztl.gym.common.utils;

import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.core.page.PageDomain;
import com.ztl.gym.common.core.page.TableDataInfo;
import com.ztl.gym.common.core.page.TableSupport;

import java.util.List;

/**
 * 自定义List分页工具
 *
 * @author xuyudian
 */
public class PageUtil {

    /**
     * 开始分页
     *
     * @param list
     * @return
     */
    public static TableDataInfo startPage(List list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();//pageNum  页码
        Integer pageSize = pageDomain.getPageSize();//pageSize 每页多少条数据
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=5000000;
        }

        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        Integer pageCount = 0; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引

        if (pageNum != pageCount) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setTotal(list.size());


        // 如果toIndex大于List的总条数 会出现 java.lang.IndexOutOfBoundsException:错误
        if (toIndex > list.size()) {
            List pageList = list.subList(fromIndex, list.size());
            rspData.setRows(pageList);
            return rspData;
        }

        List pageList = list.subList(fromIndex, toIndex);
        rspData.setRows(pageList);
        return rspData;
    }
}
