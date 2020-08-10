package com.tomaszwasilonek.vaults.ws.ui.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tomaszwasilonek.vaults.ws.service.PaymentService;
import com.tomaszwasilonek.vaults.ws.shared.dto.PaymentDTO;
import com.tomaszwasilonek.vaults.ws.ui.model.request.DepositRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.MoneyTransferRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.request.WithdrawalRequestModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.OperationStatusModel;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationName;
import com.tomaszwasilonek.vaults.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/transactions")
public class PaymentController {
	
	@Autowired
	PaymentService paymentService;

	// TODO: test if Vaults belong to User
	@PostMapping("/money-transfer")
	public OperationStatusModel makeMoneyTransfer(@Valid @RequestBody MoneyTransferRequestModel moneyTransferBody) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.MONEY_TRANSFER.name());
		
		PaymentDTO moneyTransfer = new PaymentDTO();
		BeanUtils.copyProperties(moneyTransferBody, moneyTransfer);
		
		paymentService.moneyTransfer(moneyTransfer);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@PostMapping("/deposit")
	public OperationStatusModel makeDeposit(@Valid @RequestBody DepositRequestModel depositBody) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DEPOSIT.name());
		
		PaymentDTO theDeposit = new PaymentDTO();
		BeanUtils.copyProperties(depositBody, theDeposit);
		
		paymentService.deposit(theDeposit);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@PostMapping("/withdraw")
	public OperationStatusModel makeWithdrawal(@Valid @RequestBody WithdrawalRequestModel withdrawalBody) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.WITHDRAWAL.name());
		
		PaymentDTO theWithdrawal = new PaymentDTO();
		BeanUtils.copyProperties(withdrawalBody, theWithdrawal);
		
		paymentService.withdraw(theWithdrawal);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
}
