package com.sp94dev.wallet.transaction;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    List<Transaction> transactions = new ArrayList<>();

    @GetMapping()
    public List<Transaction> getAll() {
        return this.transactions;
    }

    @PostMapping()
    public String postMethodName(@RequestBody Transaction transaction) {
        this.transactions.add(transaction);

        return "Transaction added successfully";
    }

}
