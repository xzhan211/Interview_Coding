import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class TransactionProcessorAppTest {

    @Test
    void testDeposit() {
        String[] inputFileLines = new String[] {"1010064447770000100000", "1010064447770000050000", "10100712345670000200000"};
        TransactionProcessorApp app = new TransactionProcessorApp();
        app.processTransactions(inputFileLines);
        var map = app.getAccountMap();
        Assertions.assertEquals(150000, map.get("444777").getTotalAmount());
    }

    @Test
    void testWithdrawal() {
        String[] inputFileLines = new String[] {"1010064447770000100000", "1020064447770000040000", "10100712345670000200000"};
        TransactionProcessorApp app = new TransactionProcessorApp();
        app.processTransactions(inputFileLines);
        var map = app.getAccountMap();
        Assertions.assertEquals(60000, map.get("444777").getTotalAmount());
    }

    @Test
    void testWithdrawalWithMAX() {
        String[] inputFileLines = new String[] {"1010064447770000100000", "1020064447770000010000", "1020064447770000050000"};
        TransactionProcessorApp app = new TransactionProcessorApp();
        app.processTransactions(inputFileLines);
        var map = app.getAccountMap();
        Assertions.assertEquals(90000, map.get("444777").getTotalAmount());
    }



    @Test
    void testDepositAndWithdrawal() {
        String[] inputFileLines = loadFileLines();
        TransactionProcessorApp app = new TransactionProcessorApp();
        app.processTransactions(inputFileLines);
        var accountToMoney = app.getAccountMap();
        Assertions.assertEquals(110000, accountToMoney.get("444777").getTotalAmount());
        Assertions.assertEquals(10000, accountToMoney.get("234591").getTotalAmount());
        Assertions.assertEquals(25000, accountToMoney.get("1234567").getTotalAmount());
    }





    private static String[] loadFileLines() {
        try {
            BufferedReader in = new BufferedReader(
                    new FileReader("src/main/resources/input.txt"));
            String str;
            List<String> list = new ArrayList<>();
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            in.close();
            return list.toArray(new String[0]);
        } catch (IOException e) {
            System.out.println("Could not load file. " + e.getMessage());
        }
        return new String[0];
    }
}
