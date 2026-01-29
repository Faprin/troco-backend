package com.toco_backend.users_backend.modules.transaction.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.toco_backend.users_backend.modules.user.model.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

// @Entity
// @Builder
// @Table(name = "transaction")
// @NoArgsConstructor
// @AllArgsConstructor
public class TransactionEntity {
    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // --- PARTES IMPLICADAS --- 
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private UserEntity requester;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reciver_id")
    private Long reciverId;
    
    // --- NOTA DE APERTURA --- 
    @NotNull
    @Column(name = "proposal_note", nullable=false)
    private String proposalNote;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;
    

    // --- METADATOS ---
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
 
    @UpdateTimestamp
    @Column(name = "lasta_modified", nullable = false)
    private LocalDateTime updateAt;

    public Long getId() {
        return this.id;
    }*/
}
