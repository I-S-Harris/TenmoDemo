package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    @Autowired
    private TransferDao transferDao;


    @RequestMapping(path = "account/transfers/{id}" , method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable long id) {
        List<Transfer> listOfAllTransfers = transferDao.getAllTransfers(id);

        return listOfAllTransfers;
    }
    @RequestMapping(path = "transfer/{id}" , method = RequestMethod.GET)
    public Transfer getOnetransfer(@PathVariable long id){
        Transfer oneTransfer = transferDao.getTransferByTransferId(id);
        return  oneTransfer;
    }
    @RequestMapping(path = "transfer", method = RequestMethod.POST)
    public String sendTransferRequest(@RequestBody Transfer transfer) {
        String results = transferDao.sendTransfer( transfer.getAccountFrom(),  transfer.getAccountTo(), transfer.getAmount());
        return results;
    }
    @RequestMapping(path="/transfers/user/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfersByUserId(@PathVariable int userId) {
        return transferDao.getPendingTransfers(userId);
    }

}