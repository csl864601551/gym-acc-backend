package com.ztl.gym.idis.prop;

import com.ztl.gym.code.domain.Code;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * IDIS企业节点配置
 *
 * @author zt_sly
 * @date 2021-07-21
 */
@Data
@Component
@ConfigurationProperties("idis")
public class IdisProp {


    /**
     * 分表ID
     */
    private Long companyId;
    /**
     * 企业节点主机名
     */
    private String host;

    /**
     * 企业节点端口号
     */
    private String port;

    /**
     * 企业节点用户名
     */
    private String user;

    /**
     * 企业节点密码
     */
    private String pwd;

    /**
     * 标识前缀
     */
    private String prefix;

    /**
     * 模板版本
     */
    private String version;

    /**
     * 标识属性
     */
    private List<String> attrList;
    /**
     * 标识属性
     */
    private List<Code> codeList;

}
