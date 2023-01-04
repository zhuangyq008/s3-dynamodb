package com.poc.lambda;


import java.util.Date;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.SystemPropsUtil;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;



// Handler value: example.Handler
/**
 * @author enginez
 */
public class Handler implements RequestHandler<S3Event, String> {
  public Handler(){
    System.getenv().forEach((k,v)->{
      Console.log("env: {}={}", k,v);
    });
  }
  Gson gson = new GsonBuilder().setPrettyPrinting().create();
  @Override
  public String handleRequest(S3Event s3event, Context context) {
    try {
      Console.log("EVENT: " + gson.toJson(s3event));
      S3EventNotificationRecord record = s3event.getRecords().get(0);

      String srcBucket = record.getS3().getBucket().getName();

      // Object key may have spaces or unicode non-ASCII characters.
      String srcKey = record.getS3().getObject().getUrlDecodedKey();
      
      String dstBucket = srcBucket;
      String dstKey = "resized-" + srcKey;
      AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
      S3Object s3Object = s3Client.getObject(new GetObjectRequest(
              srcBucket, srcKey));
      String contentType = s3Object.getObjectMetadata().getContentType();
      long objSize = s3Object.getObjectMetadata().getContentLength();
      String objEndcoding = s3Object.getObjectMetadata().getContentEncoding();
      String objMd5 = s3Object.getObjectMetadata().getContentMD5();

      // put key to Dynamodb destination bucket
      final Region region = Region.US_EAST_1;
      DynamoDbClient ddb = DynamoDbClient.builder()
          .region(region)
          .build();
          S3Entity obj = new S3Entity();
          obj.setObjKey(srcKey);
          obj.setBucketKey(srcBucket);
          obj.setContentType(contentType);
          obj.setEventDt(new Date().toInstant());
          obj.setObjEndcoding(objEndcoding);
          obj.setObjMd5(objMd5);
          obj.setObjSize(objSize);
      DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
          .dynamoDbClient(ddb)
          .build();

          putRecord(enhancedClient,obj) ;
      Console.log("Successfully resized " + srcBucket + "/"
              + srcKey + " and uploaded to " + dstBucket + "/" + dstKey);
      return "Ok";
  } catch (Exception e) {

      throw new RuntimeException(e);
    }
  }
  public static void putRecord(DynamoDbEnhancedClient enhancedClient,S3Entity obj) {

    try {
        DynamoDbTable<S3Entity> custTable = enhancedClient.table("S3Entity", TableSchema.fromBean(S3Entity.class));

        Date dt = new Date();
        
        obj.setCreateDt(dt.toInstant());
        // Put the customer data into an Amazon DynamoDB table.
        custTable.putItem(obj);

    } catch (DynamoDbException e) {
        Console.error(e.getMessage());
    }
    System.out.println("S3 data added to the table.");
}
}
