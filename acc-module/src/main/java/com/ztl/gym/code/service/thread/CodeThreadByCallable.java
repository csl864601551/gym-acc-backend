package com.ztl.gym.code.service.thread;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.code.service.impl.CodeServiceImpl;
import com.ztl.gym.common.utils.bean.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author : wujinhao
 * @Description: TODO
 * @date Date : 2021年08月12日 15:16
 */
public class CodeThreadByCallable implements Callable<Integer> {


    private ICodeService codeService;

    List<Code> list;

    public CodeThreadByCallable(List<Code> list) {
        this.list = list;
        codeService = SpringUtil.getBean(ICodeService.class);
    }

    @Override
    public Integer call() throws Exception {
        List<Code> codes = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            codes.add(list.get(i));
            //单码5000条数据预处理一下
            if (codes.size() == 20000) {
                codeService.executeBatchInsertCode(codes);
                codes.clear();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (codes.size() > 0) {
            codeService.executeBatchInsertCode(codes);
        }
        return 1;
    }

}
