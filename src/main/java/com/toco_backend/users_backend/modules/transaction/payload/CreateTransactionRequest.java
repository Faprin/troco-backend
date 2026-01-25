package com.toco_backend.users_backend.modules.transaction.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {

    private Long targetItemId;
    private List<?> offeredItemsIds;
    private String initialMessage;

}
