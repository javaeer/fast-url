<div align="center">

# fast-url

**A lightweight short-URL service built on Spring Boot — convert long URLs to short codes, cache in Redis, and track visits asynchronously via RabbitMQ.**

[![License: Apache 2.0](https://img.shields.io/github/license/javaeer/fast-url?style=flat-square)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-0091EA?style=flat-square)](https://maven.apache.org/)
[![Version](https://img.shields.io/badge/version-0.1.0--SNAPSHOT-orange?style=flat-square)](https://github.com/javaeer/fast-url/releases)

</div>

---

## 📖 简介 / Introduction

`fast-url` 是一个**基于 Spring Boot 的短链接服务**：把冗长的原始网址压缩为短码，访问短链时再 302 重定向回原始网址，并通过 Redis 缓存加速、RabbitMQ 异步统计访问次数。适合作为营销短链、站内短链分发等场景的后端。


- 🔗 **长链转短链**：基于雪花算法生成分布式 ID，再用 Base62 编码为短码
- ♻️ **去重**：同一长链只生成一个短链，重复请求返回既有记录
- ⚡ **Redis 缓存**：短码 → 长链映射缓存，重定向走缓存、低延迟
- 📊 **异步统计**：短链访问次数通过 RabbitMQ 解耦累加，手动 ACK 保证可靠消费
- 🗄 **MyBatis-Plus**：ORM 持久化，代码简洁
- 🧩 **统一响应**：`ResponzeResult` 封装接口返回，异常统一处理

## 🧱 架构概览 / Architecture

```mermaid
flowchart LR
    U[用户/浏览器] -->|GET /generate?longUrl| C[FastUrlController]
    C --> S[FastUrlService.saveAndGet]
    S -->|雪花ID+Base62| DB[(MySQL t_fast_url)]
    C -->|写缓存| R[(Redis FAST_URL:shortUrl)]
    U -->|GET /{shortUrl}| C2[FastUrlController.redirect]
    C2 -->|读缓存| R
    C2 -->|发送短码| MQ[RabbitMQ]
    MQ --> L[FastUrlListener]
    L -->|访问+1| DB2[(MySQL t_fast_url_access)]
    C2 -->|302 重定向| U
```

| 模块 | 说明 | 关键类 |
| --- | --- | --- |
| 接入层 | 生成短链 / 重定向 / 发 MQ | `FastUrlController` |
| 业务层 | 生成短码、去重、落库 | `FastUrlService` / `FastUrlServiceImpl` |
| 持久层 | 短链表、访问计数表 | `FastUrlMapper` / `FastUrlAccessMapper` |
| 消费层 | 异步累加访问次数（手动 ACK） | `FastUrlListener` |
| 工具层 | 雪花 ID、Base62 编码、系统时钟 | `Sequence` / `EncoderUtils` / `SystemClock` |
| 实体 | 短链 / 访问计数 | `FastUrl` / `FastUrlAccess` |

## 🛠 技术栈 / Tech Stack

| 依赖 | 版本 | 用途 |
| --- | --- | --- |
| Java | 8 | 运行环境 |
| [Spring Boot](https://spring.io/projects/spring-boot) | 2.7.18 | 应用框架 |
| [MyBatis-Plus](https://baomidou.com/) | 3.5.5 | ORM 持久化 |
| MySQL Connector/J | 8.0.33 | 数据库驱动（runtime） |
| Spring Data Redis + Lettuce | 2.7.18 | 缓存 |
| Spring AMQP (RabbitMQ) | 2.7.18 | 异步消息 |
| Lombok | — | 简化样板代码 |
| commons-codec / commons-text | — / 1.15.0 | 编解码工具 |
| bitcoinj-core | 0.16.2 | 工具依赖（被 `Sequence` 引用） |
| swagger-annotations | 1.6.8 | API 注解 |
| Undertow | 2.7.18 | Web 容器（可在 `application.yml` 启用） |

## 🚀 快速开始 / Quick Start

### 环境要求

- JDK **8** 及以上
- Maven **3.6+**（仓库已附带 `mvnw` / `mvnw.cmd` 包装器）
- **外部依赖**：MySQL 8、Redis、RabbitMQ（需先建库并执行仓库根目录的 `t_fast_url.sql` / `t_fast_url_access.sql`）

### 准备数据库与中间件

```bash
# 1) 创建数据库
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS fast_url DEFAULT CHARSET utf8mb4;"

# 2) 执行建表（仓库根目录）
mysql -uroot -p fast_url < t_fast_url.sql
mysql -uroot -p fast_url < t_fast_url_access.sql

# 3) Redis / RabbitMQ 就绪（默认端口 6379 / 5672）
```

### 配置

编辑 `src/main/resources/application.yml`，将 `base.config` 下的 MySQL / Redis / RabbitMQ 连接改为**你自己的地址与凭据**（请勿提交真实生产凭据）。

### 构建与运行

```bash
./mvnw clean package
java -jar target/fast-url.jar
# 应用监听 8080
```

## 🔌 API 示例 / API Usage

### 生成短链

```bash
curl "http://localhost:8080/generate?longUrl=https://example.com/some/very/long/url"
# -> 统一响应体，data 为 FastUrl（含 id / shortUrl / longUrl / domain 等）
# 默认 domain = https://www.yunlou.net.cn，因此完整短链为 https://www.yunlou.net.cn/{shortUrl}
```

`/generate` 同时支持 `GET` 与 `POST`。

### 访问短链（重定向）

```bash
# 浏览器或 curl -L 访问，将 302 重定向到原始长链
curl -L "http://localhost:8080/{shortUrl}"
```

> 重定向时短码经 Redis 命中后直接 302；若缓存未命中则跳转到 `https://smartcloudx.com/404.html`。访问事件会异步发送到 RabbitMQ，由 `FastUrlListener` 累加 `t_fast_url_access` 表的访问计数。

## 🗄 数据表结构 / Schema

- **`t_fast_url`**：`id`（雪花 ID，Base62 编码为短码）、`short_url`、`long_url`、`domain`、`create_time`、`update_time`、`del_flag` 等
- **`t_fast_url_access`**：`id`（短码字符串）、`access_count`（访问次数）等

完整 DDL 见仓库根目录 `t_fast_url.sql` / `t_fast_url_access.sql`。

## 🔐 安全须知

- **🔴 凭据已明文入库（最高优先级）**：`application.yml` 内含真实服务器 IP 与 MySQL / Redis / RabbitMQ 密码。**公开前请立即轮转这些密码**，并把连接信息改为环境变量注入（如 `BASE_CONFIG_MYSQL_PASSWORD`）。
- **明文 HTTP**：服务默认 HTTP（8080），对外暴露建议前置 HTTPS 反向代理。
- **缓存未回源**：重定向仅读 Redis，缓存缺失即跳 404，不会回查数据库——生产建议增加缓存预热 / 回源降级。
- **硬编码域名**：短链域名 `https://www.yunlou.net.cn` 与 404 页 `https://smartcloudx.com/404.html` 写在代码中，需改代码才能替换。

更多安全策略见 [SECURITY.md](SECURITY.md)。

## 🗺 路线图 / Roadmap

### ✅ 已完成 / Done
- [x] 长链 → 短链（雪花 ID + Base62），并支持去重
- [x] Redis 缓存加速重定向
- [x] RabbitMQ 异步统计访问次数（手动 ACK）
- [x] MyBatis-Plus 持久化、统一响应与异常处理

### 📋 待办 / TODO
- [ ] **将 `application.yml` 中的 IP / 密码外部化**（消除明文凭据泄露）
- [ ] 短链域名 / 404 页改为可配置
- [ ] 重定向缓存未命中时回源数据库降级（而非直接 404）
- [ ] 增加 HTTPS / 短链访问鉴权与限流
- [ ] 补充单元 / 集成测试与 Docker Compose 一键起依赖

## 📂 项目结构 / Project Structure

```
fast-url/
├── LICENSE
├── README.md / README.en.md
├── pom.xml
├── t_fast_url.sql              # 建表 DDL
├── t_fast_url_access.sql      # 访问计数表 DDL
└── src/main/
    ├── java/cn/net/yunlou/fasturl/
    │   ├── FastUrlApplication.java        # 启动类
    │   ├── controller/FastUrlController.java   # 生成 / 重定向
    │   ├── service/                        # FastUrlService(Impl) / FastUrlAccessService(Impl)
    │   ├── mapper/                         # FastUrlMapper / FastUrlAccessMapper
    │   ├── entity/                         # FastUrl / FastUrlAccess
    │   ├── listener/FastUrlListener.java    # RabbitMQ 消费
    │   ├── utils/                          # Sequence / EncoderUtils / SystemClock
    │   └── ResponzeResult / ResponzeStatus / ResponzeException / IEnum  # 统一响应
    └── resources/
        └── application.yml                 # 连接配置（含需外部化的凭据）
```

## 🤝 贡献 / Contributing

欢迎提交 Issue 与 Pull Request！请先阅读 [CONTRIBUTING.md](CONTRIBUTING.md) 了解分支模型与提交规范。

## 🔒 安全漏洞上报 / Security

如发现安全漏洞，请按 [SECURITY.md](SECURITY.md) 中的流程**私下**上报，勿在公开 Issue 中披露。

## 📄 许可证 / License

本项目基于 [Apache License 2.0](LICENSE) 开源。
