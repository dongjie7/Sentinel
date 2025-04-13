package com.alibaba.csp.sentinel.dashboard.repository.metric;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.MetricEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Classname com.alibaba.csp.sentinel.dashboard.repository.metric.JdbcMetricsRepository.java
 * @author dongjie
 * @Date 2025/4/13 9:36
 * @version 1.0
 * Copyright notice
 */
@Component
@Primary
public class JdbcMetricsRepository implements MetricsRepository<MetricEntity>{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(MetricEntity metric) {
        String sql = "INSERT INTO sentinel_metric (gmt_create, gmt_modified, app, resource, timestamp, pass_qps, block_qps, success_qps, exception_qps, rt) " +
                "VALUES (NOW(), NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                metric.getApp(),
                metric.getResource(),
                metric.getTimestamp().getTime(),
                metric.getPassQps(),
                metric.getBlockQps(),
                metric.getSuccessQps(),
                metric.getExceptionQps(),
                metric.getRt()
        );
    }

    @Override
    public List<MetricEntity> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime) {
        String sql = "SELECT * FROM sentinel_metric WHERE app = ? AND resource = ? AND timestamp BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new RowMapper<>() {
            @Override
            public MetricEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                long timestamp = rs.getLong("timestamp");
                MetricEntity metricEntity = new MetricEntity();
                metricEntity.setApp(rs.getString("app"));
                metricEntity.setResource(rs.getString("resource"));
                metricEntity.setId(rs.getLong("id"));
                metricEntity.setPassQps(rs.getLong("pass_qps"));
                metricEntity.setBlockQps(rs.getLong("block_qps"));
                metricEntity.setSuccessQps(rs.getLong("success_qps"));
                metricEntity.setExceptionQps(rs.getLong("exception_qps"));
                metricEntity.setRt(rs.getDouble("rt"));
                metricEntity.setTimestamp(new Date(timestamp));
                metricEntity.setGmtCreate(rs.getDate("gmt_create"));
                metricEntity.setGmtModified(rs.getDate("gmt_modified"));
                return metricEntity;
            }
        }, app, resource, startTime, endTime);
    }

    @Override
    public void saveAll(Iterable<MetricEntity> metrics) {
        Iterator iterator = metrics.iterator();
        metrics.forEach(this::save);
    }

    @Override
    public List<String> listResourcesOfApp(String app) {
        String sql = "SELECT distinct resource FROM sentinel_metric WHERE app = ?";
        return jdbcTemplate.queryForList(sql, String.class, app);
    }
}
