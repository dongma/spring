
# spring-seckill
概述：该项目是基于`SSM`整合实现在线商品的秒杀功能，后期为了提高项目的性能使用`Redis`进行性能优化，整个项目   
    是基于`Maven`创建的。秒杀系统的逻辑是，当用户点击商品列表进行秒杀操作的时候，会拿到当前系统的时间与商品   
    秒杀开始`startTime`和结束时间`endTime`进行比较，当商品秒杀未开始的时候，在详情页面显示距离秒杀开始的时间；   
    当已经在秒杀商品的范围内的时候，会执行秒杀操作并在秒杀的详情页显示秒杀成功的提示信息。   
- 相关文档的地址：   
  MyBatis官方文档：http://www.mybatis.org/mybatis-3/zh/index.html   
  MyBatis-Spring整合官方文档：http://www.mybatis.org/spring/  
  Spring官方文档地址：http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/
### 项目使用到的技术点：  
- MySQL数据库：在系统中主要使用两张数据表`seckill`(用于存放秒杀商品的信息，包括商品编号，商品名称，库存， 秒杀开始和结束时间)和`success_killed`(秒杀的详情表，主要包括秒杀商品的`id`,用户的电话号码，秒杀的时间，以及秒杀的状态)。
- MyBatis：`MyBatis`通过`Mapper`动态代理开发实现`DAO`层，借助于`MyBatis`的接口声明式开发，在`xml`文件中配置相应`sql`脚本将`sql`与`DAO`层进行分离，降低了整个项目的耦合度。同时与`Spring`容器整合，将`SqlSessionFactory`注入到`Spring Ioc`容器中,借助于`Spring`的包扫描机制，将`MyBatis`中所有的`Mapper`注入到`Ioc`中。
- Spring : 借助于`Spring`的`Ioc`容器，整合了`Service`层以及`Service`所有的依赖，降低整个系统的耦合度；声明式事务`Transactional`，为了保持整个秒杀操作的完整性，使用`Spring`事务进行处理。
- Spring MVC：1.`Spring MVC`中`DispatcherServlet`的配置；2.`Spring Restful`接口的设计与使用，良好的接口设计更易于理解，也更适于团队集体开发；3.`Spring Controller`的使用，`@RequestMapping`的配置规则，`GET`和`POST`方式不同的 配置,`Spring`借助于`@ResponseBody`返回`JSon`格式的数据与前台进行交互；4.`ViewResolver`：借助于视图解析器将返回的逻辑视图转换为请求转发（或者重定向）的地址。 
- Redis：为了提高对热点商品的访问速度，在`DAO`层加入`Redis`缓存为了提高访问商品的效率。对于热点商品的第一次访问时，会将其`entity`实体对象序列化到`Redis`中，当第二次访问的时候，直接从`Redis`中取出商品信息，减少对数据库的访问以提高整个系统的效率。
- Bootstrap与JQuery：数据的呈现使用`Bootstrap`进行展示，在秒杀操作中需要获取系统的时候执行秒杀，需要时用`Ajax`进行获取系统时间。`Jquery`的模块化开发，将整个的操作封装到一个`JS`对象中，类似于`Java`中包的作用。另外为了界面显示美观以及在`cookie`中存取值，使用到一些`jquery-countdown`和`jquery-cookie`的插件。 
