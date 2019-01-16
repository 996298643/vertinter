package com.css.handlers.police;

import com.css.annotation.Path;
import com.css.callback.CarQueryCall;
import com.css.common.BaseHandler;
import com.css.model.car.ClientInfo;
import com.css.model.car.Response;
import com.css.model.car.Result;
import com.css.utils.JDBCUtils;
import com.css.utils.JabxUtils;
import com.css.utils.JsonUtils;
import com.css.utils.ResultJson;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 车辆接口
 * @author lzq
 * */
@Path(value="/police/car", type = "get", handler= CarServiceHandler.class)
public class CarServiceHandler extends BaseHandler {

    ExecutorService executor = Executors.newFixedThreadPool(30);

    public CarServiceHandler(Vertx vertx, SQLClient sqlClient) {
        super(vertx, sqlClient);
    }

    @Override
    public void handle(RoutingContext event) {
        HttpServerResponse response = event.response();
        HttpServerRequest request = event.request();
        response.putHeader("content-type", "application/json;charset=UTF-8");
        String hphm = request.getParam("hphm"); //号牌号码
        String clientInfo = request.getParam("clientInfo");
        String clientStrXml = "";
        ClientInfo client = null;
        if (clientInfo == null || "".equals(clientInfo)) {
            clientStrXml = getClientInfo("wbdw_jyj", "重庆市监狱管理局", "1", "5000", "10.20.208.100");
        } else {
            client = JsonUtils.parse(clientInfo, ClientInfo.class);
            clientStrXml = getClientInfo("wbdw_jyj", client.getUsername(), client.getUserid(), client.getDb(), client.getIp());
        }

        String condition = getCondition(hphm);
        String requiredItems = getRequiredItems();
        ResultJson JSON = new ResultJson();
        try {
            CarQueryCall query = new CarQueryCall(condition, requiredItems, clientStrXml);
            FutureTask<String> task = new FutureTask<String>(query);
            executor.execute(task);
            String xml = "";

            try {
                xml = task.get(15000, TimeUnit.MILLISECONDS); //设置15秒
            } catch (TimeoutException e) {
                JSON.setStatus(false).setMessage("查询超时,可能车牌号码错误！");
                response.end(JsonUtils.toJson(JSON));
                return;
            }

            if (clientInfo != null) {
                insertLogs(client, hphm, xml); //插入查询日志
            }
            Response responseModel = JabxUtils.xml2Object(xml, Response.class);
            String resultCode = responseModel.getBody().getResultCode();
            if ("00".equals(resultCode)) {
                List<Result> resultList = responseModel.getBody().getResultList();
                if (resultList != null && resultList.size() > 0) {
                    List<Result> resultlists = getResultList(xml);
                    JSON.setStatus(true).setMessage("查询成功").setData(JsonUtils.toJson(resultlists));
                    //插入到结果表中

                    String selectSql = "select * from ZL_POLICE_CAR where HPHM = ?";
                    JsonArray params = new JsonArray();
                    params.add(hphm);
                    sqlClient.queryWithParams(selectSql, params, handle -> {
                        if (handle.succeeded()) {
                            if (handle.result().getRows().size() <= 0) {
                                String insertSql = "insert into ZL_POLICE_CAR(SN, CLLX, XM, ZZL, CCDJRQ, YXQZ, HPHM, ZSXXDZ, CJSJ, SCBJ, JG)"
                                        + " values(SYS_GUID(), ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";
                                List<JsonArray> paramsList = new ArrayList<JsonArray>();

                                for (Result result : resultlists) {
                                    JsonArray insertParams = new JsonArray();
                                    insertParams.add(result.getCLLX() == null ? "" : result.getCLLX());
                                    insertParams.add(result.getXM() == null ? "" : result.getXM());
                                    insertParams.add(result.getZZL() == null ? "" : result.getZZL());
                                    insertParams.add(result.getCCDJRQ() == null ? "" : result.getCCDJRQ());
                                    insertParams.add(result.getYXQZ() == null ? "" : result.getYXQZ());
                                    insertParams.add(result.getHPHM() == null ? "" : result.getHPHM());
                                    insertParams.add(result.getZSXXDZ() == null ? "" : result.getZSXXDZ());
                                    insertParams.add("0");
                                    insertParams.add("5000");
                                    paramsList.add(insertParams);
                                }

                                insertBatch(insertSql, paramsList);

                            } else {
                                //更新参数
                                int cnt = 0;
                                for (Result result : resultlists) {
                                    JsonObject row = handle.result().getRows().get(cnt);
                                    JsonArray updateParams = new JsonArray();
                                    updateParams.add(result.getCLLX() == null ? "" : result.getCLLX());
                                    updateParams.add(result.getXM() == null ? "" : result.getXM());
                                    updateParams.add(result.getZZL() == null ? "" : result.getZZL());
                                    updateParams.add(result.getCCDJRQ() == null ? "" : result.getCCDJRQ());
                                    updateParams.add(result.getYXQZ() == null ? "" : result.getYXQZ());
                                    updateParams.add(result.getHPHM() == null ? "" : result.getHPHM());
                                    updateParams.add(result.getZSXXDZ() == null ? "" : result.getZSXXDZ());
                                    updateParams.add(hphm);
                                    updateParams.add("0");
                                    updateParams.add(row.getString("CLLX"));
                                    String updateSql = "UPDATE ZL_POLICE_CAR SET CLLX = ?, XM = ?, ZZL = ?, CCDJRQ = ?, YXQZ = ?, HPHM= ?, ZSXXDZ = ?"
                                            + " WHERE HPHM = ? AND SCBJ = ? and CLLX = ?";
                                    updateParam(updateSql, updateParams); //执行更新
                                    cnt++;
                                }

                                //条件参数
                            }
                        }
                    });
                    response.end(JsonUtils.toJson(JSON));
                } else {
                    JSON.setStatus(false).setMessage("没有查询到数据,请核对车牌号码.");
                }
            } else {
                JSON.setStatus(false).setMessage(responseModel.getBody().getMessage());
                response.end(JsonUtils.toJson(JSON));
            }
        } catch (Exception e) {
            JSON.setStatus(false).setMessage("系统异常" + e.getMessage());
            e.printStackTrace();
            response.end(JsonUtils.toJson(JSON));
        }
    }

    public void insertLogs(ClientInfo client, String sfzh, String xml) {
        String insertSql = "insert into ZL_POLICE_LOG(SN, LOGINNAME, NAME, USERID, IP, DB, SFZH, CJSJ, SCBJ, JG, CONTENT, LB) values(sys_guid(), ?, ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?)";
        JsonArray params = new JsonArray();
        params.add(client.getLoginname());
        params.add(client.getUsername());
        params.add(client.getUserid());
        params.add(client.getIp());
        params.add(client.getDb());
        params.add(sfzh);
        params.add("0");
        String jg = client.getDb().substring(0, 4);
        params.add(jg);
        params.add(xml);
        params.add(3);
        List<JsonArray> paramsList = new ArrayList<JsonArray>();
        paramsList.add(params);
        insertBatch(insertSql, paramsList);

        //插入中继区数据库
        String insertSql2 = "insert into ZL_POLICE_LOG(SN, LOGINNAME, NAME, USERID, IP, DB, SFZH, cjsj, scbj, jg, CONTENT, LB)"
                + " values(sys_guid(), '" + client.getLoginname() + "', '" + client.getUsername() + "', '" + client.getUserid() + "', '" + client.getIp() + "', '" + client.getDb() + "', '" + sfzh + "', sysdate, '0', '" + jg + "', '" + xml + "','3')";
        JDBCUtils.execute(insertSql2);
    }

    private String getCondition(String hphm) {
        return "<condition><item><HPHM>" + hphm + "</HPHM></item></condition>";
    }

    public String getRequiredItems() {
        return "<requiredItems><item><CLLX></CLLX>"
                + "<XM></XM>"
                + "<ZZL></ZZL>"
                + "<CLXH></CLXH>"
                + "<CCDJRQ></CCDJRQ>"
                + "<YXQZ></YXQZ>"
                + "<HPHM></HPHM>"
                + "<ZSXXDZ></ZSXXDZ>"
                + "</item></requiredItems>";
    }

    public String getClientInfo(String loginname, String username, String userid, String db, String ip) {
        return "<clientInfo><loginName>" + loginname + "</loginName>" + "<userName>" + username + "</userName>"
                + "<userCardId>" + userid + "</userCardId>" + "<userDept>" + db + "</userDept>" + "<ip>" + ip + "</ip>"
                + "</clientInfo>";
    }

    public List<Result> getResultList(String xml) {
        Pattern p = Pattern.compile("<resultList>(.*)</resultList>");

        Matcher matcher = p.matcher(xml);
        matcher.find();
        System.out.println(matcher.group(1));

        String resultStr = "<resultList>" + matcher.group(1) + "</resultList>";
        List<Result> lists = null;
        try {
            lists = (List<Result>)parseXml2List(resultStr, Result.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return lists;
    }


    public static List<?> parseXml2List(String xml, Class<? extends Result> cls)
            throws Exception {
        List<Result> lists = new ArrayList<Result>();
        Document doc = DocumentHelper.parseText(xml);
        Element et = doc.getRootElement();
        String root = et.getName();
        List<Element> list = doc.selectNodes("//" + root + "/result");
        if (!list.isEmpty() && list.size() > 0) {
            for(int i = 0; i < list.size() ; i++) {
                Element element = list.get(i);
                List elems = element.elements();

                Class<? extends Result> newclz = Result.class;
                Result result = newclz.newInstance();
                for(Object s : elems){
                    DefaultElement e = (DefaultElement)s;
                    Method method = newclz.getMethod("set"+e.getName(), String.class);
                    method.invoke(result,e.getText());
                }
                lists.add(result);
            }
        }
        return lists;
    }
}
