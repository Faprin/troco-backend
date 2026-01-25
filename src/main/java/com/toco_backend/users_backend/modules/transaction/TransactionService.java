package com.toco_backend.users_backend.modules.transaction;

import org.springframework.stereotype.Service;

import com.toco_backend.users_backend.modules.transaction.model.TransactionEntity;
import com.toco_backend.users_backend.modules.transaction.model.TransactionStatus;
import com.toco_backend.users_backend.modules.transaction.payload.CreateTransactionRequest;
import com.toco_backend.users_backend.modules.user.UserRepository;
import com.toco_backend.users_backend.modules.user.model.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    // private final ItemRepository itemRepository;

    /**
     * 
     * @param username
     * @param request
     * @return transaction id
     */
    /*public Long createTransaction(String username, CreateTransactionRequest request) {
        
        // el usuario ue la crea es el interesado
        UserEntity requester = getSafeUserByUsername(username);

        // el back debe de encontrar aquel usuario dueño de lo que quiero
        ItemEntity requestedItem = itemRepository.getItemById(); // or else...
        
        UserEntity reciver = requestedItem.getOwner();

        if(requester.getId().equals(receiver.getId())) 
            throw new RuntimeException("No puedes intercambiar cosas contigo mismo");
    

        TransactionEntity newTransaction = TransactionEntity.builder()
            .requester(requester)
            .reciver(reciver)
            .proposalNote(request.getInitialMessage())
            .status(TransactionStatus.PENDING)
            .build();

        transactionRepository.save(newTransaction);

        return newTransaction.getId();
    }

 */
    /** TODO MOVER A UTILS PORQUE SE USA EN VARIOS SITIOS
     * Si el usuario existe, lo devuelve. En caso contrario lanza un RuntimeException diciendo que el usuario 
     * no fue encontrado
     * @param username Nombre de usuario a buscar
     * @return Usuario en caso de que haya sido encontrado
     */
    public UserEntity getSafeUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
