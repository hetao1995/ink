对于一个技术人员来说，有一个自己的个人博客应该是蛮必要的，可以在博客上记录自己的学习过程，分享讨论自己的观点，想想就觉得是一件有意思的事情。Ink就是这样一个简单易用的博客系统，后台采用springboot+thymeleaf+mybatis+ehcache开发，用nginx进行动静分离，前端用的是[Tale](https://github.com/otale/tale)博客前端，采用的是docker进行部署。
# 1. 为什么要写个人博客
自己写一个个人博客的想法由来已久，原本用hexo在github上采用[yilia模板](https://github.com/litten/hexo-theme-yilia)搭建了一个[静态博客](http://tao.he.cn/)，但是因为是静态博客，评论这些都需要依赖第三方组件，而这些第三方组件常常跑路，虽然最后采用了基于github issue的[gitalk](https://github.com/gitalk/gitalk)，但是gitalk需要github账号才能够评论，不支持匿名评论。而且hexo需要在本地写好markdown然后发布，需要一套hexo的环境，这对于我这种宿舍、教研室、公司到处跑的人来说很不友好，需要在每一台电脑上搭建环境。正好我有一个闲置的腾讯云服务器和几个域名，就想着能不能搭建一个能够浏览器编辑和发布的博客系统，正好上一家公司的事情告一段落了，下一家公司的事情还要一段时间开始，想着就趁这一段时间把这个博客搭建起来。
# 2. Ink采用的技术
最先我想的是直接在github上面找一个个人博客系统安装上就完事儿，对比了一些博客的界面，我觉得[Tale](https://github.com/otale/tale)最符合我的心意，它的界面简单美观，功能也比较齐全。本来想着直接用它搭建，但是发现他这个博客用的是Blade框架，是一个蛮小众的java框架了，以后增加自己的需求可能比较麻烦，就准备用springboot+thymeleaf二次开发，用mysql自带的全文索引做搜索，用Ehcache缓存，并且采用jwt登录替代session。原本以为一周时间就搞定，结果这一个月公司和毕设开题的事情比较多，愣是搞了接近一个月才把原本Tale的功能全部实现，正好到元旦前搞定，在这个过程中，参考了同样用Tale作为模板的[My-Blog](https://github.com/ZHENFENG13/My-Blog)以及其他优秀的开源项目。
# 3. 后续可能会添加的功能
现在的这个博客只是一个雏形，后面还要添加其他的功能，包括导出SQL和上传文件备份，浏览信息统计。还可能会写一个批量将hexo中的markdown导入到博客系统中的功能，这些功能都会陆续实现，但是最近一个月老师那边事情比较多，可能没啥时间了，寒假的时候看看有没有时间实现这些功能吧。
# 4. 如何使用Ink
使用ink非常简单，只需要安装docker和docker-compose，从[github](https://github.com/hetao1995/ink)上下载源码，然后运行即可。
```bash
git clone https://github.com/hetao1995/ink.git
cd docker
./start.sh
```  
等待docker-compose运行完毕之后浏览器访问ip/install按照步骤输入信息即可，全程不需要做更多的操作。如果需要关闭ink,在项目docker目录下输入./stop.sh即可关闭容器并删除镜像。
