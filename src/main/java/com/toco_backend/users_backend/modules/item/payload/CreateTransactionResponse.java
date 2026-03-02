package com.toco_backend.users_backend.modules.item.payload;

import com.toco_backend.users_backend.modules.transaction.model.TransactionStatus;
import com.toco_backend.users_backend.modules.user.model.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTransactionResponse {

    private String proposalNote;
    private UserEntity owner;
    private UserEntity requester;
    private TransactionStatus status;
}
