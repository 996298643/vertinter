package com.css.model.car;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Body {

    @XmlElement
    public String message;

    @XmlElement
    public String resultCode;

    public List<Result> resultList;

    @XmlTransient
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlTransient
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    //@XmlElementWrapper(name="resultList")
    //@XmlAnyElement(lax=true)
    //@XmlElements(value= {@XmlElement(name="result", type=com.css.model.car.Result.class)})
    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }
}
