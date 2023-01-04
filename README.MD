# 工程说明

lambda从s3捕获到s3-putobject的事件，函数负责将S3的元数据写入到到DynamoDB

## 工程环境配置

### 前置条件

1. 在本地客户端安装aws cli
2. 初始化aws configure 配置合适的IAM AK/SK
3. 安装SAM

### 清单说明

- template.yml 配置lambda定义，关于[SAM](https://github.com/awslabs/serverless-application-model/blob/master/versions/2016-10-31.md#awsserverlessfunction)
- sam build编译
- event/s3-put.json是本地调用的input playload
- 使用命令本地调用进行测试： sam local invoke -e event/s3-put.json

Lambda函数会从~/.aws/credentials获取相关的AK/SK并初始化到lambda 函数实例的环境变量

如果是AWS线上环境通过IAM Role绑定到函数，**任何时候都不要讲AK/SK硬编码到代码中**

## 工程依赖

通过maven管理依赖，核心依赖项目

```
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-core</artifactId>
      <version>1.2.0</version>
    </dependency>
    <dependency>
      <groupId>software.amazon.awssdk</groupId>
      <artifactId>dynamodb-enhanced</artifactId>
      <version>2.17.123</version>
    </dependency>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-lambda-java-events</artifactId>
      <version>2.2.7</version>
    </dependency>
    <dependency>
      <groupId>com.amazonaws</groupId>
      <artifactId>aws-java-sdk-s3</artifactId>
      <version>1.11.578</version>
    </dependency>
```
