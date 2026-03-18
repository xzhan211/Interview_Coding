import java.util.*;


public class TransactionProcessorApp {

      static class Amount {
        long totalAmount;
        long totalWithdrawalAmount;
        Amount(long totalAmount, long totalWithdrawalAmount) {
            this.totalAmount = totalAmount;
            this.totalWithdrawalAmount = totalWithdrawalAmount;
        }

         public long getTotalAmount() {
             return totalAmount;
         }

         public long getTotalWithdrawalAmount() {
            return totalWithdrawalAmount;
         }
     }

    private static final String DEPOSIT = "1010";
    private static final String WITHDRAWAL = "1020";
    private static final String TRANSFER = "2010";
    private static final long MAX_DEPOSIT = 100000;
    private static final long MAX_WITHDRAWAL = 40000;
    private static final long MAX_TOTAL_WITHDRAWAL = 60000;

    private final Map<String, Amount> accountToMoney = new HashMap<>();

    public void processTransactions(String[] transactions) {
        System.out.println("Starting Transaction Processor:");
        System.out.println("Start Inputs:");

        for (String transaction : transactions) {
//            System.out.println(transaction);

            String op = transaction.substring(0, 4);

            String srcAccountLen;
            String destAccountLen = "";
            int srcLen = 0;
            int destlen = 0;
            int srcStartIdxOfAmount = 0;
            int destStartIdxOfAmount = 0;
            String srcAccount = "";
            String destAccount = "";
            long amount = 0l;

            int idx = 4;
            if(op.equals(TRANSFER)) {
                srcAccountLen = transaction.substring(idx, idx + 2);
                idx += 2;
                srcLen = Integer.valueOf(srcAccountLen);
                srcAccount = transaction.substring(idx, idx + srcLen);
                idx += srcLen;
                destAccountLen = transaction.substring(idx, idx + 2);
                idx += 2;
                destlen = Integer.valueOf(destAccountLen);
                destAccount = transaction.substring(idx, idx + destlen);
                idx += destlen;
                amount = Long.valueOf(transaction.substring(idx));
            } else {
                srcAccountLen = transaction.substring(4,6);
                srcLen = Integer.valueOf(srcAccountLen);
                srcStartIdxOfAmount = 6+srcLen;
                srcAccount = transaction.substring(6, srcStartIdxOfAmount);
                amount = Long.valueOf(transaction.substring(srcStartIdxOfAmount));
            }


            if(op.equals(DEPOSIT)) {

                System.out.println(srcAccount + " --- " + amount);

                if(amount > MAX_DEPOSIT) System.err.println("deposit amount should not greater than " + MAX_DEPOSIT);
                else {
                    if(!accountToMoney.containsKey(srcAccount)) {
                        accountToMoney.put(srcAccount, new Amount(0l, 0l));
                    }
                    Amount amt = accountToMoney.get(srcAccount);
                    amt.totalAmount += amount;
                }
            }else if(op.equals(WITHDRAWAL)) {
                if(!accountToMoney.containsKey(srcAccount)) {
                    System.err.println("account doesn't exist");
                    continue;
                }

                if(amount > MAX_WITHDRAWAL) {
                    System.err.println("withdrawal amount should not greater than " + MAX_WITHDRAWAL);
                    continue;
                }



                Long balance = accountToMoney.get(srcAccount).totalAmount;

                if(balance < amount) {
                    System.err.println("account doesn't have enough money");
                } else {
                    if(accountToMoney.get(srcAccount).totalWithdrawalAmount + amount > MAX_TOTAL_WITHDRAWAL) {
                        System.err.println("total withdrawal amount should not greater than " + MAX_TOTAL_WITHDRAWAL);
                        continue;
                    }
                    Amount amt = accountToMoney.get(srcAccount);
                    amt.totalWithdrawalAmount += amount;
                    amt.totalAmount -= amount;
                }
//                    accountToMoney.put(account, balance - amount);
            } else if(op.equals(TRANSFER)) {
                if(!accountToMoney.containsKey(srcAccount)) {
                    System.err.println("source account doesn't exist");
                    continue;
                }
                if(amount > MAX_WITHDRAWAL) {
                    System.err.println("transfer amount should not greater than " + MAX_WITHDRAWAL);
                    continue;
                }
                Amount srcAmt = accountToMoney.get(srcAccount);
                if(srcAmt.totalAmount < amount) {
                    System.err.println("source account doesn't have enough money");
                    continue;
                }
                if(srcAmt.totalWithdrawalAmount + amount > MAX_TOTAL_WITHDRAWAL) {
                    System.err.println("total withdrawal amount should not greater than " + MAX_TOTAL_WITHDRAWAL);
                    continue;
                }
                srcAmt.totalAmount -= amount;
                srcAmt.totalWithdrawalAmount += amount;
                if(!accountToMoney.containsKey(destAccount)) {
                    accountToMoney.put(destAccount, new Amount(0l, 0l));
                }
                accountToMoney.get(destAccount).totalAmount += amount;
            } else {
                System.err.println("input operation is wrong.");
            }
        }
        System.out.println("End Inputs:");
    }

    public Map<String, Amount> getAccountMap() {
        return accountToMoney;
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");

        String[] transactions = {
            "1010064447770000100000",  // deposit 100000 to 444777
            "1010064447770000050000",  // deposit 50000 to 444777
            "1020064447770000020000",  // withdraw 20000 from 444777
            "2010064447770612345670000010000"  // transfer 10000 from 444777 to 1234567
        };

        TransactionProcessorApp app = new TransactionProcessorApp();
        app.processTransactions(transactions);

        System.out.println("\nFinal Balances:");
        app.getAccountMap().forEach((account, amt) ->
            System.out.println("  " + account + ": " + amt.getTotalAmount()));
    }
}



