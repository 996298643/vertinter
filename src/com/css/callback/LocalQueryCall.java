package com.css.callback;

import com.css.police.RequestServiceServiceLocator;
import com.css.police.RequestServiceSoapBindingStub;
import com.css.utils.Md5Utils;

import java.util.concurrent.Callable;

public class LocalQueryCall implements Callable<String> {

    private String username = "wbdw_jyj";

    private String password = "wbdw_jyj";

    private String serviceCode = "500100000000_01_0000000054-2141";

    private String condition;

    private String requiredItems;

    private String clientStrXml;

    public LocalQueryCall(String condition, String requiredItems, String clientStrXml) {
        this.condition = condition;
        this.requiredItems = requiredItems;
        this.clientStrXml = clientStrXml;
    }

    @Override
    public String call() throws Exception {
        RequestServiceServiceLocator locator = new RequestServiceServiceLocator();
        RequestServiceSoapBindingStub stub = (RequestServiceSoapBindingStub) locator.getRequestService();
        String xml = stub.requestQuery("wbdw_jyj", Md5Utils.MD5("wbdw_jyj"), "500100000000_01_0000000054-2141", this.condition, this.requiredItems, this.clientStrXml);
        return xml;
    }
}
