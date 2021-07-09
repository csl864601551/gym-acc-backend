package com.ztl.gym.print.mapper;

import com.ztl.gym.print.domain.PrintData;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PrintDataMapper {
    List<PrintData> selectPrintData();

    int updatePrint(@RequestParam("id") Long id);
}
