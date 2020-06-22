# xishi
xishi-eureka：注册中心 端口：8190

yml: local表示本地 test表示测试服 prod表示正式服

application-prodavaila.yml/application-prodavailb.yml：多节点部署项目2个注册中心相互注册


xishi-generator：代码生成

xishi-live-show：直播相关 端口：10106

xishi-movie：电影相关 端口：10105

xishi-plat：公共数据 端口：10103

xishi-socket：实时数据相关 端口：10108

xishi-user：用户相关 端口：10104


xishi-xxx-entity：接收/返回参数实体类 req包接收参数实体类 vo包返回参数实体类

xishi-xxx-rest：app接口代码

	controller：控制器
  
	config: 配置
  
	dao/mapper：接口mapper
  
	feign：调用其他模块接口
  
	model/pojo：实体类
  
	mq：发消息
  
	mqManager：处理消息
  
	service：服务实现层
  
	resources/sqlmap/mapper：mapper.xml
	
