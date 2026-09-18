# 安全策略 / Security Policy

## 🛡 受支持的版本 / Supported Versions

| 版本 | 是否维护 |
| --- | --- |
| `0.1.0-SNAPSHOT` | ✅ 维护中（最新开发版） |

## 🔒 漏洞上报 / Reporting a Vulnerability

请**不要**在公开 Issue 中披露安全漏洞。可通过以下方式私下上报：

- 使用 GitHub 仓库的 **Security → Report a vulnerability**（安全通告）
- 或邮件联系维护者：**javaeer@aliyun.com**

请尽量提供：

1. 漏洞类型与影响范围
2. 复现步骤（环境、配置、触发方式）
3. 可能的修复建议（如有）

我们会在收到后尽快确认并回应处理进展。

## 🔴 已知安全风险 / Known Security Risks（务必在处理前知悉）

### 1. 生产凭据与内网 IP 明文入库（最高优先级）
`src/main/resources/application.yml` 中**明文写死了真实服务器 IP（`119.3.172.150`）以及 MySQL / Redis 密码（`Walker@Boundless`）、RabbitMQ 凭据（`javaeer` / `javaeer`）**。

**立即处置**：
1. **轮转**上述所有密码（尤其是已公开的）；
2. 将连接信息改为**环境变量 / 配置中心**注入（如 `BASE_CONFIG_MYSQL_PASSWORD`），不要把真实值提交进版本库；
3. 在公开仓库前，先用占位符替换并在文档中说明。

### 2. 明文 HTTP
服务默认以 HTTP（8080）提供，账号与短链访问未加密。**建议前置 HTTPS 反向代理**后再对外暴露。

### 3. 重定向缓存未回源
`FastUrlController.redirect` 仅从 Redis 读取短码 → 长链映射；**缓存未命中即跳转到 `https://smartcloudx.com/404.html`，不会回查数据库**。生产建议增加缓存预热或回源降级，避免短链在缓存失效后不可用。

### 4. 硬编码域名与跳转页
短链域名 `https://www.yunlou.net.cn`（写在 `FastUrlServiceImpl`）与 404 页 `https://smartcloudx.com/404.html`（写在 `FastUrlController`）均为硬编码，替换域名需改代码。

### 5. 访问控制缺位
`/generate` 与 `/{shortUrl}` 均无鉴权与限流，任何人均可大量生成短链或触发重定向。**生产建议增加调用方鉴权、频控与防滥用策略**。

---

感谢你帮助提升本项目的安全性！🙏
