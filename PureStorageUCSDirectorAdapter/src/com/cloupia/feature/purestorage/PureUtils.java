package com.cloupia.feature.purestorage;


import org.apache.log4j.Logger;

import com.cloupia.feature.purestorage.constants.PureConstants;
import com.cloupia.feature.purestorage.accounts.FlashArrayAccount;
import com.purestorage.rest.PureApiVersion;
import com.purestorage.rest.PureRestClient;
import com.purestorage.rest.PureRestClientConfig;
import com.purestorage.rest.PureRestClientImpl;


public class PureUtils 
{
    static Logger logger = Logger.getLogger(PureUtils.class);

    public static PureRestClient ConstructPureRestClient(FlashArrayAccount acc)
    {
        return ConstructPureRestClient(acc, "1.4");
    }
    public static PureRestClient ConstructPureRestClient(FlashArrayAccount acc, String apiVersion)
    {
        String apiEndPoint = "https://" + acc.getManagementAddress() + "/api";
        PureApiVersion restAPIversion = PureApiVersion.fromString(apiVersion);
        logger.info(acc.getAccount().getAccountName() + ":Connecting to Pure FlashArray at [" + apiEndPoint + "] with username ["
                + acc.getUsername() + "]");
        PureRestClientConfig CONFIG = PureRestClientConfig.newBuilder(acc.getUsername(), acc.getPassword(), apiEndPoint, restAPIversion)//10.203.128.141
                .setClientInfo(PureConstants.PURE_ADAPTER_NAME, PureConstants.PURE_ADAPTER_VERSION)
                .setIgnoreCertificateError(true)
               .build();

        PureRestClient CLIENT = PureRestClientImpl.create(CONFIG);
        CLIENT.array().getAttributes();
        logger.info(acc.getAccount().getAccountName() + ":Successfully connected to Pure FlashArray at " + apiEndPoint);
        return CLIENT;
    }
}
