package com.eminyidle.checklist.controller;

import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.dto.req.UpdateItemReq;
import com.eminyidle.checklist.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/checklist")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;
    private final String HEADER_USER_ID = "UserId";

    @GetMapping("/{tourId}")
    ResponseEntity<List<ChecklistItemDetail>> searchChecklist(@RequestHeader(HEADER_USER_ID) String userId, @PathVariable String tourId) {
        log.debug("tourId "+ userId);
        return ResponseEntity.ok(checklistService.searchItemList(userId, tourId));
    }

    @PostMapping("/private")
    ResponseEntity<Void> createPrivateItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        log.debug("** create private "+checklistItem.toString());
        checklistService.createPrivateItem(userId, checklistItem);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/public")
    ResponseEntity<Void> createPublicItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        checklistService.createPublicItem(userId, checklistItem);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/item")
    ResponseEntity<Void> updateItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody UpdateItemReq updateItemReq) {
        checklistService.updateItem(userId, updateItemReq.getOldItem(), updateItemReq.getNewItem());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/item")
    ResponseEntity<Void> deleteItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        checklistService.deleteItem(userId, checklistItem);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check")
    ResponseEntity<Void> checkItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        checklistService.checkItem(userId, checklistItem);
        return ResponseEntity.ok().build();
    }
}
