package com.ztl.gym.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.URLUtil;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.PutObjectResult;
import com.ztl.gym.common.constant.HttpStatus;
import com.ztl.gym.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Component
public class CommonUtil {
    /**
     * 定义日志对象
     */
    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
    /**
     * 雪花算法18位
     */
    public static String snowflake() {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long id = snowflake.nextId();
        return String.valueOf(id);
    }

    /**
     * 声明静态属性-endPoint
     */
    private static String endPoint;
    /**
     * 声明静态属性-ak
     */
    private static String ak;
    /**
     * 声明静态属性-sk
     */
    private static String sk;

    /**
     * 声明静态属性-sk
     */
    private static String bucketName;


    @Value("${obs.endPoint}")
    private String obsEndPoint;

    @Value("${obs.ak}")
    private String obsAk;

    @Value("${obs.sk}")
    private String obsSk;

    @Value("${obs.bucketName}")
    private String obsBucketName;

    /**
     * 先把yml 值读取到普通属性中再转到静态属性值时面
     *
     * @PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次;
     */
    @PostConstruct
    public void transValues() {
        endPoint = this.obsEndPoint;
        ak = this.obsAk;
        sk = this.obsSk;
        bucketName = this.obsBucketName;
    }

    /**
     * 上传图片
     */
    public static String uploadPic(MultipartFile file, String folder) {
        String fileName = file.getOriginalFilename();
        //获取文件名加后缀
        if (fileName != null && !"".equals(fileName)) {
            String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
            ObsClient obsClient = new ObsClient(ak, sk, endPoint);
            String imagePath = "acc/"+folder + "/" + DateUtil.format(new Date(), "yyyyMM") + "/";
            Snowflake snowflake = IdUtil.createSnowflake(1, 1);
            fileName = Convert.toStr(snowflake.nextId());
            String saveUrl = imagePath + fileName + fileSuffix;
            InputStream input;
            try {
                input = file.getInputStream();
                PutObjectResult ret = obsClient.putObject(bucketName, saveUrl, input);
                String url = URLUtil.decode(ret.getObjectUrl());
                log.info("文件上传成功,url:" + url);
                return URLDecoder.decode(url).replace(":443", "");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            log.error("获取文件名失败");
            return null;
        }
    }

    public static String upload(String filePath, InputStream inputStream) {
        // 您的工程中可以只保留一个全局的ObsClient实例
        // ObsClient是线程安全的，可在并发场景下使用
        ObsClient obsClient = null;
        try {
            // 创建ObsClient实例
            obsClient = new ObsClient(ak, sk, endPoint);
            // 调用接口进行操作，例如上传对象
            PutObjectResult response = obsClient
                    .putObject(bucketName, filePath, inputStream);  // localfile为待上传的本地文件路径，需要指定到具体的文件名
            System.out.println(response.getObjectUrl());
            if (null != response && StringUtils.isNotEmpty(response.getObjectUrl())) {
                return URLDecoder.decode(response.getObjectUrl());
            } else {
                return null;
            }
        } catch (ObsException e) {
            log.error("-- OBS 上传文件异常 --- ", e);
        } finally {
            // 关闭ObsClient实例，如果是全局ObsClient实例，可以不在每个方法调用完成后关闭
            // ObsClient在调用ObsClient.close方法关闭后不能再次使用
            if (obsClient != null) {
                try {
                    obsClient.close();
                } catch (IOException ioe) {
                    log.error("-- OBS 上传文件异常 --- ", ioe);
                }
            }
        }
        return null;
    }

    /**
     * 上传图片
     */
    public static String uploadPic(InputStream inputStream, String folder) {

        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        String imagePath = "acc/"+folder + "/" + DateUtil.format(new Date(), "yyyyMM") + "/";
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        String fileName = Convert.toStr(snowflake.nextId());
        String saveUrl = imagePath + fileName + ".png";
        try {
            PutObjectResult ret = obsClient.putObject(bucketName, saveUrl, inputStream);
            String url = URLUtil.decode(ret.getObjectUrl());
            log.info("文件上传成功,url:" + url);
            return URLDecoder.decode(url).replace(":443", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取某段时间内的所有日期
    public static List<String> getDateListBetween(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dStart = null;
            Date dEnd = null;
            try {
                dStart = sdf.parse(start);
                dEnd = sdf.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Date> dateList = findDates(dStart, dEnd);
            List list = new ArrayList();
            for (Date date : dateList) {
                list.add(sdf.format(date));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //JAVA获取某段时间内的所有日期
    public static List<Date> findDates(Date dStart, Date dEnd) {
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(dStart);

        List dateList = new ArrayList();
        //别忘了，把起始日期加上
        dateList.add(dStart);
        // 此日期是否在指定日期之后
        while (dEnd.after(cStart.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cStart.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(cStart.getTime());
        }
        return dateList;
    }

    /**
     * 日期转换星期几
     *
     * @param date 需要转换的日期
     */
    public static String dateToWeek(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 指示一个星期中的某天,0代表星期天。
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 0 : cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }

    /**
     * 将数据转换为特定的list
     */
    public static <T, R> List<R> mapList(Collection<T> raw, Function<T, R> converter) {
        if (raw == null) {
            return new ArrayList<>();
        }
        List<R> res = new ArrayList<>(raw.size());
        for (T t : raw) {
            res.add(converter.apply(t));
        }
        return res;
    }


    /**
     * 将数据转换为特定的set
     */
    public static <T, R> Set<R> mapSet(Collection<T> raw, Function<T, R> converter) {
        if (raw == null) {
            return new HashSet<>();
        }
        Set<R> res = new HashSet<>(raw.size());
        for (T t : raw) {
            res.add(converter.apply(t));
        }
        return res;
    }

    public static <T, R> Set<R> mapSet(Collection<T> raw, Function<T, R> converter, Set<R> set) {
        if (raw == null) {
            return new HashSet<>();
        }
        for (T t : raw) {
            set.add(converter.apply(t));
        }
        return set;
    }


    /**
     * 将数据转换为特定的map
     */
    public static <T, K, V> Map<K, V> mapMap(Collection<T> raw, Function<T, K> toKey, Function<T, V> toValue) {
        if (raw == null) {
            return new HashMap<>();
        }
        Map<K, V> res = new HashMap<>(raw.size());
        for (T t : raw) {
            res.put(toKey.apply(t), toValue.apply(t));
        }
        return res;
    }

    public static <T, K> Map<K, T> mapMap(Collection<T> raw, Function<T, K> toKey) {
        return mapMap(raw, toKey, Function.identity());
    }

    /**
     * 转义字符串中的在这个表达式中有特殊含义的字符
     */
    public static String escapeRegex(String str) {
        if (StringUtils.isNotBlank(str)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (str.contains(key)) {
                    str = str.replace(key, "\\" + key);
                }
            }
        }
        return str;
    }

    /**
     * 生成订单随机数
     * 生成规则时间戳+随机数
     *
     * @return
     */
    public static String buildOrderNo(int length) {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            log.error("生成随机数失败");
            throw new CustomException("系统内部错误.", HttpStatus.ERROR);
        }
        StringBuffer num = new StringBuffer();
        for (int i = 0; i < length; i++) {
            num.append(random.nextInt(9));
        }
        return num.toString();
    }
    /**
     * 生成订单随机数
     * 生成规则时间戳+随机数
     *
     * @return
     */
    public static String buildOrderNo() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            log.error("生成随机数失败");
            throw new CustomException("系统内部错误.", HttpStatus.ERROR);
        }
        StringBuffer num = new StringBuffer();
        for (int i = 0; i < 5; i++) {
            num.append(random.nextInt(9));
        }
        return DateUtils.dateTimeNow() + num;
    }
    public static void main(String[] args) {
        String code = buildOrderNo(5);
        System.out.println(code);
    }
}
