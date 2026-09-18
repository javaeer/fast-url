# 更新日志 / Changelog

本文件所有记录遵循 [Keep a Changelog](https://keepachangelog.com/zh-CN/) 约定，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [Unreleased]

### 计划 / Planned
- 将 `application.yml` 中的 MySQL / Redis / RabbitMQ 连接信息（含 IP 与密码）外部化，消除明文凭据泄露
- 短链域名、404 跳转页改为可配置
- 重定向缓存未命中时回源数据库降级（而非直接 404）
- 增加 HTTPS、短链访问鉴权与限流
- 补充单元 / 集成测试与 Docker Compose 一键起依赖

## [0.1.0] - 2026-03-06

### 新增 / Added
- 基于 Spring Boot 2.7.18 + MyBatis-Plus 的短链接服务
- 长链 → 短链：雪花算法生成分布式 ID，Base62 编码为短码，并支持去重
- Redis 缓存短码 → 长链，加速重定向
- RabbitMQ 异步统计访问次数（`FastUrlListener` 手动 ACK 可靠消费）
- `GET /generate`（同时支持 POST）生成短链、`GET /{shortUrl}` 重定向
- 统一响应体 `ResponzeResult` 与异常处理
- `t_fast_url` / `t_fast_url_access` 建表 DDL

[Unreleased]: https://github.com/javaeer/fast-url/compare/v0.1.0...main
[0.1.0]: https://github.com/javaeer/fast-url/releases/tag/v0.1.0
