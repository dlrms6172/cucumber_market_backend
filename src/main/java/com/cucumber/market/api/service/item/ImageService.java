package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.image.ImageDto;
import com.cucumber.market.api.mapper.item.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageMapper imageMapper;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final String bucketFolder = "Item";

    @Transactional
    public List<String> addImages(Integer itemId, List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();

        files.forEach(file -> {

            String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());  //확장자 제거한 기존 이미지 이름
            String contentType = file.getContentType();

            String keyName = createKeyName(fileName);  //S3 bucket 에 저장될 고유한 key 이름 생성

            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(keyName)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .build();

            try {

                s3Client.putObject(putOb, RequestBody.fromBytes(file.getBytes()));  //S3 bucket 에 이미지 파일 저장

                //DB 에 이미지 정보 저장
                ImageDto imageDto = new ImageDto();
                imageDto.setItemId(itemId);
                imageDto.setOriginalName(fileName);
                imageDto.setKeyName(keyName);

                imageMapper.insertImage(imageDto);

                //S3 bucket 에 저장된 이미지 url 프론트에 반환
                String imageUrl = getUrlFromS3Bucket(keyName);
                imageUrls.add(imageUrl);

            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        });

        return imageUrls;
    }


    public String createKeyName(String fileName) {
        String uniqueName = fileName + UUID.randomUUID();

        return bucketFolder + "/" + uniqueName;
    }


    //S3 bucket url 형식으로 변환
    public String getUrlFromS3Bucket(String keyName) {
        GetUrlRequest request = GetUrlRequest.builder().bucket(bucket).key(keyName).build();
        String imageUrl = s3Client.utilities().getUrl(request).toExternalForm();

        return imageUrl;
    }


    //한 상품의 이미지들의 S3 bucket url 조회
    public List<String> getImageUrls(Integer itemId) {
        List<String> imageUrls = new ArrayList<>();

        List<String> savedKeyNames = imageMapper.selectImageKeyNames(itemId);
        savedKeyNames.forEach(savedKeyName -> imageUrls.add(getUrlFromS3Bucket(savedKeyName)));

        return imageUrls;
    }


    /**
     * 상품 이미지 수정
     * @param itemId
     * @param unchangedImageUrls - 기존에 저장된 이미지들 중 사용자가 그대로 사용할 이미지들
     * @param files - 사용자가 새로 추가한 이미지들
     * @return  List<String> newImageUrls
     */
    @Transactional
    public List<String> updateImages(Integer itemId, List<String> unchangedImageUrls, List<MultipartFile> files) {
        List<String> newImageUrls = new ArrayList<>();

        List<String> savedKeyNames = imageMapper.selectImageKeyNames(itemId);  //기존에 저장된 이미지들 중
        for (String savedKeyName : savedKeyNames) {

            String url = getUrlFromS3Bucket(savedKeyName);

            if (!unchangedImageUrls.contains(url)) {  //사용자가 삭제한 이미지의 경우
                deleteImage(savedKeyName);
            } else {  //사용자가 그대로 사용한 이미지의 경우
                newImageUrls.add(url);
            }
        }

        //사용자가 새로 입력한 이미지는 S3 bucket 및 DB 에 저장
        if (files != null) {
            newImageUrls.addAll(addImages(itemId, files));
        }

        return newImageUrls;

    }


    @Transactional
    public void deleteImage(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);

        imageMapper.deleteImage(key);

    }


    //S3 bucket 에 저장된 이미지 삭제
    @Transactional
    public void deleteImages(Integer itemId) {
        List<String> savedKeyNames = imageMapper.selectImageKeyNames(itemId);
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

        imageMapper.deleteImages(itemId);
    }

}
