package com.css.police;

/**
 * @author: 廖振钦
 * @date: 2018年10月8日 上午9:58:12
 * @description:
 */
public interface RequestService_PortType extends java.rmi.Remote {
    public String requestQuery(String username, String password, String serviceCode, String condition, String requiredItems, String clientInfo) throws java.rmi.RemoteException;
    public String czrkQuery(String username, String password, String serviceCode, String condition, String requiredItems, String clientInfo) throws java.rmi.RemoteException;
    public String yhksQuery(String username, String password, String serviceCode, String condition, String clientInfo) throws java.rmi.RemoteException;
    public String requestExamine(String username, String password, String serviceCode, String condition, String clientInfo) throws java.rmi.RemoteException;
    public String compareQuery(String username, String password, String serviceCode, String condition, String requiredItems, String clientInfo) throws java.rmi.RemoteException;
    public String compareExamine(String username, String password, String serviceCode, String condition, String clientInfo) throws java.rmi.RemoteException;
    public String fileCompareQuery(String username, String password, String serviceCode, String condition, String requiredItems, String clientInfo) throws java.rmi.RemoteException;
    public String fileCompareExamine(String username, String password, String serviceCode, String condition, String clientInfo) throws java.rmi.RemoteException;
}
