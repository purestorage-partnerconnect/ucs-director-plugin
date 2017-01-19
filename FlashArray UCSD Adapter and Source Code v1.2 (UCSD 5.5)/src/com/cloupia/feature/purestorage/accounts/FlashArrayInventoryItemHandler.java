package com.cloupia.feature.purestorage.accounts;

import org.apache.log4j.Logger;

import com.cloupia.lib.connector.AbstractInventoryItemHandler;
import com.cloupia.lib.connector.InventoryContext;

public class FlashArrayInventoryItemHandler extends AbstractInventoryItemHandler
{
    private static Logger logger = Logger.getLogger(FlashArrayInventoryItemHandler.class);
    
    @Override
    public void cleanup(String arg0) throws Exception
    {
        logger.info("FlashArrayInventoryItemHandler.cleanup: " + arg0);
    }

    @Override
    public void doInventory(String arg0, InventoryContext arg1) throws Exception
    {
        logger.info("FlashArrayInventoryItemHandler.doInventory: " + arg0);
    }

    @Override
    public void doInventory(String arg0, Object arg1) throws Exception
    {
        logger.info("FlashArrayInventoryItemHandler.doInventory: " + arg0);
    }
}