# Quark 项目使用说明

## 前端页面地址

- **普通用户前端页面**：  
  http://localhost:8082/index

- **管理员用户前端页面**：  
  http://localhost:8080/login

## 使用方式

1. **下载项目**  
   将仓库克隆或下载到本地。

2. **编译父工程**  
   进入 `quark-parent` 目录，运行：  
   mvn clean install
   如果编译过程中出现报错，可将错误信息提交给 GPT 进行诊断。

3. **启动子模块**
   分别进入以下五个子模块目录，运行各自的 *Application.java 启动类：

	admin

	common

	chat

	portal

	rest

确保启动后控制台无异常且进程保持运行。

访问前端页面
在浏览器中输入对应 URL，即可访问页面：

普通用户：http://localhost:8082/index

管理员用户：http://localhost:8080/login

若上述步骤均完成且无报错，即表示项目环境已正确配置并启动成功。
