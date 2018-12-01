package com.css.police;

/**
 * @author: 廖振钦
 * @date: 2018年10月8日 上午10:30:11
 * @description:
 */
public class RequestServiceServiceLocator extends org.apache.axis.client.Service
		implements RequestServiceService {

	public RequestServiceServiceLocator() {
	}

	public RequestServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public RequestServiceServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	private String RequestService_address = "http://gxpt.cq:81/dcws/RequestService";

	public String getRequestServiceAddress() {
		return RequestService_address;
	}

	private String RequestServiceWSDDServiceName = "RequestService";

	public String getRequestServiceWSDDServiceName() {
		return RequestServiceWSDDServiceName;
	}

	public void setRequestServiceWSDDServiceName(String name) {
		RequestServiceWSDDServiceName = name;
	}

	public RequestService_PortType getRequestService() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(RequestService_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getRequestService(endpoint);
	}

	public RequestService_PortType getRequestService(java.net.URL portAddress)
			throws javax.xml.rpc.ServiceException {
		try {
			com.css.police.RequestServiceSoapBindingStub _stub = new com.css.police.RequestServiceSoapBindingStub(
					portAddress, this);
			_stub.setPortName(getRequestServiceWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setRequestServiceEndpointAddress(String address) {
		RequestService_address = address;
	}

	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (RequestService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
				com.css.police.RequestServiceSoapBindingStub _stub = new com.css.police.RequestServiceSoapBindingStub(
						new java.net.URL(RequestService_address), this);
				_stub.setPortName(getRequestServiceWSDDServiceName());
				return _stub;
			}
		} catch (Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		String inputPortName = portName.getLocalPart();
		if ("RequestService".equals(inputPortName)) {
			return getRequestService();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://webservice.web.ws.service.iflytek.com", "RequestServiceService");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://webservice.web.ws.service.iflytek.com", "RequestService"));
		}
		return ports.iterator();
	}

	public void setEndpointAddress(String portName, String address)
			throws javax.xml.rpc.ServiceException {
		if ("RequestService".equals(portName)) {
			setRequestServiceEndpointAddress(address);
		} else {
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	public void setEndpointAddress(javax.xml.namespace.QName portName, String address)
			throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}
}
