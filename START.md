# 本地启动（前端/后端一起跑）

适用场景：你主要做前端开发，需要同时启动前端页面 + 后端接口（本地 MySQL）。

## 端口约定

- 前端（Vite）：http://localhost:3000
- 后端（Spring Boot）：http://localhost:8080
- 数据库（MySQL）：localhost:3306
- 前端代理：`/api/*` 会转发到 `http://localhost:8080`（见 [vite.config.js](file:///Users/yanglixia/chinaUnidata/———/product-demo-platform/frontend/vite.config.js)）

## 第一次启动（需要做一次）

### 1) 安装依赖

macOS 推荐使用 Homebrew：

```bash
brew update

# 前端需要 Node.js（你的环境一般已经有了）
# 后端需要 Maven + Java 17
brew install maven
brew install openjdk@17

# 数据库
brew install mysql
```

### 2) 启动 MySQL 并初始化数据

启动 MySQL：

```bash
brew services start mysql
```

初始化数据库（只需要执行一次，重复执行会报“表已存在”等错误）：

```bash
mysql -uroot < backend/src/main/resources/init.sql
```

## 每次开发怎么同时启动（推荐：开两个终端）

### 终端 A：后端（Spring Boot）

进入 `backend/` 后启动（如果你已经把 Java 17 配进了终端环境，只需要执行 `mvn spring-boot:run`）：

```bash
cd backend

export JAVA_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
export PATH="/usr/local/opt/openjdk@17/bin:$PATH"

mvn spring-boot:run
```

看到类似日志代表启动成功：

- `Tomcat started on port 8080`

### 终端 B：前端（Vite）

进入 `frontend/` 后启动：

```bash
cd frontend
npm install
npm run dev -- --host 0.0.0.0 --port 3000
```

打开页面：

- http://localhost:3000

## 更省事的启动方式（推荐）

### 方式 A：一次性把 Java 17 写进你的终端配置（以后就不用每次 export）

把下面两行加到你的 `~/.zshrc`（只需要做一次）：

```bash
export JAVA_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
export PATH="/usr/local/opt/openjdk@17/bin:$PATH"
```

保存后执行：

```bash
source ~/.zshrc
```

之后每次启动后端直接：

```bash
cd backend
mvn spring-boot:run
```

### 方式 B：用项目自带脚本启动（不用记命令）

项目根目录下新增了 `scripts/`：

- `./scripts/dev-backend.sh` 只启动后端
- `./scripts/dev-frontend.sh` 只启动前端
- `./scripts/dev.sh` 同时启动前后端（会尝试启动 MySQL）

示例：

```bash
./scripts/dev-backend.sh
```

或：

```bash
./scripts/dev.sh
```

## 健康检查（遇到“暂无产品”时先看这几个）

### 1) 后端是否活着

```bash
curl -sS http://localhost:8080/api/products
```

能返回 JSON（非 500/非连接失败）说明后端和数据库基本正常。

### 2) MySQL 是否启动

```bash
lsof -nP -iTCP:3306 -sTCP:LISTEN
```

能看到 `mysqld` 监听 3306 说明 MySQL 起来了。

### 3) 前端代理是否生效

前端请求 `/api/products` 由 Vite 代理到 8080；如果你在前端控制台看到：

- `http proxy error: /api/... ECONNREFUSED`

通常是后端 8080 没启动，或后端启动但接口 500（数据库未初始化/未启动）。

## 常见问题

### Q1：`zsh: command not found: mvn`

说明 Maven 没装或没进 PATH：

```bash
brew install maven
mvn -v
```

### Q2：后端启动了，但 `/api/products` 返回 500

多数是数据库问题（没装、没启动、没初始化）：

```bash
brew services start mysql
mysql -uroot < backend/src/main/resources/init.sql
```

### Q3：Java 版本不对

后端基于 Spring Boot 3.x，建议用 Java 17：

```bash
brew install openjdk@17
export JAVA_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"
export PATH="/usr/local/opt/openjdk@17/bin:$PATH"
java -version
```
