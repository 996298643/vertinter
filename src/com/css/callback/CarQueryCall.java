package com.css.callback;

import com.css.police.RequestServiceServiceLocator;
import com.css.police.RequestServiceSoapBindingStub;
import com.css.utils.Md5Utils;

import java.util.concurrent.Callable;

public class CarQueryCall implements Callable<String> {

    private String username = "wbdw_sfj";

    private String password = "wbdw_sfj";

    private String serviceCode = "500100000000_01_0000000230-2161";

    private String condition;

    private String requiredItems;

    private String clientStrXml;

    public CarQueryCall(String condition, String requiredItems, String clientStrXml){
        this.condition = condition;
        this.requiredItems = requiredItems;
        this.clientStrXml = clientStrXml;
    }


    @Override
    public String call() throws Exception {
        RequestServiceServiceLocator locator = new RequestServiceServiceLocator();
        RequestServiceSoapBindingStub stub = (RequestServiceSoapBindingStub) locator.getRequestService();
        String xml = stub.requestQuery("wbdw_sfj", Md5Utils.MD5("wbdw_sfj"), "500100000000_01_0000000230-2161", this.condition, this.requiredItems, this.clientStrXml);
        return xml;
    }
}
