package org.cspoker.server.common;

import org.cspoker.common.api.cashier.context.CashierContext;
import org.cspoker.server.common.account.ExtendedAccountContext;

public class CashierContextImpl implements CashierContext {

	public CashierContextImpl(ExtendedAccountContext accountContext) {
	}

	public int getMoneyAmount() {
		return 0;
	}

	public void requestMoney() {
	}

}
