package com.sp94dev.wallet.transaction;

import java.util.List;

public interface TransactionRepository {
    
    Transaction save(Transaction transaction);
    
    List<Transaction> findAll();
    
}
