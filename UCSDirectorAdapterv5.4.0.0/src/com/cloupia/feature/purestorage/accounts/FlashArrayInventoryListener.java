package com.cloupia.feature.purestorage.accounts;

import com.cloupia.lib.connector.InventoryContext;
import com.cloupia.lib.connector.InventoryEventListener;

public class FlashArrayInventoryListener implements InventoryEventListener {
    @Override
    public void afterInventoryDone(String accountName, InventoryContext context)
    {
    }

    @Override
    public void beforeInventoryStart(String accountName, InventoryContext context)
    {
    }
}
