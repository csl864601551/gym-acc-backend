package com.ztl.gym.storage.service.impl;

import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.service.ICodeAttrService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.annotation.DataSource;
import com.ztl.gym.common.constant.AccConstants;
import com.ztl.gym.common.core.domain.entity.SysDept;
import com.ztl.gym.common.core.domain.model.LoginUser;
import com.ztl.gym.common.enums.DataSourceType;
import com.ztl.gym.common.exception.BaseException;
import com.ztl.gym.common.exception.CustomException;
import com.ztl.gym.common.service.CommonService;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.DateUtils;
import com.ztl.gym.common.utils.SecurityUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.product.service.IProductStockService;
import com.ztl.gym.storage.domain.*;
import com.ztl.gym.storage.domain.vo.FlowVo;
import com.ztl.gym.storage.domain.vo.StorageVo;
import com.ztl.gym.storage.mapper.StorageMapper;
import com.ztl.gym.storage.service.*;
import com.ztl.gym.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 仓库Service业务层处理
 *
 * @author zhucl
 * @date 2021-04-13
 */
@Service
public class StorageServiceImpl implements IStorageService {
    @Autowired
    private StorageMapper storageMapper;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ICodeAttrService codeAttrService;
    @Autowired
    private IStorageInService storageInService;
    @Autowired
    private IStorageOutService storageOutService;
    @Autowired
    private IStorageTransferService storageTransferService;
    @Autowired
    private IStorageBackService storageBackService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IProductStockService productStockService;

    /**
     * 查询仓库
     *
     * @param id 仓库ID
     * @return 仓库
     */
    @Override
    public Storage selectStorageById(Long id) {
        return storageMapper.selectStorageById(id);
    }

    /**
     * 查询仓库列表
     *
     * @param storage 仓库
     * @return 仓库
     */
    @Override
    public List<Storage> selectStorageList(Storage storage) {
        LoginUser user = SecurityUtils.getLoginUser();
        SysDept dept = user.getUser().getDept();
        Long deptId = dept.getDeptId();
        //判断是否为平台
        if (!deptId.equals(AccConstants.ADMIN_DEPT_ID)) {
            String ancestors = dept.getAncestors();
            int count = (ancestors.length() - ancestors.replace(",", "").length()) / ",".length();
            if (count == 1) {
                if(storage.getCompanyId()==null){
                    storage.setCompanyId(SecurityUtils.getLoginUserTopCompanyId());
                }
                //storage.setLevel(AccConstants.STORAGE_LEVEL_COMPANY);
            }
            if(storage.getTenantId()==null){
                storage.setTenantId(deptId);
            }
                //storage.setLevel(AccConstants.STORAGE_LEVEL_TENANT);


            //FIXME
//            Map<String, Object> params = new HashMap<>();
//            params.put("deptId", SecurityUtils.getLoginUserTopCompanyId());
//            storage.setParams(params);
        }
        storage.setStatus(AccConstants.STORAGE_DELETE_NO);
        return storageMapper.selectStorageList(storage);
    }

    /**
     * 查询仓库列表
     *
     * @param storage 仓库
     * @return 仓库
     */
    public Integer countStorage(Storage storage) {
        if (storage.getTenantId() == null || storage.getTenantId().toString().equals("0")) {
            storage.setTenantId(commonService.getTenantId());
        }
        return storageMapper.countStorage(storage);
    }

    /**
     * 新增仓库
     *
     * @param storage 仓库
     * @return 结果
     */
    @Override
    public int insertStorage(Storage storage) {
        if (null == storage.getStorageNo()) {
            throw new BaseException("仓库编号不能为空！");
        }
        Storage queryStorage = new Storage();
        queryStorage.setTenantId(storage.getTenantId());
        queryStorage.setStorageNo(storage.getStorageNo());
        queryStorage.setStatus(AccConstants.STORAGE_DELETE_NO);
        Integer storageCount = this.countStorage(queryStorage);
        if (storageCount > 0) {
            throw new BaseException("仓库编号已重复，请重新命名");
        }

        LoginUser user = SecurityUtils.getLoginUser();
        SysDept dept = user.getUser().getDept();
        Long deptId = dept.getDeptId();
        //判断是否为平台
        if (!deptId.equals(AccConstants.ADMIN_DEPT_ID)) {
            Long companyId;
            Long tenantId;
            if (storage.getCompanyId() == null) {
                companyId = SecurityUtils.getLoginUserTopCompanyId();
                storage.setCompanyId(companyId);
            } else {
                companyId = storage.getCompanyId();
            }
            if (storage.getTenantId() == null) {
                tenantId = SecurityUtils.getLoginUserCompany().getDeptId();
                storage.setTenantId(tenantId);
            } else {
                tenantId = storage.getTenantId();
            }
            if (!tenantId.equals(companyId)) {
                storage.setLevel(AccConstants.STORAGE_LEVEL_TENANT);
            } else {
                storage.setLevel(AccConstants.STORAGE_LEVEL_COMPANY);
            }
        }
        storage.setStatus(0L);
        storage.setCreateTime(DateUtils.getNowDate());
        storage.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return storageMapper.insertStorage(storage);
    }

    /**
     * 修改仓库
     *
     * @param storage 仓库
     * @return 结果
     */
    @Override
    public int updateStorage(Storage storage) {
        Storage queryStorage = new Storage();
        queryStorage.setId(storage.getId());
        queryStorage.setStorageNo(storage.getStorageNo());
        queryStorage.setStatus(AccConstants.STORAGE_DELETE_NO);
        Integer storageCount = this.countStorage(queryStorage);
        if (storageCount > 0) {
            throw new BaseException("仓库编号已重复，请重新命名");
        }

        storage.setUpdateTime(DateUtils.getNowDate());
        storage.setUpdateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        return storageMapper.updateStorage(storage);
    }

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库ID
     * @return 结果
     */
    @Override
    public int deleteStorageByIds(Long[] ids) {
        return storageMapper.deleteStorageByIds(ids);
    }

    /**
     * 删除仓库信息
     *
     * @param id 仓库ID
     * @return 结果
     */
    @Override
    public int deleteStorageById(Long id) {
        return storageMapper.deleteStorageById(id);
    }

    /**
     * 根据用户查询仓库
     *
     * @param storage
     * @return
     */
    @Override
    public List<Storage> selectStorageByUser(Storage storage) {
        return storageMapper.selectStorageByUser(storage);
    }

    /**
     * 查询码信息 【包含码属性、产品、批次】
     *
     * @param codeVal
     * @return
     */
    @Override
    public StorageVo selectLastStorageByCode(String codeVal) {
        StorageVo storageVo = new StorageVo(); // 查询码的最新物流信息

        Code code = new Code();
        code.setCode(codeVal);
        Long companyId = CodeRuleUtils.getCompanyIdByCode(codeVal);
        if (companyId > 0) {
            code.setCompanyId(companyId);
            Code codeEntity = codeService.selectCode(code);
            if (codeEntity != null) {
                storageVo.setCode(codeVal);//区分前端是否查询到码相关信息
                storageVo.setCompanyId(companyId);
                if (codeEntity.getCodeType().toString().equals("single")) {//判断单码是属于单码or箱码
                    if (codeEntity.getpCode() == null) {
                        storageVo.setCodeTypeName("单码");
                    } else {
                        storageVo.setCodeTypeName("箱码");
                        storageVo.setpCode(codeEntity.getpCode());
                    }
                } else if (codeEntity.getCodeType().toString().equals("box")) {
                    storageVo.setpCode(codeEntity.getCode());
                    storageVo.setCodeTypeName("箱码");
                }
                //判断是否码是否绑定了产品
                if (codeEntity.getCodeAttr().getProductId() != null) {
                    storageVo.setProductId(codeEntity.getCodeAttr().getProductId());//产品ID
                    storageVo.setProductNo(codeEntity.getCodeAttr().getProductNo());//规格型号
                    storageVo.setProductName(codeEntity.getCodeAttr().getProduct().getProductName());//物料名称
                    storageVo.setBatchId(codeEntity.getCodeAttr().getBatchId());//产品批次ID
                    storageVo.setBatchNo(codeEntity.getCodeAttr().getBatchNo());//产品批次
                }
                storageVo.setRecordId(codeEntity.getCodeAttr().getRecordId());//码记录表ID
                storageVo.setInNo(commonService.getStorageNo(AccConstants.STORAGE_TYPE_IN));//企业入库单号

                Integer storageType = codeEntity.getStorageType();
                Long storageRecordId = codeEntity.getStorageRecordId();
                if (storageType != null && storageRecordId != 0) {
                    if (storageType == AccConstants.STORAGE_TYPE_IN) {
                        StorageIn storageIn = storageInService.selectStorageInById(storageRecordId);
                        if (storageIn != null) {
                            storageVo.setOutNo(commonService.getStorageNo(AccConstants.STORAGE_TYPE_OUT));//企业第一次出库
                            storageVo.setExtraNo(storageIn.getInNo());
                            storageVo.setFromStorageId(storageIn.getToStorageId());
                        }
                    } else if (storageType == AccConstants.STORAGE_TYPE_OUT) {
                        StorageOut storageOut = storageOutService.selectStorageOutById(storageRecordId);
                        storageVo.setOutNo(storageOut.getOutNo());
                        storageVo.setOutTime(storageOut.getOutTime());
                    } else if (storageType == AccConstants.STORAGE_TYPE_BACK) {
                        StorageBack storageBack = storageBackService.selectStorageBackById(storageRecordId);
                    } else if (storageType == AccConstants.STORAGE_TYPE_TRANSFER) {
                        StorageTransfer storageTransfer = storageTransferService.selectStorageTransferById(storageRecordId);
                    }
                }
            }
        }
        List<String> list=new ArrayList<>();
        list.add(codeVal);
        storageVo.setNum(codeService.getCodesCount(list));
        return storageVo;
    }

    /**
     * 新增码流转明细-套标 【1.新增码流转明细 2.修改码属性codeAttr中最新流转信息 3.更新对应企业经销商产品库存】
     *
     * @return
     * @remark 【注意】该接口指针对套标，如果传值单码含有箱码，会自动关联整箱，单个退货不能调用此接口
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataSource(DataSourceType.SHARDING)
    public int addCodeFlow(int storageType, long storageRecordId, String code) {
        //查询码类型与企业id
        String codeType = CodeRuleUtils.getCodeType(code);
        Long companyId = CodeRuleUtils.getCompanyIdByCode(code);

        //查询码
        Code codeEntity = new Code();
        Code codeRes = null;
        codeEntity.setCode(code);
        codeEntity.setCompanyId(companyId);
        codeRes = codeService.selectCode(codeEntity);

        //判断该单码有无箱码，如果有则更新整箱
        boolean isBox = false;
        boolean isSingle = false;
        String boxCode = null;
        int insertRes = 0;
        if (codeType.equals(AccConstants.CODE_TYPE_SINGLE)) {
            if (StringUtils.isNotBlank(codeRes.getpCode())) {
                isBox = true;
                boxCode = codeRes.getpCode();
            } else {
                //只更新单码
                isSingle = true;
            }
        } else if (codeType.equals(AccConstants.CODE_TYPE_BOX)) {
            isBox = true;
            boxCode = code;
        }
        //批量插入码明细
        List<FlowVo> insertList = new ArrayList<>();
        if (isBox && StringUtils.isNotBlank(boxCode)) {
            //箱码
            insertList.add(buildFlowParam(companyId, boxCode, storageType, storageRecordId));
            //单码
            Code codeParam = new Code();
            codeParam.setCompanyId(companyId);
            codeParam.setpCode(boxCode);
            List<Code> sonList = codeService.selectCodeList(codeParam);
            for (Code sonCode : sonList) {
                insertList.add(buildFlowParam(companyId, sonCode.getCode(), storageType, storageRecordId));
            }
        } else if (isSingle && StringUtils.isNotBlank(code)) {
            insertList.add(buildFlowParam(companyId, code, storageType, storageRecordId));
        }
        insertRes = codeService.insertCodeFlowForBatchSingle(companyId, storageType, insertList);

        //更新码属性最新物流节点
        int updRes = 0;
        if (insertRes > 0) {
/*            //更新码属性中的最新流转节点信息
            CodeAttr codeAttr = new CodeAttr();
            codeAttr.setId(codeRes.getCodeAttrId());
            //入库或退货入库时需更新码所属企业/经销商
            if (storageType == AccConstants.STORAGE_TYPE_IN) {
                StorageIn storageIn=storageInService.selectStorageInById(storageRecordId);
                codeAttr.setTenantId(storageIn.getTenantId());
            }
            codeAttr.setStorageType(storageType);
            codeAttr.setStorageRecordId(storageRecordId);
            updRes = codeAttrService.updateCodeAttr(codeAttr);*/

            //更新码属性中的最新流转节点信息2
            //入库或退货入库时需更新码所属企业/经销商
            Code codeTemp=new Code();//箱码用
            if (storageType == AccConstants.STORAGE_TYPE_IN) {
                StorageIn storageIn=storageInService.selectStorageInById(storageRecordId);
                codeRes.setTenantId(storageIn.getTenantId());

                codeTemp.setTenantId(storageIn.getTenantId());
            }
            codeTemp.setCompanyId(companyId);
            codeRes.setStorageType(storageType);
            codeRes.setStorageRecordId(storageRecordId);
            if(isBox){
                codeTemp.setStorageType(storageType);
                codeTemp.setStorageRecordId(storageRecordId);
                codeTemp.setpCode(boxCode);
                codeService.updateCodeStorageByPCode(codeTemp);
                codeRes.setCode(boxCode);
            }

            updRes =codeService.updateCodeStorageByCode(codeRes);

        }
        return updRes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addCodeFlows(int storageType, long storageRecordId, List<String> codes) {

        //查询码
        Map<String,Object> codeParam = new HashMap<>();
        Long companyId = CodeRuleUtils.getCompanyIdByCode(codes.get(0));
        codeParam.put("companyId",companyId);
        codeParam.put("codes",codes);
        List<Code> lists= codeService.selectCodeListByCodes(codeParam);

        //批量插入码明细
        List<FlowVo> insertList = new ArrayList<>();
        for (Code code : lists) {
            insertList.add(buildFlowParam(code.getCompanyId(), code.getCode(), storageType, storageRecordId));
        }

        int insertRes = codeService.insertCodeFlowForBatchSingle(companyId, storageType, insertList);

        //更新码属性最新物流节点
        int updRes = 0;
        if (insertRes > 0) {

            //更新码属性中的最新流转节点信息2
            //入库或退货入库时需更新码所属企业/经销商
            if (storageType == AccConstants.STORAGE_TYPE_IN) {
                StorageIn storageIn = storageInService.selectStorageInById(storageRecordId);
                codeParam.put("tenantId",storageIn.getTenantId());
            }
            codeParam.put("companyId",companyId);
            codeParam.put("storageType",storageType);
            codeParam.put("storageRecordId",storageRecordId);
            updRes = codeService.updateCodeStorageByCodes(codeParam);

        }
        return updRes;
    }
    /**
     * 更新产品库存信息
     *
     * @param storageType
     * @param storageRecordId
     * @return
     */
    public void updateProductStock(int storageType, long storageRecordId) {
        Long tenantId = null;
        Long storageId = null;
        Long productId = null;
        Integer flowNum = null;
        if (storageType == AccConstants.STORAGE_TYPE_IN) {
            StorageIn storageIn = storageInService.selectStorageInById(storageRecordId);
            if (storageIn == null) {
                throw new CustomException("更新产品库存时未查询到最新入库单");
            }
            tenantId = storageIn.getTenantId();
            storageId = storageIn.getToStorageId();
            productId = storageIn.getProductId();
            flowNum = Integer.parseInt(String.valueOf(storageIn.getActInNum()));
            // 无仓库，第一步查询是否有仓库，没有直接新建仓库
            if (storageId == null) {
                Storage temp = new Storage();
                temp.setTenantId(storageIn.getTenantId());
                List<Storage> list = selectStorageList(temp);
                if (list.size() > 0) {
                    storageId = list.get(0).getId();
                } else {
                    Storage storage = new Storage();
                    storage.setStorageName("默认仓库");
                    storage.setStorageNo("1");
                    storage.setTenantId(storageIn.getTenantId());
                    insertStorage(storage);
                    storageId = storage.getId();
                }
                storageIn.setToStorageId(storageId);
                storageInService.updateStorageIn(storageIn);
            }

        } else if (storageType == AccConstants.STORAGE_TYPE_OUT) {
            StorageOut storageOut = storageOutService.selectStorageOutById(storageRecordId);
            if (storageOut == null) {
                throw new CustomException("更新产品库存时未查询到最新出库单");
            }
            tenantId = storageOut.getTenantId();
            storageId = storageOut.getFromStorageId();
            productId = storageOut.getProductId();
            if(storageOut.getActOutNum()!=null){
                flowNum = Integer.parseInt(String.valueOf(storageOut.getActOutNum()));
            }else {
                flowNum = Integer.parseInt(String.valueOf(storageOut.getOutNum()));
            }

        } else if (storageType == AccConstants.STORAGE_TYPE_BACK) {
            StorageBack storageBack = storageBackService.selectStorageBackById(storageRecordId);
            if (storageBack == null) {
                throw new CustomException("更新产品库存时未查询到最新退货单");
            }
            tenantId = storageBack.getTenantId();
            storageId = storageBack.getFromStorageId();
            productId = storageBack.getProductId();
            flowNum = Integer.parseInt(String.valueOf(storageBack.getActBackNum()));

        } else {
            throw new CustomException("流转类型异常，无法更新产品库存");
        }

        if (storageId == null || storageId == 0 || productId == null || productId == 0 || flowNum == null || flowNum == 0) {
            throw new CustomException("更新产品库存缺少参数!");
        } else {
            productStockService.insertProductStock(tenantId,storageId, productId, storageType, storageRecordId, flowNum);
        }
    }

    /**
     * 组建码流转明细参数
     *
     * @param companyId
     * @param code
     * @param storageType
     * @param storageRecordId
     * @return
     */
    private static FlowVo buildFlowParam(long companyId, String code, int storageType, long storageRecordId) {
        FlowVo flowVo = new FlowVo();
        flowVo.setCompanyId(companyId);
        flowVo.setCode(code);
        flowVo.setStorageType(storageType);
        flowVo.setStorageRecordId(storageRecordId);
        flowVo.setCreateUser(SecurityUtils.getLoginUser().getUser().getUserId());
        flowVo.setCreateTime(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));
        return flowVo;
    }

    /**
     * 查询自己和所有下级经销商
     * ${@com.ztl.gym.storage.service.impl.StorageServiceImpl@getAllChildId('and tenant_id ')}
     * 查询权限控制，关联到mapper.xml
     *
     * @param sql
     * @return
     */
    public static String getAllChildId(String sql) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String concat = sql.concat("in ( select ").concat(String.valueOf(loginUser.getUser().getDeptId())).concat(" union select dept_id from sys_dept where status = 0 and del_flag = '0' and dept_type=2 and find_in_set(").concat(String.valueOf(loginUser.getUser().getDeptId())).concat(", ancestors))");
        return concat;
    }

    /**
     * 查询自己
     * ${@com.ztl.gym.storage.service.impl.StorageServiceImpl@getMyTenantId('and tenant_id ')}
     * 查询权限控制，关联到mapper.xml
     *
     * @param sql
     * @return
     */
    public static String getMyTenantId(String sql) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String concat = sql.concat("in (").concat(String.valueOf(loginUser.getUser().getDeptId())).concat(")");
        return concat;
    }


}
