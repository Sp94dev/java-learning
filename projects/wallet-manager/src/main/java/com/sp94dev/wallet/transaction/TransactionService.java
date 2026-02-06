package com.sp94dev.wallet.transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sp94dev.wallet.transaction.dto.TransactionStats;

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

    public TransactionStats getStats() {
        List<Transaction> allTransactions = repository.findAll();
        int totalTransactions = allTransactions.size();
        Map<String, Long> byType = allTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
        Map<Long, Double> valueByInstrument = allTransactions.stream().collect(
                Collectors.groupingBy(
                        Transaction::instrumentId, Collectors.summingDouble(t -> t.price() * t.quantity())));
        return new TransactionStats(totalTransactions, byType, valueByInstrument);

    }
}
