package com.css.model.car;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
public class Result implements java.io.Serializable{

	@XmlElement
	public String CLLX;    //车辆类型

	@XmlElement
	public String XM;      //姓名

	@XmlElement
	public String ZZL;     //总质量

	@XmlElement
	public String CLXH;    //车辆型号

	@XmlElement
	public String CCDJRQ;  //初次登记日期

	@XmlElement
	public String YXQZ;    //检验有效期止

	@XmlElement
	public String HPHM;    //号牌号码

	@XmlElement
	public String ZSXXDZ;  //住所详细地址
	
	public Result() {
		super();
	}

	public void setCLLX(String CLLX) {
		this.CLLX = CLLX;
	}

	public void setXM(String XM) {
		this.XM = XM;
	}

	public void setZZL(String ZZL) {
		this.ZZL = ZZL;
	}

	public void setCLXH(String CLXH) {
		this.CLXH = CLXH;
	}

	public void setCCDJRQ(String CCDJRQ) {
		this.CCDJRQ = CCDJRQ;
	}

	public void setYXQZ(String YXQZ) {
		this.YXQZ = YXQZ;
	}

	public void setHPHM(String HPHM) {
		this.HPHM = HPHM;
	}

	public void setZSXXDZ(String ZSXXDZ) {
		this.ZSXXDZ = ZSXXDZ;
	}

	public String getCLLX() {
		return CLLX;
	}

	public String getXM() {
		return XM;
	}

	public String getZZL() {
		return ZZL;
	}

	public String getCLXH() {
		return CLXH;
	}

	public String getCCDJRQ() {
		return CCDJRQ;
	}

	public String getYXQZ() {
		return YXQZ;
	}

	public String getHPHM() {
		return HPHM;
	}

	public String getZSXXDZ() {
		return ZSXXDZ;
	}
}
