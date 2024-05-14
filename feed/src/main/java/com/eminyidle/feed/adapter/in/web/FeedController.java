package com.eminyidle.feed.adapter.in.web;

import com.eminyidle.feed.adapter.dto.req.CreateFeedReq;
import com.eminyidle.feed.adapter.dto.res.GetFeedIdRes;
import com.eminyidle.feed.application.port.in.CreateFeedUsecase;
import com.eminyidle.feed.application.port.in.UpdateFeedUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
@Slf4j
public class FeedController {

    private final CreateFeedUsecase createFeedUsecase;
    private final UpdateFeedUsecase updateFeedUsecase;

    // 피드 생성
    @PostMapping
    public ResponseEntity<GetFeedIdRes> createFeed(@RequestHeader("UserId") String userId, @RequestBody CreateFeedReq createFeedReq) {
        log.info("userId: {}", userId);
        String feedId = UUID.randomUUID().toString();
        createFeedUsecase.createFeed(
                feedId, userId, createFeedReq.getFeedTitle(), createFeedReq.getFeedContent(), createFeedReq.getFeedThemeTagList(),
                createFeedReq.getFeedMateTag(), createFeedReq.getTourId(), createFeedReq.getHiddenDayList(),
                createFeedReq.getHiddenPlaceList(), createFeedReq.getHiddenActivityList(), createFeedReq.getHiddenItemList());

        return ResponseEntity.ok().body(GetFeedIdRes.builder()
                .feedId(feedId)
                .build());
    }

    // 피드 수정
    @PutMapping("/{feedId}")
    public void updateFeed(@RequestHeader("UserId") String userId, @PathVariable String feedId, @RequestBody CreateFeedReq createFeedReq) {
        log.info("피드 수정 : " + userId);
        updateFeedUsecase.updateFeed(
                feedId, userId, createFeedReq.getFeedTitle(), createFeedReq.getFeedContent(), createFeedReq.getFeedThemeTagList(),
                createFeedReq.getFeedMateTag(), createFeedReq.getTourId(), createFeedReq.getHiddenDayList(),
                createFeedReq.getHiddenPlaceList(), createFeedReq.getHiddenActivityList(), createFeedReq.getHiddenItemList());
    }
}
