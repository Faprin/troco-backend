package com.toco_backend.users_backend.modules.transaction;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // TODO terminar el metodo
    public TransactionEntity createTransaction(CreateTransactionRequest request){
        // debo de encontrar al otro usuario y el item en la bd
        ItemEntity item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new RuntimeException("Item no encontrado"));
        UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TransactionEntity transaction = TransactionEntity.builder()
            .item(item)
            .user(user)
            .build();
        
    }
}
