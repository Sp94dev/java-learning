package com.sp94dev.wallet.transaction;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final InMemoryTransactionRepository repository;

    public TransactionService(InMemoryTransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }
}
