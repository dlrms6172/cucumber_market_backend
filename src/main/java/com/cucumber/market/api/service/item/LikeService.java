package com.cucumber.market.api.service.item;

import com.cucumber.market.api.mapper.item.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeMapper likeMapper;

    public void addLike(Integer itemId, Integer memberId) {
        likeMapper.insertLike(itemId, memberId);
    }

    public void deleteLike(Integer itemId, Integer memberId) {
        //좋아요 존재 및 좋아요 삭제 권한 확인
        likeMapper.selectLike(itemId, memberId).orElseThrow(IllegalArgumentException::new);
        likeMapper.deleteLike(itemId, memberId);
    }
}
