# MuzicBox

一个使用 **Java + XML** 编写的极简 Android 音乐播放器，也是《安卓逆向负基础之正向开发实践》系列的配套开源项目。

项目会从最小可运行工程开始，逐章加入本地音乐、后台播放、在线音乐、JNI 参数签名和基础安全实践。代码与开发笔记同步演进，每个阶段都提供可回看的 Git 标签。

## 当前进度

当前版本：`v0.2.0`（第 02 章）

- [x] 创建 Java Android 工程
- [x] 完成极简启动页
- [x] 用日志观察 Activity 生命周期
- [x] 扫描并播放本地音乐
- [ ] 后台音乐播放与 Service
- [ ] 在线音乐播放与网络请求
- [ ] 用 JNI 实现参数签名
- [ ] 基础反调试与 Frida 检测
- [ ] R8 混淆、签名与加壳实验

## 文章目录

1. [项目规划与工程创建](docs/chapters/01-project-and-plan.md)（初版）
2. [本地音乐扫描与基础播放](docs/chapters/02-local-music-and-playback.md)（初版）
3. 后台音乐播放与 Service（待更新）
4. 在线音乐播放与网络请求（待更新）
5. 用 JNI 实现参数签名（待更新）
6. 反调试与 Frida 检测（待更新）
7. R8 混淆、签名与加壳实验（待更新）

第 07 篇完成后，阶段验收、已知问题、文章索引和后续计划统一整理到 README 与 `v1.0.0` Release 说明中，不再单独占用一个编号篇章。

完整版本规划见 [ROADMAP.md](ROADMAP.md)，学习记录见 [docs/learning-log.md](docs/learning-log.md)。

## 技术基线

- 开发语言：Java 17
- 界面：Android XML View
- 最低系统：Android 8.0（API 26）
- 目标系统：Android 16（API 36）
- 构建工具：Android Gradle Plugin 9.2.1 / Gradle 9.4.1

## 本地运行

1. 使用 Android Studio 打开仓库根目录。
2. 等待 Gradle 同步完成。
3. 连接 Android 8.0 及以上设备或启动模拟器。
4. 运行 `app` 配置。

也可以在 Windows 终端中执行：

```powershell
.\gradlew.bat :app:assembleDebug
```

调试 APK 会生成在 `app/build/outputs/apk/debug/app-debug.apk`。

## 项目内容

本仓库同时展示三类可验证成果：

- 可以构建和运行的 Android 代码；
- 面向初学者的开发笔记；
- 每章对应的提交历史、版本标签与验证记录。

## License

本项目采用 [MIT License](LICENSE)。
