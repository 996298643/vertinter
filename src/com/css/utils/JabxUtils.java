package com.css.utils;


import com.css.model.car.Wrapper;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JabxUtils {

    /**
     * @param
     * @return 返回xmlToList
     */

    private static <T> List<T> unmarshal(javax.xml.bind.Unmarshaller unmarshaller,

                                         Class<T> clazz, String xml) throws JAXBException {
        Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(new StringReader(xml));
        return wrapper.getResultList();

    }


    public static  <T> List parseXmlToList(Class<T> topLevelClass, String xmlLocation) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Wrapper.class,topLevelClass);
        // Unmarshal
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        List<T> list = unmarshal(unmarshaller, topLevelClass, xmlLocation);
        return list;
    }


    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(String xmlStr, Class<T> c) {
        try {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            T t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
            return t;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param object 对象
     * @return 返回xmlStr
     */
    public static String object2Xml(Object object) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshal = context.createMarshaller();
            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化输出
            marshal.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// 编码格式,默认为utf-8
            marshal.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xml头信息
            marshal.setProperty("jaxb.encoding", "utf-8");
            marshal.marshal(object, writer);
            return new String(writer.getBuffer());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
