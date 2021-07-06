//package com.ztl.gym.web.controller.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.*;
//
//
//@Slf4j
//@RequestMapping("/api")
//@RestController
//public class AndroidController {
//
//    private final static String CODE = "Device_mgn";
//
//    private final static String GROUP_CODE = "GROUP_TYPE";
//
//    //能够入库的工序
//    private final static String IN_ROOM_PROCESS = "交联";
//
//
//    private final static String CALENDAR_PARAMETER_UNIT = "CALENDAR_PARAMETER_UNIT";
//
//    private final static String CONVERSION_FACTOR_OF_STANDARD_COAL = "CONVERSION_FACTOR_OF_STANDARD_COAL";
//
//
//
//    /**
//     * 版本更新
//     *
//     * @author zttlink
//     * @Date 2018/12/23 4:57 PM
//     */
//    @ResponseBody
//    @RequestMapping("/versionUpdate")
//    public ResponseData versionUpdate(@RequestBody Map map) {
//        String token = map.get("token") == null ? null : map.get("token").toString();
//        String currentVersion = map.get("currentVersion") == null ? null : map.get("currentVersion").toString();
//        UserDto user = userService.getUserIdByToken(token);
//        if (user == null) {
//            return ResponseData.error(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED.getMessage(), new Object());
//        }
//        if (ToolUtil.isOneEmpty(currentVersion)) {
//            return ResponseData.error(MISSING_REQUIRED_PARA.getCode(), MISSING_REQUIRED_PARA.getMessage(), new Object());
//        }
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("isUpdate", false);
//        res.put("forceUpdate", false);
//        res.put("apkUrl", "");
//        res.put("updateDescription", "");
//        List<AndroidVersionResult> androidVersionList = userService.getAndroidVersionInfo();
//
//        if (androidVersionList == null || androidVersionList.size() == 0) {
//            return ResponseData.success(res);
//        }
//        //得到最大版本
//        androidVersionList.sort((x, y) -> compareVersion(y.getNewVersion(), x.getNewVersion()));
//        AndroidVersionResult androidVersion = androidVersionList.get(0);
//
//        res.put("apkUrl", androidVersion.getApkUrl());
//        res.put("updateDescription", androidVersion.getUpdateDescription());
//        if (!StringUtils.isEmpty(androidVersion.getNewVersion()) && this.compareVersion(currentVersion, androidVersion.getNewVersion()) < 0) {
//            res.put("isUpdate", true);
//        }
//        if (!StringUtils.isEmpty(androidVersion.getMinVersion()) && this.compareVersion(currentVersion, androidVersion.getMinVersion()) < 0) {
//            res.put("forceUpdate", true);
//        }
//        String forceUpdateList = androidVersion.getForceUpdateList();
//        String[] forceUpdateArr = androidVersion.getForceUpdateList().split(",");
//        if (!StringUtils.isEmpty(forceUpdateList) && Arrays.binarySearch(forceUpdateArr, currentVersion) >= 0) {
//            res.put("forceUpdate", true);
//        }
//        return ResponseData.success(res);
//
//    }
//
//
//    /**
//     * 比较版本号
//     *
//     * @param version1 version1
//     * @param version2 version2
//     * @return 0, 1
//     */
//    private int compareVersion(String version1, String version2) {
//        //.是转义字符，需要带\\，不然无法split
//        String[] arr1 = version1.split("\\.");
//        String[] arr2 = version2.split("\\.");
//        int len = Math.min(arr1.length, arr2.length);
//        for (int i = 0; i < len; i++) {
//            if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
//                return 1;
//            } else if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
//                return -1;
//            }
//        }
//
//        if (arr1.length > len) {
//            for (int i = len; i < arr1.length; i++) {
//                //如果后面都是.0.0则返回0
//                if (Integer.parseInt(arr1[i]) > 0) {
//                    return 1;
//                }
//            }
//            return 0;
//        }
//
//        if (arr2.length > len) {
//            for (int i = len; i < arr2.length; i++) {
//                //如果arr2后面还有非0字符则表示arr2版本更大
//                if (Integer.parseInt(arr2[i]) > 0) {
//                    return -1;
//                }
//            }
//            return 0;
//        }
//        //两个数组长度、字符都相等则返回0
//        return 0;
//    }
//
//    /**
//     * 上传apk
//     *
//     * @param file file
//     * @return msg
//     */
//    @ResponseBody
//    @RequestMapping(value = "/uploadApk", method = RequestMethod.POST)
//    public ResponseData uploadApk(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
//
//        String projectPath = System.getProperty("user.dir");
//
//        //目标路径,目前是相对路径
//        String filePath = PathUtil.selectApkPathByOs();
//        String absolutelyPath = projectPath.concat(File.separator).concat(filePath);
//
//        log.info(" apk存放路径为: {}", absolutelyPath);
//
//        File targetPath = new File(absolutelyPath);
//        //如果文件目录不存在，就执行创建
//        if (!targetPath.exists() && !targetPath.isDirectory()) {
//            targetPath.mkdirs();
//        }
//
//
//        String fileName = file.getOriginalFilename();
//        if (StringUtils.isEmpty(fileName)) {
//            return ResponseData.error(500, "上传出错,文件名为空", new Object());
//        }
//        //获取后缀名
//        String suffix = fileName.substring(fileName.indexOf("."));
//        if (!(suffix.contains("apk"))) {
//            return ResponseData.error(500, "上传出错,该文件不是符合要求的apk格式", new Object());
//        }
//
//        //目标文件
//        File targetFile = new File(absolutelyPath, file.getOriginalFilename());
//        try {
//            //将上传的文件写到服务器上指定的文件。
//            file.transferTo(targetFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseData.error(500, "上传出错,apk写入失败", new Object());
//        }
//
//        String hostUrl = PathUtil.getApkUrl(request);
//        String url = hostUrl.concat(targetFile.getName());
//
//        return ResponseData.success(url);
//    }
//
//
//
//    //转化为安卓需要的结构体
//    private List<Map> convertToDayReport(List<Map> dataList, Map<String, String> enNameCnNameMap, List<String> title) {
//        for (Map map : dataList) {
//            List<String> valueList = new ArrayList<>();
//            String time = map.getOrDefault("time", "").toString();
//            map.remove("time");
//            TreeMap<String, Object> nameValueMap = new TreeMap<>(String::compareTo);
//
//            //将英文翻译成中文
//            for (Object key : map.keySet()) {
//                String cnName = enNameCnNameMap.get(key.toString());
//                nameValueMap.put(cnName,
//                        new BigDecimal(map.get(key).toString()).setScale(1, RoundingMode.UP));
//            }
//
//            for (String titleName : title) {
//                if (nameValueMap.containsKey(titleName)) {
//                    valueList.add(nameValueMap.get(titleName).toString());
//                } else {
//                    valueList.add("");
//                }
//            }
//            String[] valueArr = valueList.toArray(new String[0]);
//            map.clear();
//            map.put("time", time);
//            map.put("data", valueArr);
//        }
//
//        if (dataList.size() != 0) {
//            Map firstMap = dataList.get(0);
//            firstMap.put("title", title);
//        }
//        return dataList;
//    }
//
//    //转化为安卓需要的结构体
//    private List<Map> convertToHistoryData(List<Map> dataList, Map<String, String> enNameCnNameMap, List<String> title) {
//        for (Map map : dataList) {
//            List<String> valueList = new ArrayList<>();
//            String time = map.getOrDefault("time", "").toString();
//            map.remove("time");
//            map.remove("date");
//            TreeMap<String, Object> nameValueMap = new TreeMap<>(String::compareTo);
//
//            //将英文翻译成中文
//            for (Object key : map.keySet()) {
//                String cnName = enNameCnNameMap.get(key.toString());
//                nameValueMap.put(cnName,
//                        new BigDecimal(map.get(key).toString()).setScale(1, RoundingMode.UP));
//            }
//            for (String titleName : title) {
//                if (nameValueMap.containsKey(titleName)) {
//                    valueList.add(nameValueMap.get(titleName).toString());
//                } else {
//                    valueList.add("");
//                }
//            }
//            String[] valueArr = valueList.toArray(new String[0]);
//            map.clear();
//            map.put("time", time);
//            map.put("data", valueArr);
//        }
//        if (dataList.size() != 0) {
//            Map firstMap = dataList.get(0);
//            firstMap.put("title", title);
//        }
//        return dataList;
//    }
//
//
//
//
//}
