package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private long transferId;
    private int transferTypeId;
    private int transferStatusId;
    private long accountFrom;
    private long accountTo;
    private BigDecimal amount;
    private long user_id_From;
    private long user_id_To;
    private String transferType;
    private String transferStatus;
    private String usernameFrom;
    private String usernameTo;

    public Transfer() {}

    public Transfer(long transferId, int transferTypeId, int transferStatusId, long accountFrom, long accountTo, BigDecimal amount, long user_id_From, long user_id_To, String transferType, String transferStatus, String usernameFrom, String usernameTo) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.user_id_From = user_id_From;
        this.user_id_To = user_id_To;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
    }

    public long getTransferId() {
        return transferId;
    }
    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }
    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }
    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public long getAccountFrom() {
        return accountFrom;
    }
    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() {
        return accountTo;
    }
    public void setAccountTo(long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getUser_id_From() {
        return user_id_From;
    }
    public void setUser_id_From(long user_id_From) {
        this.user_id_From = user_id_From;
    }

    public long getUser_id_To() {
        return user_id_To;
    }
    public void setUser_id_To(long user_id_To) {
        this.user_id_To = user_id_To;
    }

    public String getTransferType() {
        return transferType;
    }
    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }
    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }
    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }
    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }
}
