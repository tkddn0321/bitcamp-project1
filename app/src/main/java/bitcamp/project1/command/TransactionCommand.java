package bitcamp.myapp.command;

import bitcamp.myapp.util.Prompt;
import bitcamp.myapp.vo.Transaction;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class TransactionCommand {

    LinkedList transactions = new LinkedList(); // Using raw LinkedList

    public void executeTransactionCommand(String type, String command) {
        System.out.printf("[%s]\n", command);
        switch (command) {
            case "등록":
                this.addTransaction(type);
                break;
            case "목록":
                this.listTransaction();
                break;
            case "조회":
                this.viewTransaction();
                break;
            case "변경":
                this.updateTransaction();
                break;
            case "삭제":
                this.deleteTransaction();
                break;
        }
    }

    public void showSummary() {
        for (Object obj : transactions.toArray()) {
            Transaction transaction = (Transaction) obj;
            System.out.printf("%s, %s, %s, %s, %s원\n",
                    transaction.getType(),
                    transaction.getCreatedDateFormatted(),
                    transaction.getCategory(),
                    transaction.getContent(),
                    formatAmount(transaction.getAmount()));
        }
    }

    private void deleteTransaction() {
        int transactionNo = Prompt.inputInt("번호?");
        Transaction transactionToRemove = null;
        for (Object obj : transactions.toArray()) {
            Transaction transaction = (Transaction) obj;
            if (transaction.getNo() == transactionNo) {
                transactionToRemove = transaction;
                break;
            }
        }
        if (transactionToRemove != null) {
            transactions.remove(transactionToRemove);
            System.out.printf("%d번을 삭제했습니다.\n", transactionToRemove.getNo());
        } else {
            System.out.println("해당 번호가 없습니다.");
        }
    }

    private void updateTransaction() {
        listTransaction();
        int transactionNo = Prompt.inputInt("번호?");
        Transaction transactionToUpdate = null;
        for (Object obj : transactions.toArray()) {
            Transaction transaction = (Transaction) obj;
            if (transaction.getNo() == transactionNo) {
                transactionToUpdate = transaction;
                break;
            }
        }
        if (transactionToUpdate != null) {
            transactionToUpdate.setCategory(Prompt.input("카테고리(%s)?", transactionToUpdate.getCategory()));
            transactionToUpdate.setContent(Prompt.input("내용(%s)?", transactionToUpdate.getContent()));
            transactionToUpdate.setAmount(Prompt.inputInt("금액(%,d)?", transactionToUpdate.getAmount()));
            System.out.println("변경했습니다.");
        } else {
            System.out.println("해당 번호가 없습니다.");
        }
    }

    private void addTransaction(String type) {
        Transaction transaction = new Transaction();

        transaction.setType(type);
        try {
            transaction.setCreatedDate(Prompt.input("작성일(yyyy-MM-dd)?"));
        } catch (Exception e) {
            System.out.println("날짜 형식이 올바르지 않습니다.");
            return;
        }
        transaction.setCategory(Prompt.input("카테고리?"));
        transaction.setContent(Prompt.input("내용?"));
        transaction.setAmount(Prompt.inputInt("금액?"));
        transaction.setNo(Transaction.getNextSeqNo());
        this.transactions.add(transaction);
        System.out.println("등록 했습니다.");
    }

    private void listTransaction() {
        System.out.println("번호 구분 작성일 카테고리 내용 금액");
        for (Object obj : transactions.toArray()) {
            Transaction transaction = (Transaction) obj;
            System.out.printf("%d, %s, %s, %s, %s, %s원\n",
                    transaction.getNo(), transaction.getType(), transaction.getCreatedDateFormatted(),
                    transaction.getCategory(), transaction.getContent(), formatAmount(transaction.getAmount()));
        }
    }

    private void viewTransaction() {
        listTransaction();
        int transactionNo = Prompt.inputInt("번호?");
        Transaction foundTransaction = null;
        for (Object obj : transactions.toArray()) {
            Transaction transaction = (Transaction) obj;
            if (transaction.getNo() == transactionNo) {
                foundTransaction = transaction;
                break;
            }
        }
        if (foundTransaction != null) {
            System.out.printf("번호: %d\n", foundTransaction.getNo());
            System.out.printf("구분: %s\n", foundTransaction.getType());
            System.out.printf("작성일: %s\n", foundTransaction.getCreatedDateFormatted());
            System.out.printf("카테고리: %s\n", foundTransaction.getCategory());
            System.out.printf("내용: %s\n", foundTransaction.getContent());
            System.out.printf("금액: %s원\n", formatAmount(foundTransaction.getAmount()));
        } else {
            System.out.println("해당 번호가 없습니다.");
        }
    }

    private String formatAmount(int amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    public int calculateTotalAmount() {
        int totalAmount = 0;
        for (Object obj : transactions.toArray()) {
            Transaction transaction = (Transaction) obj;
            totalAmount += transaction.getAmount();
        }
        return totalAmount;
    }
}
