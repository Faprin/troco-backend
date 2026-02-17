package com.toco_backend.users_backend.modules.transaction.payload;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTransactionRequest {
    private Long targetId;
    private List<Long> offeredItems;
    private String proposalNote;
}
