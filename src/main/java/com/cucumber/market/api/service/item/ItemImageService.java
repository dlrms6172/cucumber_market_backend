package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.mapper.item.ItemImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Utilities;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemImageService {

    private static final String BUCKET_FOLDER = "Item";
    private final ItemImageMapper itemImageMapper;
    private final ImageUploader imageUploader;
    private final S3Utilities s3Utilities;

    public List<String> addImages(Integer itemId, List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            String imageUrl = addImage(itemId, files.get(i), i);
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }


    public String addImage(Integer itemId, MultipartFile file, Integer index) {
        Map<String, String> imageInfo = imageUploader.uploadImage(BUCKET_FOLDER, file);  //S3에 이미지 저장

        ItemDto.itemImageDto imageDto = new ItemDto.itemImageDto();
        imageDto.setOriginalName(imageInfo.get("originalName"));
        imageDto.setKeyName(imageInfo.get("keyName"));
        imageDto.setItemId(itemId);
        imageDto.setIndex(index);

        itemImageMapper.insertImage(imageDto);  //DB에 이미지 정보 저장

        //S3 bucket 에 저장된 이미지 url 프론트에 반환
        String imageUrl = imageUploader.getUrlFromS3Bucket(imageDto.getKeyName());
        return imageUrl;
    }


    //한 상품의 이미지들의 S3 bucket url 조회
    @Transactional(readOnly = true)
    public List<String> getImageUrls(Integer itemId) {
        List<String> imageUrls = new ArrayList<>();

        List<String> savedKeyNames = itemImageMapper.selectImageKeyNames(itemId);
        savedKeyNames.forEach(savedKeyName -> imageUrls.add(imageUploader.getUrlFromS3Bucket(savedKeyName)));

        return imageUrls;
    }


    @Transactional(readOnly = true)
    public List<String> getRepImageUrls(List<Integer> itemIdList) {
        List<String> repImageUrls = new ArrayList<>();

        itemIdList.forEach(itemId -> {
            String keyName = itemImageMapper.selectRepImageKeyName(itemId);
            String url = imageUploader.getUrlFromS3Bucket(keyName);
            repImageUrls.add(url);
        });

        return repImageUrls;
    }


    /**
     * 상품 이미지 수정
     * @param itemId
     * @param imageIndexList
     * @param files - 사용자가 새로 추가한 이미지들
     * @return  List<String> newImageUrls
     */
    public List<String> updateImages(Integer itemId, List<String> imageIndexList, List<MultipartFile> files) {
        deleteAllImages(itemId, imageIndexList);

        return generateNewImageUrls(itemId, imageIndexList, files);
    }


    private List<String> generateNewImageUrls(Integer itemId, List<String> imageIndexList, List<MultipartFile> files) {
        UrlValidator urlValidator = new UrlValidator();
        List<String> newImageUrls = new ArrayList<>();

        for (int i = 0; i < imageIndexList.size(); i++) {
            String s = imageIndexList.get(i);
            int fileIdx = 0;

            if (urlValidator.isValid(s)) {  //기존 이미지 url은 인덱스 업데이트
                URI uri = URI.create(s);
                String key = s3Utilities.parseUri(uri).key().orElseThrow();
                itemImageMapper.updateImageIndex(key, i);
                newImageUrls.add(s);
            } else {  //사용자가 새로 입력한 이미지는 S3 bucket 및 DB 에 저장

                newImageUrls.add(addImage(itemId, files.get(fileIdx), i));
                fileIdx++;
            }
        }

        return newImageUrls;
    }


    private void deleteAllImages(Integer itemId, List<String> imageIndexList) {
        List<String> savedKeyNames = itemImageMapper.selectImageKeyNames(itemId);  //기존에 저장된 이미지들 중

        for (String savedKeyName : savedKeyNames) {
            String url = imageUploader.getUrlFromS3Bucket(savedKeyName);

            if (!imageIndexList.contains(url)) {  //사용자가 삭제한 이미지의 경우
                deleteImage(savedKeyName);  //S3와 DB에서도 삭제
            }
        }
    }


    private void deleteImage(String key) {
        imageUploader.deleteImage(key);  //S3에서 이미지 삭제
        itemImageMapper.deleteImage(key);  //DB에서 이미지 정보 삭제
    }


    public void deleteAllImages(Integer itemId) {
        List<String> savedKeyNames = itemImageMapper.selectImageKeyNames(itemId);
        imageUploader.deleteImages(savedKeyNames);  //S3에서 이미지들 삭제

        itemImageMapper.deleteImages(itemId);  //DB에서 이미지들 정보 삭제
    }

}
