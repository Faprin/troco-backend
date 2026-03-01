package com.toco_backend.users_backend.modules.transaction;

import com.toco_backend.users_backend.modules.item.ItemRepository;
import com.toco_backend.users_backend.modules.item.model.ItemEntity;
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

    // TODO terminar el metodo
    public void createTransaction(CreateTransactionRequest request){
        // debo de encontrar al otro usuario y el item en la bd
        ItemEntity item = itemRepository.findById(request.getTargetId()).orElseThrow(() -> new RuntimeException("Item no encontrado"));
        UserEntity user = userRepository.findById(item.getOwner().getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        
    }
}
