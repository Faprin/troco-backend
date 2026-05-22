package com.toco_backend.users_backend.modules.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toco_backend.users_backend.modules.transaction.payload.CreateTransactionRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionRequest request, java.security.Principal user){
        return ResponseEntity.ok(transactionService.createTransaction(request, user.getName()));
    }
}
