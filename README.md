# mybatis-generator-plugin

## 已增强的功能
0. 敏感信息自动加密
1. 自动增加@Mapper注解
2. 多生成一个以Ext结尾的Mapper类和xml文件，用于写生成的sql以外的sql语句。这两个文件不会因为重新生成而覆盖
3. insert和insertSelective方法添加`useGeneratedKeys="true" keyProperty="id"`
4. 增加batchInsert方法，批量插入。
5. 不会生成BLOBs的java对象，但是SQL中会拆出BLOBS字段，不影响性能查询。TODO

### 使用方式
pom.xml中对mybatis-generator-maven-plugin插件添加依赖
```
	<build>
		<plugins>
			<plugin>
				<groupId>org.mybatis.generator</groupId>
				<artifactId>mybatis-generator-maven-plugin</artifactId>
				<version>1.3.0</version>
				<configuration>
					<overwrite>true</overwrite>
				</configuration>
				<dependencies>
					<dependency>
						<groupId>mysql</groupId>
						<artifactId>mysql-connector-java</artifactId>
						<version>5.1.21</version>
					</dependency>
					<dependency>
						<groupId>club.usql</groupId>
						<artifactId>mybatis-generator-plugin</artifactId>
						<version>1.0-SNAPSHOT</version>
					</dependency>
					<dependency>
						<groupId>org.mybatis.generator</groupId>
						<artifactId>mybatis-generator-core</artifactId>
						<version>1.3.2</version>
					</dependency>
				</dependencies>
			</plugin>
		</plugins>
	</build>
```
generatorConfig.xml中添加plugin，具体请参照resources下的generatorConfig文件
```
<plugin type="club.usql.mybatis.generator.plugin.ExtXMLPlugin"/>
```

### 数据库加密
该模块用于，对数据敏感数据进行加密，如用户证件号，手机号等。
可在写入和更新数据库时对指定的列进行加密，在查询时对指定的列解密。

#### 使用方式
pom.xml
```
<dependency>
    <groupId>club.usql</groupId>
    <artifactId>mybatis-generator-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
以下以AES为例，具体代码可见AESCryptTypeHandler。
1. 单独指定列
在查询的resultMap中指定某一列，查询时可解密，如下：
```
<resultMap id="BaseResultMap" type="EncTest">
    <id column="ID" jdbcType="BIGINT" property="id" />
    <result column="CREATE_AT" jdbcType="TIMESTAMP" property="createAt" />
    <result column="CREATE_BY" jdbcType="VARCHAR" property="createBy" />
    <result column="MODIFY_AT" jdbcType="TIMESTAMP" property="modifyAt" />
    <result column="MODIFY_BY" jdbcType="VARCHAR" property="modifyBy" />
    <result column="CERT_NO" jdbcType="VARCHAR" property="certNo" typeHandler="club.usql.mybatis.generator.handler.AESCryptTypeHandler" />
    <result column="MOBILE_PHONE" jdbcType="VARCHAR" property="mobilePhone" typeHandler="club.usql.mybatis.generator.handler.AESCryptTypeHandler" />
</resultMap>
```
在插入时，对参数名指定，
```
<insert id="insert" parameterType="EncTest">
    insert into enc_test (ID, CREATE_AT, CREATE_BY, 
      MODIFY_AT, MODIFY_BY, CERT_NO, 
      MOBILE_PHONE
      )
    values (#{id,jdbcType=BIGINT}, #{createAt,jdbcType=TIMESTAMP}, #{createBy,jdbcType=VARCHAR}, 
      #{modifyAt,jdbcType=TIMESTAMP}, #{modifyBy,jdbcType=VARCHAR}, #{certNo,jdbcType=VARCHAR,typeHandler=club.usql.mybatis.generator.handler.AESCryptTypeHandler}, 
      #{mobilePhone,jdbcType=VARCHAR,typeHandler=club.usql.mybatis.generator.handler.AESCryptTypeHandler}
      )
  </insert>
```
如果大面积的铺这样的代码，也是一种体力活。
那么你可以在mybatis-generator的配置文件中，使用columnOverride，那么你生成的代码得到的就是上面列举的。
```
<table tableName="enc_test" domainObjectName="EncTest" enableCountByExample="false"
       enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
       selectByExampleQueryId="false">
    <columnOverride column="CERT_NO" javaType="String" jdbcType="VARCHAR" typeHandler="club.usql.mybatis.generator.handler.AESCryptTypeHandler"/>
    <columnOverride column="MOBILE_PHONE" javaType="String" jdbcType="VARCHAR" typeHandler="club.usql.mybatis.generator.handler.AESCryptTypeHandler"/>
</table>
```
2. 你可以使用AESString，那么不用指定列，mybatis会自动扫描这个类型的字段进行加密解密，如下
```
<table tableName="enc_test" domainObjectName="EncTest" enableCountByExample="false"
               enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"
               selectByExampleQueryId="false">
    <columnOverride column="CERT_NO" javaType="club.usql.mybatis.generator.type.AESString" jdbcType="VARCHAR" />
    <columnOverride column="MOBILE_PHONE" javaType="club.usql.mybatis.generator.type.AESString" jdbcType="VARCHAR" />
</table>
```
不过，还需要在应用配置application.yml中配置typeHandler
```
mybatis:
  type-handlers-package: club.usql.mybatis.generator.handler.auto
```
#### 已支持加密算法
AES 
