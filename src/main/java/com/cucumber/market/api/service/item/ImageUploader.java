package com.cucumber.market.api.service.item;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;

    public Map<String, String> uploadImage(String bucketFolder, MultipartFile file) {

        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());  //확장자 제거한 기존 이미지 이름
        String keyName = createKeyName(bucketFolder, fileName);  //S3 bucket 에 저장될 고유한 key 이름 생성

        //이미지 정보
        Map<String, String> imageInfo = new HashMap();
        imageInfo.put("originalName", fileName);
        imageInfo.put("keyName", keyName);

        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucket)
                .key(keyName)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

        try {
            s3Client.putObject(putOb, RequestBody.fromBytes(file.getBytes()));  //S3 bucket 에 이미지 파일 저장

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageInfo;
    }


    public String createKeyName(String bucketFolder, String fileName) {
        String uniqueName = fileName + UUID.randomUUID();

        return bucketFolder + "/" + uniqueName;
    }


    //S3 bucket url 형식으로 변환
    public String getUrlFromS3Bucket(String keyName) {
        GetUrlRequest request = GetUrlRequest.builder().bucket(bucket).key(keyName).build();
        String imageUrl = s3Client.utilities().getUrl(request).toExternalForm();

        return imageUrl;
    }


    public void deleteImage(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }


    public void deleteImages(List<String> savedKeyNames) {
        List<ObjectIdentifier> keys = new ArrayList<>();

        for (String savedKeyName : savedKeyNames) {
            ObjectIdentifier objectId = ObjectIdentifier.builder()
                    .key(savedKeyName)
                    .build();

            keys.add(objectId);
        }

        Delete del = Delete.builder()
                .objects(keys)
                .build();


        DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(del)
                .build();

        s3Client.deleteObjects(multiObjectDeleteRequest);
    }

}
