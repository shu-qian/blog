# 个人博客系统项目介绍

个人博客系统以单体架构入手，首先快速开发，不考虑项目优化，以降低开发负担。开发完成后开始优化项目，提升编程思维能力，例如页面静态化、缓存、云存储、日志等，并最终将其部署上线使用Docker。

整体项目组成为 Vue + Spring Boot + MyBatis Plus + Redis + MySQL。该系统将具备登录注册、发帖评论功能，并提供一个后台权限管理子系统。

## 内容与过程

1. **文章管理**
   - 实现文章的发布、编辑、删除等功能。
   - 能够展示全部文章、文章详情、最热文章、最新文章。
   - 支持文章归档，可以根据分类和标签展示文章。
   - 将图片、JS、CSS等静态资源存储于七牛云等 CDN 平台，提高网站访问速度。

2. **用户管理**
   - 支持用户注册、登录、权限管理等功能。
   - 使用 JWT 与 Redis 实现令牌认证和用户信息缓存。
   - 使用 MD5 加密，将 token 存放于 Redis 中以提高访问认证速度和安全性，但需处理令牌过期、续期、踢掉线等场景。
   - 添加登录拦截器实现统一登录判断，使用 ThreadLocal 替代 Session 完成保存用户登录信息功能。

3. **阅读数和评论管理**
   - 使用线程池更新阅读数，并用 CAS 乐观锁保证更新操作的原子性和线程安全性。
   - 使用 Redis 进行数据缓存和自增。
   - 允许用户对文章进行评论，能够展示评论列表。

4. **角色与权限管理**
   - 使用 Spring Security 实现权限系统，实现多用户多权限的授权管理，保障系统安全性和功能权限控制。

5. **其他优化**
   - 添加 AOP 完成统一日志记录，并使用 Spring Cache 进行统一缓存处理。
   - 在统一缓存后，文章 ID 出现了精度损失问题，通过在 Spring MVC 中配置消息转换器解决了 Long 类型响应精度丢失问题。
