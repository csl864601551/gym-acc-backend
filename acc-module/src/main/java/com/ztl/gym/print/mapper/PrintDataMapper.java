package com.ztl.gym.print.mapper;

import com.ztl.gym.print.domain.PrintData;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface PrintDataMapper {

    /**
     * 查询产量统计列表
     *
     * @param printData 产量统计
     * @return 产量统计集合
     */
    List<PrintData> selectPrintDataList(PrintData printData);

    List<PrintData> selectPrintData(@RequestParam("line") String line);

    int updatePrint(@RequestParam("id") Long id);

    PrintData getPrintBoxData(Map<String, Object> params);

    List<PrintData> selectPrintDataExport(PrintData printData);
}
