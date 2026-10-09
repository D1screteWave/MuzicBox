# MuzicBox 开发与文章路线

MuzicBox 采用“每一章完成一个可验证版本”的方式推进。功能、安全实践和文章都围绕同一份代码演进，避免把笔记写成与仓库脱节的知识清单。

| 章节 | 可交付功能 | Android / 安全知识 | 计划标签 |
| --- | --- | --- | --- |
| 01 | 最小可运行工程与启动页 | 工程结构、Activity、Manifest、资源、Intent 概览 | `v0.1-plan-and-project` |
| 02 | 本地音乐扫描、播放/暂停、上一首/下一首 | MediaStore、权限、ContentResolver、显式 Intent | `v0.2-local-player` |
| 03 | 后台音乐播放与 Service | Service、前台服务、BroadcastReceiver、音频焦点 | `v0.3-background-playback` |
| 04 | 在线音乐播放与网络请求 | SharedPreferences、网络权限、HTTP 接口、流媒体播放 | `v0.4-online-music` |
| 05 | 用 JNI 实现参数签名 | NDK、JNI、so、参数签名与服务端校验 | `v0.5-jni-signature` |
| 06 | 基础反调试与 Frida 检测 | 调试状态、进程特征、对抗边界与绕过思维 | `v0.6-anti-debug` |
| 07 | R8 混淆、签名与加壳实验 | R8 的代码压缩、优化与混淆，签名、完整性和加壳前后对比 | `v0.7-release-hardening` |

七篇正式文章完成后，再执行一次整体回归测试并创建 `v1.0.0` Release。最终结构、已知问题、阶段收获和后续计划写入 README、Release 说明或不编号的阶段后记，不再单独编号。

## 范围约束

- 使用 Java + XML，确保适合正在学习 Java 的 Android 初学者。
- 第二章只做最小播放器控制，不提前加入进度拖动和复杂播放队列。
- 第三章再引入后台播放，让 Service 和 BroadcastReceiver 出现在自然的业务场景中。
- 在线音乐只面向用户自己拥有或有权使用的服务器与音频内容。
- 安全部分用于理解保护边界，不把任何单一检测或加壳描述为“绝对安全”。

## 每章完成标准

1. 功能代码可以构建，核心路径经过验证。
2. README、学习日志与章节状态同步更新。
3. Git 提交聚焦，提交信息能说明本次变化。
4. 建立对应版本标签，方便读者切换代码。
