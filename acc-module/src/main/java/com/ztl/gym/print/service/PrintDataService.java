package com.ztl.gym.print.service;

import com.ztl.gym.print.domain.PrintData;

import java.util.List;
import java.util.Map;

public interface PrintDataService {

    List<PrintData> selectPrintDataList(PrintData printData);

    List<PrintData> getData(String line);

    int updatePrint(Long boxCode);

    PrintData getPrintBoxData(Map<String, Object> params);

    List<PrintData> selectPrintDataExport(PrintData printData);
}
