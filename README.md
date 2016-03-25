# springboot-mysql-binlog
  最近有个项目，需要将mysql数据进行聚合，再导入到redis当中。最开始的方案是在业务逻辑中控制，当向mysql写入数据时，
  通过异步的方式，向聚合服务发一条消息，聚合服务再更新数据到redis当中。
  考虑到埋点比较多，容易出错。因此考察通过监控binlog来充当消息中介功能。通过测试表明，这种方案还是靠谱的。
  优点有如下：
 * 不需要埋点，原有的业务没有任何影响。
 * 减少了服务之间的藕合

 　好吧，下面说一下步骤。

 ## 开启binlog服务
```
[mysqld]
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock
user=mysql
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0
default-character-set=utf8
log_bin=mysql-bin
expire_logs_days=4
sync_binlog=1
binlog_format=mixed
server-id=1
binlog-do-db=test_db

```
 ## 开启权限
### 创建用户
```
create user binlog@'%' identified by 'binlog';
```
### 设置权限
```
# Grant access rights:
GRANT SELECT, PROCESS, FILE, SUPER, REPLICATION CLIENT, REPLICATION SLAVE, RELOAD ON *.* TO binlog@'%';
```
### 启动
```
Flush Privileges; 
```

## 在线监控binlog
 通过mysql-binlog-connector-java是中间件，获取实时binlog信息。
该中间件的github地址为(https://github.com/shyiko/mysql-binlog-connector-java.git)[https://github.com/shyiko/mysql-binlog-connector-java.git]

### 依赖包
加入依赖即可。
```
compile('com.github.shyiko:mysql-binlog-connector-java:0.3.1')
```
### 控制程序
下面是实际的测试程序，已调试通过。
```
	@Bean
	public String test() throws IOException{
		BinaryLogClient client = new BinaryLogClient("192.168.1.1", 3306, "binlog", "binlog");
		client.registerEventListener(new EventListener() {

		    @Override
		    public void onEvent(Event event) {
		    	if(event.getHeader().getEventType() == EventType.QUERY){
					QueryEventData data = event.getData();
					System.out.println(data.toString());  // 这行有惊喜
				}
		    }
		});
		client.connect();
		
		return null;
	}
```
实际使用过程中，使用正则表达式进行解析sql语句即可。还是比较方便的。

  
