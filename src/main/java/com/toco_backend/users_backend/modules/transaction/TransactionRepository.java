package com.toco_backend.users_backend.modules.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toco_backend.users_backend.modules.transaction.model.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    
}
