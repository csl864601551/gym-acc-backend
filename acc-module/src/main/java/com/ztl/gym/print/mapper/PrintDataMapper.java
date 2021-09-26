package com.ztl.gym.print.mapper;

import com.ztl.gym.print.domain.PrintData;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface PrintDataMapper {
    List<PrintData> selectPrintData(@RequestParam("line") String line);

    int updatePrint(@RequestParam("id") Long id);

    PrintData getPrintBoxData(Map<String, Object> params);
}
