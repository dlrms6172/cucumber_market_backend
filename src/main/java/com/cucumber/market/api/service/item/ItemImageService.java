package com.cucumber.market.api.service.item;

import com.cucumber.market.api.dto.item.ItemDto;
import com.cucumber.market.api.mapper.item.ItemImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemImageService {

    private final String bucketFolder = "Item";
    private final ItemImageMapper itemImageMapper;
    private final ImageUploader imageUploader;

    @Transactional
    public List<String> addImages(Integer itemId, List<MultipartFile> files) {
        List<String> imageUrls = new ArrayList<>();

        files.forEach(file -> {

            Map<String, String> imageInfo = imageUploader.uploadImage(bucketFolder, file);  //S3에 이미지 저장

            ItemDto.itemImageDto imageDto = new ItemDto.itemImageDto();
            imageDto.setOriginalName(imageInfo.get("originalName"));
            imageDto.setKeyName(imageInfo.get("keyName"));
            imageDto.setItemId(itemId);

            itemImageMapper.insertImage(imageDto);  //DB에 이미지 정보 저장

            //S3 bucket 에 저장된 이미지 url 프론트에 반환
            String imageUrl = imageUploader.getUrlFromS3Bucket(imageDto.getKeyName());
            imageUrls.add(imageUrl);

        });

        return imageUrls;
    }


    //한 상품의 이미지들의 S3 bucket url 조회
    public List<String> getImageUrls(Integer itemId) {
        List<String> imageUrls = new ArrayList<>();

        List<String> savedKeyNames = itemImageMapper.selectImageKeyNames(itemId);
        savedKeyNames.forEach(savedKeyName -> imageUrls.add(imageUploader.getUrlFromS3Bucket(savedKeyName)));

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

        List<String> savedKeyNames = itemImageMapper.selectImageKeyNames(itemId);  //기존에 저장된 이미지들 중
        for (String savedKeyName : savedKeyNames) {

            String url = imageUploader.getUrlFromS3Bucket(savedKeyName);

            if (!unchangedImageUrls.contains(url)) {  //사용자가 삭제한 이미지의 경우
                deleteImage(savedKeyName);
            } else {  //사용자가 그대로 사용한 이미지의 경우
                newImageUrls.add(url);
            }
        }

        //사용자가 새로 입력한 이미지는 S3 bucket 및 DB 에 저장
        if (filesExist(files)) {
            newImageUrls.addAll(addImages(itemId, files));
        }

        return newImageUrls;
    }

    private boolean filesExist(List<MultipartFile> files) {
        return files != null;
    }

    @Transactional
    private void deleteImage(String key) {
        imageUploader.deleteImage(key);  //S3에서 이미지 삭제
        itemImageMapper.deleteImage(key);  //DB에서 이미지 정보 삭제
    }


    @Transactional
    public void deleteImages(Integer itemId) {
        List<String> savedKeyNames = itemImageMapper.selectImageKeyNames(itemId);
        imageUploader.deleteImages(savedKeyNames);  //S3에서 이미지들 삭제

        itemImageMapper.deleteImages(itemId);  //DB에서 이미지들 정보 삭제
    }

}
