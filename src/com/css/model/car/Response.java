package com.css.model.car;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlRootElement(name="response")
public class Response implements Serializable {

    @XmlElement
    public Head head;

    @XmlElement
    public Body body;

    @XmlTransient
    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    @XmlTransient
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
