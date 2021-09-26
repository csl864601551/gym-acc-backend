package com.ztl.gym.print.service.Impl;

import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.print.domain.PrintData;
import com.ztl.gym.print.mapper.PrintDataMapper;
import com.ztl.gym.print.service.PrintDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrintDataServiceImpl implements PrintDataService {
    @Autowired
    private CommonService commonService;

    @Autowired
    private PrintDataMapper printDataMapper;

    @Override
    public List<PrintData> getData(String line) {
        return printDataMapper.selectPrintData(line);
    }

    @Override
    public int updatePrint(Long id) {
        return printDataMapper.updatePrint(id);
    }

    @Override
    public PrintData getPrintBoxData(Map<String, Object> params) {
        return printDataMapper.getPrintBoxData(params);
    }
}
