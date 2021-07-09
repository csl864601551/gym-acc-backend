package com.ztl.gym.print.service;

import com.ztl.gym.print.domain.PrintData;

import java.util.List;

public interface PrintDataService {
    List<PrintData> getData();

    int updatePrint(Long boxCode);
}
