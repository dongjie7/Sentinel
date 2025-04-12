package com.alibaba.csp.sentinel.dashboard.rule.nacos;

/**
 * @Description: nacos配置
 * @author: zyf
 * @date: 2022/03/01$
 * @version: V1.0
 */
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "nacos.server")
public class NacosConfigProperties {
    private String ip;
    private String namespace;
    private String username;
    private String password;
    private String groupId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getServerAddr() {
        return this.getIp();
    }

}