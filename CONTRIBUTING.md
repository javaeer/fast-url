# 贡献指南 / Contributing

感谢你考虑为 **fast-url** 做出贡献！本文档说明如何参与开发与提交。

## 🌿 分支模型 / Branching

| 分支 | 用途 |
| --- | --- |
| `main` | 默认保护分支，仅接受经评审的合并 |
| `dev` | 集成开发分支 |
| `feature/xxx` | 新功能开发 |
| `fix/xxx` | 缺陷修复 |

> 提交请基于 `dev` 切出 `feature/xxx` 或 `fix/xxx`，完成后向 `dev` 发 Pull Request，再由维护者合并到 `main`。

## 📝 提交规范 / Commit Convention

请使用 [Conventional Commits](https://www.conventionalcommits.org/zh-hans/) 风格：

| 前缀 | 含义 |
| --- | --- |
| `feat:` | 新功能 |
| `fix:` | 缺陷修复 |
| `docs:` | 文档 |
| `refactor:` | 重构（不改变外部行为） |
| `test:` | 测试 |
| `chore:` | 构建 / 工具链 |

示例：`feat: 支持短链域名可配置`、`fix: 重定向缓存未命中回源数据库`。

## 🛠 本地开发 / Local Development

```bash
# 使用 Maven 包装器（推荐）
./mvnw clean package

# 本地已安装 Maven 亦可
mvn clean package

# 运行（默认 8080，依赖 MySQL / Redis / RabbitMQ 就绪）
java -jar target/fast-url.jar
```

### 依赖的中间件
- MySQL 8（建库 `fast_url` 并执行根目录 `t_fast_url.sql` / `t_fast_url_access.sql`）
- Redis（默认库 1）
- RabbitMQ（默认 `javaeer` / `javaeer`，手动 ACK 队列）

> ⚠️ 请不要在 `application.yml` 中提交真实生产凭据；本地使用你自己的连接信息，或改用环境变量注入。

## 💬 讨论 / Discussions

功能建议与疑问请优先在 **GitHub Discussions**（或 Issue）中交流，再决定是否提 PR。

## ✅ PR 检查清单

- [ ] 关联对应的 Issue
- [ ] 遵循 Conventional Commits 提交规范
- [ ] 新增 / 修改逻辑附带必要的测试
- [ ] 不在代码中遗留真实凭据或内网 IP
