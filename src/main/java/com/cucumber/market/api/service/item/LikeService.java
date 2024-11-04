package com.cucumber.market.api.service.item;

import com.cucumber.market.api.mapper.item.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeMapper likeMapper;

    public Map addLike(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        likeMapper.insertLike(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }


    public Map deleteLike(Integer itemId, Integer memberId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        Map like = likeMapper.selectLike(itemId, memberId).orElseThrow(IllegalArgumentException::new);  //좋아요 존재 및 좋아요 삭제 권한 확인

        likeMapper.deleteLike(itemId, memberId);
        result.put("itemId", itemId);

        return result;
    }

}
