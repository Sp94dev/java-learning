package com.sp94dev.wallet.transaction;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sp94dev.wallet.transaction.dto.TransactionResponse;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(transactionService.getAll().stream()
                .map(TransactionResponse::from)
                .toList());
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody Transaction transaction) {
        Transaction created = transactionService.create(transaction);
        TransactionResponse response = TransactionResponse.from(created);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
                
        return ResponseEntity.created(location).body(response);
    }
}
