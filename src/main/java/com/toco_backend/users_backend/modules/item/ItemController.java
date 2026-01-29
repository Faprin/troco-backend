package com.toco_backend.users_backend.modules.item;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toco_backend.users_backend.modules.item.model.ItemsSearchCriteria;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> listItems(ItemsSearchCriteria criteria, Pageable page) {

        if(criteria.getLatitude() == null || criteria.getLongitude() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(itemService.listItems(criteria, page));
    }
}
