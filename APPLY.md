# APPLY.md — 如何把本 bundle 应用到 fast-url 仓库

本目录是一套**开源门面（open-source facelift）**交付物：它只补充项目治理与说明类文件（README、LICENSE、贡献/行为准则/安全规范、Issue/PR 模板等），**不改动任何 Java 源码、不升级依赖版本**（fast-url 仍停在 Spring Boot 2.7.18 / Java 8），**也不包含 CI 工作流**（按既有约定跳过 CI）。

---

## 1. 重要约定

| 项 | 说明 |
| --- | --- |
| 本次范围 | 仅新增/补充治理与文档文件，不动源码、不升级版本 |
| CI | **未提供** `.github/workflows/ci.yml`（延续上轮约定暂时忽略 CI） |
| 许可证 | Apache License 2.0（`LICENSE` 已随附） |

---

## 2. 应用步骤

### 2.1 克隆（注意默认分支）

```bash
git clone https://github.com/javaeer/fast-url.git
cd fast-url
```

> 如果你在本地默认拉到的是 `master` 以外的分支，请先确认远程默认分支：
> `git remote show origin | grep "HEAD branch"`。fast-url 的 HEAD 指向 `master`。

### 2.2 提交并推送

```bash
git add .
git commit -m "docs: add open-source project scaffolding (README, LICENSE, governance files)"
git push origin master
```

> 提交信息建议遵循仓库既有的 Conventional Commits 约定（详见 `CONTRIBUTING.md`）。

---
