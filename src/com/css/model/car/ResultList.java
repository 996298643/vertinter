package com.css.model.car;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ResultList {

    public List<Result> resultList;

    @XmlElements(value={@XmlElement(name = "result", type = com.css.model.car.Result.class)})
    public List<Result> getResultList() {
        return resultList;
    }
}
