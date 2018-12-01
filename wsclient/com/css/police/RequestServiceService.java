package com.css.police;

/**
 * @author: 廖振钦
 * @date: 2018年10月8日 上午9:58:55
 * @description:
 */
public interface RequestServiceService extends javax.xml.rpc.Service {
    public String getRequestServiceAddress();

    public com.css.police.RequestService_PortType getRequestService() throws javax.xml.rpc.ServiceException;

    public com.css.police.RequestService_PortType getRequestService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
