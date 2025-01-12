package com.cucumber.market.api.service.user;

import com.cucumber.market.api.dto.user.UserDto;
import com.cucumber.market.api.mapper.user.ProfileImageMapper;
import com.cucumber.market.api.service.item.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private static final String BUCKET_FOLDER = "Profile";
    private final ProfileImageMapper profileImageMapper;
    private final ImageUploader imageUploader;

    @Transactional
    public String updateImage(UserDto.UserProfilePutDto dto) {
        String url = "";  //아무런 수정 사항 없으면 빈 문자열 반환

        MultipartFile file = dto.getProfileImage();
        Integer memberId = dto.getMemberId();

        if (dto.isDeletedOldProfileImage()) {  //기존 등록된 프로필 이미지 삭제
            deleteImage(memberId);
        }

        if (fileExists(file)) {  //사용자가 새로운 프로필 이미지를 등록한 경우
            url = addImage(memberId, file);  //새로운 프로필 이미지 등록
        }

        return url;
    }

    private boolean fileExists(MultipartFile file) {
        return file != null;
    }


    //프로필 이미지 조회
    public String getImageUrl(Integer memberId) {
        Optional<String> keyName = profileImageMapper.selectImageKeyName(memberId);

        if (keyName.isPresent()) {
            return imageUploader.getUrlFromS3Bucket("resized-" + keyName.get());
        }

        return "";
    }


    @Transactional
    private String addImage(Integer memberId, MultipartFile file) {
        Map<String, String> imageInfo = imageUploader.uploadImage(BUCKET_FOLDER, file);

        UserDto.ProfileImageDto imageDto = new UserDto.ProfileImageDto();
        imageDto.setOriginalName(imageInfo.get("originalName"));
        imageDto.setKeyName(imageInfo.get("keyName"));
        imageDto.setMemberId(memberId);

        profileImageMapper.insertImage(imageDto);

        return imageUploader.getUrlFromS3Bucket("resized-" + imageDto.getKeyName());
    }


    @Transactional
    private void deleteImage(Integer memberId) {
        Optional<String> savedKeyName = profileImageMapper.selectImageKeyName(memberId);

        if (savedKeyName.isPresent()) {  //기존 프로필 이미지가 있는 경우 삭제
            imageUploader.deleteImage(savedKeyName.get());
            imageUploader.deleteImage("resized-" + savedKeyName.get());
            profileImageMapper.deleteImage(memberId);
        }

    }

}
