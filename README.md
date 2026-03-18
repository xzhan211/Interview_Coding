# Transaction Processor

A Java application that parses and processes fixed-format bank transaction strings, enforcing deposit, withdrawal, and transfer business rules.

## Prerequisites

- **Java 17** or later — check: `java -version`
- **Maven 3.6+** — check: `mvn -v`

## Project layout

```
Interview_Coding/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── TransactionProcessorApp.java
│   │   └── resources/
│   │       └── input.txt
│   └── test/
│       └── java/
│           └── TransactionProcessorAppTest.java
└── README.md
```

## Usage

```java
TransactionProcessorApp app = new TransactionProcessorApp();
app.processTransactions(new String[]{ "1010064447770000100000", "1020064447770000020000" });
Map<String, TransactionProcessorApp.Amount> accounts = app.getAccountMap();
```

## Transaction format

Each transaction is a single string with no delimiters:

### Deposit / Withdrawal

```
[op:4][acctLen:2][acct:N][amount]
```

| Field     | Description                          |
|-----------|--------------------------------------|
| `op`      | `1010` = deposit, `1020` = withdrawal |
| `acctLen` | 2-digit zero-padded length of account number |
| `acct`    | Account number (N chars)             |
| `amount`  | Transaction amount (remaining chars) |

**Example:** `1010064447770000100000`
- op = `1010` (deposit)
- acctLen = `06`, acct = `444777`
- amount = `0000100000` = 100,000

### Transfer

```
[op:4][srcLen:2][src:N][destLen:2][dest:M][amount]
```

| Field     | Description                          |
|-----------|--------------------------------------|
| `op`      | `2010` = transfer                    |
| `srcLen`  | 2-digit length of source account     |
| `src`     | Source account number                |
| `destLen` | 2-digit length of destination account |
| `dest`    | Destination account number           |
| `amount`  | Transfer amount (remaining chars)    |

## Business rules

| Rule                          | Limit   |
|-------------------------------|---------|
| Max single deposit            | 100,000 |
| Max single withdrawal/transfer | 40,000 |
| Max total withdrawals per account | 60,000 |

- Withdrawals and transfers are rejected if the account doesn't exist or has insufficient balance.
- Transfers count against the source account's cumulative withdrawal limit.
- Depositing into a non-existent account creates it automatically.
- Transferring to a non-existent destination account creates it automatically.

## Commands

```bash
mvn compile        # compile
mvn test           # run all tests
```

The `testDepositAndWithdrawal` test reads transactions from `src/main/resources/input.txt`. Edit that file to test custom scenarios.
