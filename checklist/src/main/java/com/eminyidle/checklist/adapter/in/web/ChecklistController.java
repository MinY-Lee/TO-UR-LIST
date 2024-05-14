package com.eminyidle.checklist.adapter.in.web;

import com.eminyidle.checklist.domain.ChecklistItem;
import com.eminyidle.checklist.domain.ChecklistItemDetail;
import com.eminyidle.checklist.application.dto.req.UpdateItemReq;
import com.eminyidle.checklist.application.dto.res.CreateNewItemRes;
import com.eminyidle.checklist.application.port.ChecklistService;
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
        log.debug("searchChecklist by "+ userId);
        return ResponseEntity.ok(checklistService.searchItemList(userId, tourId));
    }

    @PostMapping("/private")
    ResponseEntity<CreateNewItemRes> createPrivateItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        log.debug("createPrivateItem: "+checklistItem.toString());
        CreateNewItemRes createPrivateItemRes=new CreateNewItemRes(checklistService.createPrivateItem(userId, checklistItem));
        return ResponseEntity.ok(createPrivateItemRes);
    }

    @PostMapping("/public")
    ResponseEntity<CreateNewItemRes> createPublicItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        log.debug("createPublicItem: "+checklistItem.toString());
        CreateNewItemRes createPublicItemRes=new CreateNewItemRes(checklistService.createPublicItem(userId, checklistItem));
        return ResponseEntity.ok(createPublicItemRes);
    }

    @PutMapping("/item")
    ResponseEntity<CreateNewItemRes> updateItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody UpdateItemReq updateItemReq) {
        log.debug("updateItem: "+updateItemReq.getOldItem().toString());
        log.debug("to "+updateItemReq.getNewItem().toString());
        CreateNewItemRes updateItemRes=new CreateNewItemRes(checklistService.updateItem(userId, updateItemReq.getOldItem(), updateItemReq.getNewItem()));
        return ResponseEntity.ok(updateItemRes);
    }

    @DeleteMapping("/item")
    ResponseEntity<Void> deleteItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        log.debug("deleteItem: "+checklistItem.toString());
        checklistService.deleteItem(userId, checklistItem);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/check")
    ResponseEntity<Void> checkItem(@RequestHeader(HEADER_USER_ID) String userId, @RequestBody ChecklistItem checklistItem) {
        log.debug("checkItem: "+checklistItem.toString());
        checklistService.checkItem(userId, checklistItem);
        return ResponseEntity.ok().build();
    }
}
