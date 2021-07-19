package com.ztl.gym.web.controller.print;

import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.print.domain.PrintData;
import com.ztl.gym.print.service.PrintDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boxCode/print")
public class PrintDataController {
    @Autowired
    public PrintDataService printDataService;

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/getData")
    public AjaxResult getData() {
        try {
            List<PrintData> lsPrint =  printDataService.getData();
            return AjaxResult.success(lsPrint);
        } catch (Exception err) {
            return AjaxResult.error(err.toString());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updatePrintStatus")
    public AjaxResult updatePrint(@RequestParam(value = "id") Long id) {
        try {
            int count = printDataService.updatePrint(id);
            return AjaxResult.success(count);
        } catch (Exception err) {
            return AjaxResult.error(err.toString());
        }
    }
}
