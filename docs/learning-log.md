# MuzicBox 学习日志

这里记录每次开发的目标、验证方式和阶段结论。

## 2026-10-09：第 01 章——创建工程

### 本次目标

- 创建一个 Java + XML 的 Android 应用。
- 让启动页展示当前章节和后续路线。
- 在 `MainActivity` 中记录生命周期日志。
- 建立适合持续记录开发笔记的仓库结构。

### 环境

- Android Studio Panda 4 Patch 1（2025.3.4）
- Android Gradle Plugin 9.2.1
- Gradle 9.4.1
- Android Studio Runtime 21（用于运行构建工具）
- Java 源码兼容级别 17
- compileSdk 36.1 / targetSdk 36 / minSdk 26

### 验证记录

- `:app:assembleDebug`：通过，首次完整构建执行 33 个任务。
- `:app:lintDebug`：通过，0 个错误；修复了缺少应用图标和界面背景重复绘制问题。剩余 3 条均为已知的 SDK / Gradle 新版本提示。
- `:app:testDebugUnitTest`：当前没有单元测试源码，因此任务显示 `NO-SOURCE`。
- Debug APK：`app/build/outputs/apk/debug/app-debug.apk`。
- Android 真机安装与启动：通过，主界面显示正常。
- 运行截图：[MuzicBox 第 01 章主界面](images/article-01/01-home-screen.png)。

### 本次理解

- `Activity` 是用户进入应用后直接看到并交互的界面组件。
- `AndroidManifest.xml` 用来声明应用组件和启动入口。
- 布局与文案放进资源目录后，Java 代码不用承担界面排版工作。
- Git 标签可以把每章文章和当时的代码准确对应起来。
- “构建成功”“静态检查通过”和“真机运行通过”是三种不同层级的验证，记录时不能混写。
