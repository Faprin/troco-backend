package com.toco_backend.users_backend.modules.transaction;

import java.security.Principal;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toco_backend.users_backend.modules.transaction.payload.CreateTransactionRequest;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping()
    public ResponseEntity<?> createTransaction(Principal currentUser, @RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(Collections.singletonMap("transaction_id", service.createTransaction(currentUser.getName(), request)));
    } 
}
