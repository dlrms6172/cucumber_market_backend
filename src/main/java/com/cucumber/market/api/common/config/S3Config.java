package com.cucumber.market.api.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${cloud.aws.s3.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.s3.secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client() {

/*
      Read IAM role credentials on Amazon EC2 (배포 시 변경)

        return S3Client.builder()
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .region(Region.AP_NORTHEAST_2)
                .build();
*/

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretAccessKey);

        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

}
