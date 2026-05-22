package com.toco_backend.users_backend.modules.transaction;

import com.toco_backend.users_backend.modules.item.ItemRepository;
import com.toco_backend.users_backend.modules.item.model.ItemEntity;
import com.toco_backend.users_backend.modules.item.payload.CreateTransactionResponse;
import com.toco_backend.users_backend.modules.transaction.model.TransactionEntity;
import com.toco_backend.users_backend.modules.transaction.payload.CreateTransactionRequest;
import com.toco_backend.users_backend.modules.user.UserRepository;
import com.toco_backend.users_backend.modules.user.model.UserEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public CreateTransactionResponse createTransaction(CreateTransactionRequest request, String requesterUsername){
        UserEntity requesterUser = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado"));

        ItemEntity targetItem = itemRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Ítem objetivo no encontrado"));
                
        if (targetItem.getOwner().getUsername().equals(requesterUsername)) {
            throw new RuntimeException("No puedes hacer una transacción por tu propio ítem");
        }

        UserEntity receiverUser = targetItem.getOwner();

        java.util.List<ItemEntity> offeredItems = itemRepository.findAllById(request.getOfferedItems());
        if (offeredItems.size() != request.getOfferedItems().size()) {
            throw new RuntimeException("Algunos de los ítems ofrecidos no existen");
        }
        
        for (ItemEntity offeredItem : offeredItems) {
            if (!offeredItem.getOwner().getUsername().equals(requesterUsername)) {
                throw new RuntimeException("No puedes ofrecer un ítem que no te pertenece");
            }
        }

        TransactionEntity transaction = TransactionEntity.builder()
                .requester(requesterUser)
                .reciver(receiverUser)
                .proposalNote(request.getProposalNote())
                .status(com.toco_backend.users_backend.modules.transaction.model.TransactionStatus.PENDING)
                .targetItem(targetItem)
                .offeredItems(offeredItems)
                .build();

        transactionRepository.save(transaction);

        return CreateTransactionResponse.builder()
                .proposalNote(transaction.getProposalNote())
                .owner(receiverUser)
                .requester(requesterUser)
                .status(transaction.getStatus())
                .build();
    }
}
