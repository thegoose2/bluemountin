## 感谢你的浏览，这是我第一次独立完成一个完整的项目，如果有什么问题，还请多多包涵与指出。

## 项目名称：bolock（个人博客系统）

## 🔧 技术栈
- 后端：Spring Boot 3.2.4，MyBatis
- 前端：Thymeleaf，HTML/CSS/JavaScript，Markdown
- 数据库：MySQL，Redis
- 构建工具：Maven
- 工具：IDEA、Postman、Navicat

## 项目介绍
-Components文件下存放了两个组件，MarkdownUtils是将 Markdown 格式的文本转换成 HTML 格式的字符串，这样可以使得文章在html界面可以正常显示;
MypersistentTokenRepository则是和“记住我”功能相关的，在原来的基础上，改用Redis来储存token，这个类的作用就是自定义如何存储、更新、获取、 
删除这些 token 信息。

-config文件下存放了许多的服务设置：ArticleService是有关文章的，包括增删改查和一些其他的功能；CommentService是控制评论的读写的；favoriteService
是控制收藏功能的；followService是控制关注功能的；login则是控制用户的登录和权限分配的;MessagesService是控制私信功能的；WebMvcConfig是把你本地文件夹
里的图片或文件，通过 HTTP 接口公开访问。

-controller 是项目的 控制器层，负责处理来自前端的 HTTP 请求，是整个项目的接口入口。每个 @Controller 或 @RestController 类对应一个功能模块：
ArticleController是控制对文章的操作请求的；CommentController是接收有关评论的请求；favorite,follow,message的功能与config中一致；
Web控制器则是控制了所有网页的跳转，模版的载入等诸多功能。

-Entety是存刚实体类的文件夹，实体类用于映射数据库表结构，是项目中的数据模型核心。每一个实体类通常对应数据库中的一张表，负责封装数据记录。除了user_roles
这个数据库没有创建实体类。

-Mapper文件是数据访问层，用于操作数据库。每个 Mapper 接口对应一张或多张数据库表，定义了该表相关的 增删改查 方法，具体方法与上述两个文件相同，
具体的SQL操作语句则是放在了resources文件下的mapper文件夹里，对应的文件与Mapper里的同名。

-static文件下的css文件控制了所有的布局样式，编辑界面使用的是markdown，具体设置使用的是官方给出的文件，全都保存在editor文件里，img存放了一些修饰图。
js中，comment控制的是文章详情页的操作逻辑，也就是点击阅读全部后，打开的界面都是由他控制的，除此之外的都是uploadavatar在控制。

-templates下的fragments是用于存放 Thymeleaf 模板片段（Fragment） 的地方，fragments 文件夹用于存放 Thymeleaf 可复用模板片段，例如
页头（header）、侧边栏（sidebar）、消息列表等。这样可以避免在多个页面中重复编写相同的 HTML 内容，提升代码可维护性与复用性。

## 一些说明
这个项目的大多数控制逻辑都输基于MySQL数据库进行操作的，用Redis存放记住我的token，已经实现了将token存入数据库，但是好像
并没能实现自动登录，后面因时间问题便没有继续探究

## 心得与体会
通过这次锻炼，我开始慢慢熟悉springboot，thymeleaf，mybatis等框架，对数据库的操作和使用又有了新的理解，许多看似很复杂的功能都可以通过数据库的操作
来实现，当然这不一定是最优解。在梳理完了业务逻辑后，开始动手时发现，耗费自己精力做多的是thymeleaf的模版的调试，和加载这些模版的控制器，经常因为某个值为null
而导致页面加载不出来。到最后，发现，自己的许多功能或者说几乎所有的功能都是使用数据库操作实现的，其他的一些技术都没有使用上，并且，对于排序，一键通过等功能
自己也没来得及实现。对于我来说，这次收获最大的便是对springboot框架的理解又加深了，在后端开发学习这条路上，道阻且长；