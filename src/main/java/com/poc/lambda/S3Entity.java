package com.poc.lambda;

import java.time.Instant;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class S3Entity {
    String objKey;
    String bucketKey;
    String contentType ;
    long objSize ;
    String objEndcoding ;
    String objMd5 ; 
    Instant  eventDt;
    @DynamoDbPartitionKey
    public String getObjKey() {
        return objKey;
    }
    public void setObjKey(String objKey) {
        this.objKey = objKey;
    }
    public String getBucketKey() {
        return bucketKey;
    }
    public void setBucketKey(String bucketKey) {
        this.bucketKey = bucketKey;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public long getObjSize() {
        return objSize;
    }
    public void setObjSize(long objSize) {
        this.objSize = objSize;
    }
    public String getObjEndcoding() {
        return objEndcoding;
    }
    public void setObjEndcoding(String objEndcoding) {
        this.objEndcoding = objEndcoding;
    }
    public String getObjMd5() {
        return objMd5;
    }
    public void setObjMd5(String objMd5) {
        this.objMd5 = objMd5;
    }
    public Instant getEventDt() {
        return eventDt;
    }
    public void setEventDt(Instant eventDt) {
        this.eventDt = eventDt;
    }
    public Instant getCreateDt() {
        return createDt;
    }
    public void setCreateDt(Instant createDt) {
        this.createDt = createDt;
    }
    Instant  createDt;
}
