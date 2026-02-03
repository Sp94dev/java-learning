package com.sp94dev.wallet.transaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTransactionRepository {
    private final Map<Long, Transaction> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Transaction save(Transaction transaction) {
        Long id = idCounter.getAndIncrement();
        Transaction newTransaction = new Transaction(
                id,
                transaction.instrumentId(),
                transaction.type(),
                transaction.quantity(),
                transaction.price(),
                transaction.date());
        storage.put(id, newTransaction);
        return newTransaction;
    }

    public List<Transaction> findAll() {
        return List.copyOf(storage.values());
    }
}
