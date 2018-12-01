package com.css.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Result{

	@XmlElement
	public String ZJHM;  	  //证件号码

	@XmlElement
	public String XM;        //姓名

	@XmlElement
	public String CYM;       //曾用名

	@XmlElement
	public String XB;        //性别

	@XmlElement
	public String MZ;        //民族

	@XmlElement
	public String CSRQ;      //出生日期

	@XmlElement
	public String HKXZ;      //户口性质

	@XmlElement
	public String HKSZDXZQH; //户口所在地行政区划

	@XmlElement
	public String HKSZDXZ;   //户口所在地详址

	@XmlElement
	public String FZRQ;      //发证日期

	@XmlElement
	public String YXQX;      //有效期限

	@XmlElement
	public String ZP;        //照片
	
	public Result() {
		super();
	}

	public String getZJHM() {
		return ZJHM;
	}

	public void setZJHM(String ZJHM) {
		this.ZJHM = ZJHM;
	}

	public String getXM() {
		return XM;
	}

	public void setXM(String XM) {
		this.XM = XM;
	}

	public String getCYM() {
		return CYM;
	}

	public void setCYM(String CYM) {
		this.CYM = CYM;
	}

	public String getXB() {
		return XB;
	}

	public void setXB(String XB) {
		this.XB = XB;
	}

	public String getMZ() {
		return MZ;
	}

	public void setMZ(String MZ) {
		this.MZ = MZ;
	}

	public String getCSRQ() {
		return CSRQ;
	}

	public void setCSRQ(String CSRQ) {
		this.CSRQ = CSRQ;
	}

	public String getHKXZ() {
		return HKXZ;
	}

	public void setHKXZ(String HKXZ) {
		this.HKXZ = HKXZ;
	}

	public String getHKSZDXZQH() {
		return HKSZDXZQH;
	}

	public void setHKSZDXZQH(String HKSZDXZQH) {
		this.HKSZDXZQH = HKSZDXZQH;
	}

	public String getHKSZDXZ() {
		return HKSZDXZ;
	}

	public void setHKSZDXZ(String HKSZDXZ) {
		this.HKSZDXZ = HKSZDXZ;
	}

	public String getFZRQ() {
		return FZRQ;
	}

	public void setFZRQ(String FZRQ) {
		this.FZRQ = FZRQ;
	}

	public String getYXQX() {
		return YXQX;
	}

	public void setYXQX(String YXQX) {
		this.YXQX = YXQX;
	}

	public String getZP() {
		return ZP;
	}

	public void setZP(String ZP) {
		this.ZP = ZP;
	}
}
