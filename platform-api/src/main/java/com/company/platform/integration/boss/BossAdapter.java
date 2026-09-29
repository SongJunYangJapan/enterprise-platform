package com.company.platform.integration.boss;

// 待办：将来在这里用 Spring 的 RestClient 封装 BOSS 系统的 HTTP 接口。
// 要把 BOSS 返回的字段转换成平台自己定义的 DTO，
// 不要让 BOSS 原始的响应结构直接进入诊断服务（DiagnosisService）。
public interface BossAdapter {}
