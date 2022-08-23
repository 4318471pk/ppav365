package com.live.fox.entity;


public class OssToken {
    private String bucketName;
    private String endpoint;
    private String key;
    private String secret;
    private String token;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "OssToken{" +
                "bucketName='" + bucketName + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", key='" + key + '\'' +
                ", secret='" + secret + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

