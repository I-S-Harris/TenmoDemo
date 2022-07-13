package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

   String sendTransfer(int userFrom , int userTo , BigDecimal amount);

    List<Transfer> getTransfersByUserId(long userId);

    Transfer getTransferByTransferId(long transferId);

    List<Transfer> getAllTransfers(long userId);

    List<Transfer> getPendingTransfers(long userId);

    void updateTransfer(Transfer transfer);

    Transfer postSentBucks();

    Transfer transferBucksToUser();

    BigDecimal addToBalance(BigDecimal amountToAdd, int id);

    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);

     Account findAccountById(int id);

     Account findAccountByAccountId(int id);
}
