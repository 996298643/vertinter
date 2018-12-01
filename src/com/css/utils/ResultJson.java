package com.css.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 自定义操作结果对象
 * 
 * @author wangkai
 * @date Feb 7, 2013 1:37:15 AM
 * 
 */
public class ResultJson<T> implements Serializable {
	private static final long serialVersionUID = 4515142766439647139L;

	/** 处理结果 */
	private boolean status = false;
	/** 是否重定向 */
	private boolean redirect = false;
	/** 提示信息 */
	private String message;
	/** 重定向地址 */
	private String url;
	/** 数据集 */
	private List<T> datas;
	/** 单个数据对象 */
	private T data;

	public ResultJson() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public boolean isStatus() {
		return status;
	}

	public ResultJson<T> setStatus(boolean status) {
		this.status = status;
		return this;
	}

	public boolean isRedirect() {
		return redirect;
	}

	public ResultJson<T> setRedirect(boolean redirect) {
		this.redirect = redirect;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResultJson<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public ResultJson<T> setUrl(String url) {
		this.url = url;
		return this;
	}

	public List<T> getDatas() {
		if (datas == null) {
			datas = Collections.emptyList();
		}
		return datas;
	}

	public ResultJson<T> setDatas(List<T> datas) {
		this.datas = datas;
		return this;
	}

	public T getData() {
		return data;
	}

	public ResultJson<T> setData(T data) {
		this.data = data;
		return this;
	}

	/**
	 * 错误结果
	 * 
	 * @param message
	 * @return
	 * @author wangkai
	 * @date Feb 12, 2013
	 * 
	 */
	public ResultJson<T> error(String message) {
		return setStatus(false).setMessage(message);
	}

	/**
	 * 成功结果
	 * 
	 * @param message
	 * @return
	 * @author wangkai
	 * @date Feb 12, 2013
	 * 
	 */
	public ResultJson<T> success(String message) {
		return setStatus(true).setMessage(message);
	}

	@Override
	public String toString() {
		return "ResultJson{" +
				"status=" + status +
				", redirect=" + redirect +
				", message='" + message + '\'' +
				", url='" + url + '\'' +
				", datas=" + datas +
				", data=" + data +
				'}';
	}
}