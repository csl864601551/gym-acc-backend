package com.ztl.gym.print.service;

import com.ztl.gym.print.domain.PrintData;

import java.util.List;
import java.util.Map;

public interface PrintDataService {
    List<PrintData> getData(String line);

    int updatePrint(Long boxCode);
}
