# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
mvn compile

# Run all tests
mvn test

# Run a single test method
mvn test -Dtest=TransactionProcessorAppTest#testDeposit

# Run the main demo
mvn exec:java
```

## Architecture

Single-class application (`TransactionProcessorApp`) with no external dependencies beyond JUnit 5.

**Transaction format** — strings are positionally encoded (no delimiters):
- Bytes 0–3: operation code (`1010` deposit, `1020` withdrawal, `2010` transfer)
- Bytes 4–5: source account ID length (2-digit number)
- Bytes 6–(6+len): source account ID
- For transfers only: next 2 bytes = dest account ID length, followed by dest account ID
- Remaining bytes: amount (zero-padded)

**Business rules** (constants in `TransactionProcessorApp`):
- Max single deposit: 100,000
- Max single withdrawal/transfer: 40,000
- Max cumulative withdrawals per account: 60,000

**State** — account balances are held in `Map<String, Amount>` where `Amount` tracks `totalAmount` and `totalWithdrawalAmount` separately. Transfers count against the source account's withdrawal limit.
