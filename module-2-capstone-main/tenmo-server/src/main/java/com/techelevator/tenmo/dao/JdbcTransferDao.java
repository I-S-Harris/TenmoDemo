package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String sendTransfer(int userFrom, int userTo, BigDecimal amount) {
        int accountNumFrom = findAccountById(userFrom).getAccountId();
        System.out.println(userFrom);
        int accountNumTo = findAccountById(userTo).getAccountId();
        System.out.println(userTo);
        System.out.println(accountNumFrom);
        System.out.println(accountNumTo);
        if (userFrom == userTo) {
            return "You can not send money to your self.";
        } else if (0 >= amount.intValue()) {
            return "Invalid amount. ";
        } else {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                    " VALUES (2, 2, ? , ?, ?)";
            jdbcTemplate.update(sql, accountNumFrom, accountNumTo, amount);
            addToBalance(amount, accountNumTo);
            subtractFromBalance(amount, accountNumFrom);
            return "Transfer complete";
        }
    }

        @Override
        public Account findAccountById ( int id){
            Account account = null;
            String sql = "SELECT * FROM account WHERE user_id = ?";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
            return account;
        }

        @Override
        public BigDecimal addToBalance (BigDecimal amountToAdd,int id){
            Account account = findAccountByAccountId(id);
            String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
            try {
                jdbcTemplate.update(sql, amountToAdd, account.getAccountId());
                System.out.println(account.toString());
            } catch (DataAccessException e) {
                System.out.println("Error accessing data");
            }
            return account.getBalance();
        }

        @Override
        public BigDecimal subtractFromBalance (BigDecimal amountToSubtract,int id){
            Account account = findAccountByAccountId(id);

            String sqlString = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
            try {
                jdbcTemplate.update(sqlString, amountToSubtract, account.getAccountId());
                System.out.println(account.toString());
            } catch (DataAccessException e) {
                System.out.println("Error accessing data");
            }
            return account.getBalance();
        }

        @Override
        public Account findAccountByAccountId ( int id){
            Account account = null;
            String sql = "SELECT * FROM account WHERE account_id = ?";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = mapRowToAccount(results);
                System.out.println(account.toString());
            }
            return account;

        }


        @Override
        public List<Transfer> getTransfersByUserId ( long userId){
            return null;
        }

        @Override
        public Transfer getTransferByTransferId ( long transferId){
            Transfer transfer = new Transfer();
            String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo, ts.transfer_status_desc, tt.transfer_type_desc FROM transfer t " +
                    "JOIN account a ON t.account_from = a.account_id " +
                    "JOIN account b ON t.account_to = b.account_id " +
                    "JOIN tenmo_user u ON a.user_id = u.user_id " +
                    "JOIN tenmo_user v ON b.user_id = v.user_id " +
                    "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id " +
                    "JOIN transfer_type tt ON t.transfer_type_id = tt.transfer_type_id " +
                    "WHERE t.transfer_id = ?";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
            return transfer;
        }

        @Override
        public List<Transfer> getAllTransfers ( long userId){
            List<Transfer> transfers = new ArrayList<>();

            String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfer t " +
                    "JOIN account a ON t.account_from = a.account_id " +
                    "JOIN account b ON t.account_to = b.account_id " +
                    "JOIN tenmo_user u ON a.user_id = u.user_id " +
                    "JOIN tenmo_user v ON b.user_id = v.user_id " +
                    "WHERE a.user_id = ? OR b.user_id = ? ";

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
            return transfers;
        }


//    @Override
//    public String transferBucksToUser(long accountTo, long accountFrom, BigDecimal amount) {
//        return null;
//    }
//
//    public void addToBalance(long user_id,BigDecimal amount){
//
//        String sql = "UPDATE account " +
//                "SET balance = balance + ? " +
//                "WHERE user_id = ?;";
//        jdbcTemplate.update(sql, amount, user_id);
//
//    }


        public void subtractFromBalance ( int user_id, BigDecimal amount){

            String sql = "UPDATE account " +
                    "SET balance = balance - ? " +
                    "WHERE user_id = ?;";
            jdbcTemplate.update(sql, amount, user_id);

        }


        public void deleteTransfer ( int transfer_id){
            String sql = "DELETE FROM transfer WHERE transfer_id = ?";
            jdbcTemplate.update(sql, transfer_id);
        }

        private Transfer mapRowToTransfer (SqlRowSet results){
            Transfer transfer = new Transfer();
            transfer.setTransferId(results.getInt("transfer_id"));
            transfer.setTransferTypeId(results.getInt("transfer_type_id"));
            transfer.setTransferStatusId(results.getInt("transfer_status_id"));
            transfer.setAccountFrom(results.getInt("account_from"));
            transfer.setAccountTo(results.getInt("account_to"));
            transfer.setAmount(results.getBigDecimal("amount"));
            try {
                transfer.setUsernameFrom(results.getString("userfrom"));
                transfer.setUsernameTo(results.getString("userto"));
            } catch (Exception e) {
            }
            try {
                transfer.setTransferType(results.getString("transfer_type_desc"));
                transfer.setTransferStatus(results.getString("transfer_status_desc"));
            } catch (Exception e) {
            }


            return transfer;
        }

        private Account mapRowToAccount (SqlRowSet rowSet){
            Account account = new Account();

            account.setAccountId(rowSet.getInt("account_id"));
            account.setUserId(rowSet.getInt("user_id"));
            account.setBalance(rowSet.getBigDecimal("balance"));

            return account;
        }


        @Override
        public List<Transfer> getPendingTransfers ( long userId){
            List<Transfer> transfers = new ArrayList<>();

            String sql = "SELECT transfer_id, transfer_type_id, transfer.transfer_status_id, account_from, account_to, amount \n" +
                    "FROM transfer \n" +
                    "JOIN account ON account.account_id = transfer.account_from \n" +
                    "JOIN transfer_status ON transfer.transfer_status_id = transfer_status.transfer_status_id \n" +
                    "WHERE user_id = ? AND transfer_status_desc = 'Pending'";

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);


            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
            return transfers;
        }


        private User mapRowToUser (SqlRowSet results){
            return null;
        }

        @Override
        public void updateTransfer (Transfer transfer){

        }

        @Override
        public Transfer postSentBucks () {
            return null;
        }

        @Override
        public Transfer transferBucksToUser () {
            return null;
        }

}