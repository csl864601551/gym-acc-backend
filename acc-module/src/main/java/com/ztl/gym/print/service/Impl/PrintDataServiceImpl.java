package com.ztl.gym.print.service.Impl;

import com.ztl.gym.print.domain.PrintData;
import com.ztl.gym.print.mapper.PrintDataMapper;
import com.ztl.gym.print.service.PrintDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrintDataServiceImpl implements PrintDataService {
    @Autowired
    private PrintDataMapper printDataMapper;

    @Override
    public List<PrintData> getData() {
        return printDataMapper.selectPrintData();
    }

    @Override
    public int updatePrint(Long id) {
        return printDataMapper.updatePrint(id);
    }
}
