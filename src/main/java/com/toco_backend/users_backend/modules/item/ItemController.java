package com.toco_backend.users_backend.modules.item;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toco_backend.users_backend.modules.item.payload.CreateItemRequest;
import com.toco_backend.users_backend.modules.item.payload.ItemChangeStatusRequest;
import com.toco_backend.users_backend.modules.item.payload.ItemDetailResponse;
import com.toco_backend.users_backend.modules.item.payload.ItemResponse;
import com.toco_backend.users_backend.modules.item.payload.ItemUpdateRequest;
import com.toco_backend.users_backend.modules.item.payload.ItemsSearchCriteria;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> listItems(ItemsSearchCriteria criteria,
            @PageableDefault(size = 20) Pageable page,
            Principal currentUser) {

        if (criteria.getLatitude() == null || criteria.getLongitude() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(itemService.listItems(criteria, page, currentUser.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(@PathVariable Long id) {

        return ResponseEntity.ok(itemService.getItemDetail(id));
    }

    @GetMapping("/user/{ownerUsername}")
    public ResponseEntity<List<ItemResponse>> getItemsOfUser(@PathVariable String ownerUsername, Principal requested) {
        return ResponseEntity.ok(itemService.getItemsOfUser(ownerUsername, requested.getName()));
    }

    @PostMapping()
    public ResponseEntity<ItemResponse> createItem(@RequestBody CreateItemRequest itemRequest, Principal user) {
        return ResponseEntity.ok(itemService.createItem(itemRequest, user.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody ItemUpdateRequest updateRequest,
            Principal currentUser) {
        return ResponseEntity.ok(itemService.updateItem(id, updateRequest, currentUser.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id, Principal currentUser) {
        itemService.deleteItem(id, currentUser.getName());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ItemResponse> changeItemStatus(@PathVariable Long id, @RequestBody ItemChangeStatusRequest request,
            Principal currentUser) {
                
        return ResponseEntity.ok(
                itemService.changeItemStatus(id, request.getStatus(), currentUser.getName()));
    }
}
