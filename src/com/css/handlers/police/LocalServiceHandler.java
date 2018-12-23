package com.css.handlers.police;

import com.css.annotation.Path;
import com.css.callback.LocalQueryCall;
import com.css.callback.NationalQueryCall;
import com.css.common.BaseHandler;
import com.css.model.ClientInfo;
import com.css.model.Response;
import com.css.model.Result;
import com.css.utils.*;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 重庆本地接口
 * @author lzq
 * */
@Path(value="/police/local", type = "get", handler= LocalServiceHandler.class)
public class LocalServiceHandler extends BaseHandler {

    ExecutorService executor = Executors.newFixedThreadPool(30);

    public LocalServiceHandler(Vertx vertx, SQLClient sqlClient) {
        super(vertx, sqlClient);
    }

    @Override
    public void handle(RoutingContext event) {
        HttpServerResponse response = event.response();
        HttpServerRequest request = event.request();
        response.putHeader("content-type","application/json;charset=UTF-8");
        String sfzh = request.getParam("sfzh");
        String clientInfo = request.getParam("clientInfo");
        String clientStrXml = "";
        ClientInfo client = null;
        if(clientInfo == null || "".equals(clientInfo)){
            clientStrXml = getClientInfo("wbdw_jyj","重庆市监狱管理局","1","5000","10.20.208.100");
        }else {
            client = JsonUtils.parse(clientInfo, ClientInfo.class);
            clientStrXml = getClientInfo("wbdw_jyj", client.getUsername(), client.getUserid(), client.getDb(), client.getIp());
        }

        String condition = getCondition(sfzh);
        String requiredItems = getRequiredItems();
        ResultJson JSON = new ResultJson();
        try {
            LocalQueryCall query = new LocalQueryCall(condition, requiredItems, clientStrXml);
            FutureTask<String> task = new FutureTask<String>(query);
            executor.execute(task);
            String xml = "";

            try{
                xml = task.get(15000, TimeUnit.MILLISECONDS);
            }catch (TimeoutException e){
                JSON.setStatus(false).setMessage("查询超时,可能是身份证号代码错误！");
                response.end(JsonUtils.toJson(JSON));
                return;
            }
            if(clientInfo != null) {
                insertLogs(client, sfzh, xml); //插入查询日志
            }
            Response responseModel = JabxUtils.xml2Object(xml, Response.class);
            String resultCode = responseModel.getBody().getResultCode();
            if("00".equals(resultCode)) {
                Result result = responseModel.getBody().getResultList().getResult();
                if(result != null) {
                    JSON.setStatus(true).setMessage("查询成功").setData(JsonUtils.toJson(result));
                    //插入到结果表中
                    String selectSql = "select * from ZL_POLICE_USER where ZJHM = ? and LB = ?";
                    JsonArray params = new JsonArray();
                    params.add(sfzh);
                    params.add("2");
                    sqlClient.queryWithParams(selectSql, params, handle -> {
                        if (handle.succeeded()) {
                            if (handle.result().getRows().size() <= 0) {
                                String insertSql = "insert into ZL_POLICE_USER(SN, ZJHM, XM, CYM, XB, MZ, CSRQ, HKXZ, HKSZDXZQH, HKSZDXZ, FZRQ, YXRQ, ZP, CJSJ, SCBJ, LB, JG)"
                                        + "values(SYS_GUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?)";
                                JsonArray insertParams = new JsonArray();
                                insertParams.add(result.getZJHM() == null ? "" : result.getZJHM());
                                insertParams.add(result.getXM() == null ? "" : result.getXM());
                                insertParams.add(result.getCYM() == null ? "" : result.getCYM());
                                insertParams.add(result.getXB() == null ? "" : result.getXB());
                                insertParams.add(result.getMZ() == null ? "" : result.getMZ());
                                insertParams.add(result.getCSRQ() == null ? "" : result.getCSRQ());
                                insertParams.add(result.getHKXZ() == null ? "" : result.getHKXZ());
                                insertParams.add(result.getHKSZDXZQH() == null ? "" : result.getHKSZDXZQH());
                                insertParams.add(result.getHKSZDXZ() == null ? "" : result.getHKSZDXZ());
                                insertParams.add(result.getFZRQ() == null ? "" : result.getFZRQ());
                                insertParams.add(result.getYXQX() == null ? "" : result.getYXQX());
                                insertParams.add(result.getZP());
                                insertParams.add("0");
                                insertParams.add("2");
                                insertParams.add("5000");
                                List<JsonArray> paramsList = new ArrayList<JsonArray>();
                                paramsList.add(insertParams);
                                sqlClient.getConnection(res -> {
                                    if (res.succeeded()) { //连接成功
                                        SQLConnection connection = res.result();
                                        connection.batchWithParams(insertSql, paramsList, res1 -> {
                                            if (res1.succeeded()) {

                                            } else {

                                            }
                                        });
                                    } else { //连接失败

                                    }
                                });
                            } else {
                                JsonArray updateParams = new JsonArray();
                                //更新参数
                                updateParams.add(result.getZJHM() == null ? "" : result.getZJHM());
                                updateParams.add(result.getXM() == null ? "" : result.getXM());
                                updateParams.add(result.getCYM() == null ? "" : result.getCYM());
                                updateParams.add(result.getXB() == null ? "" : result.getXB());
                                updateParams.add(result.getMZ() == null ? "" : result.getMZ());
                                updateParams.add(result.getCSRQ() == null ? "" : result.getCSRQ());
                                updateParams.add(result.getHKXZ() == null ? "" : result.getHKXZ());
                                updateParams.add(result.getHKSZDXZQH() == null ? "" : result.getHKSZDXZQH());
                                updateParams.add(result.getHKSZDXZ() == null ? "" : result.getHKSZDXZ());
                                updateParams.add(result.getFZRQ() == null ? "" : result.getFZRQ());
                                updateParams.add(result.getYXQX() == null ? "" : result.getYXQX());
                                updateParams.add(result.getZP());
                                //条件参数
                                updateParams.add(sfzh);
                                updateParams.add("2");
                                String updateSql = "UPDATE ZL_POLICE_USER SET ZJHM = ?, XM = ?, CYM = ?, XB = ?, MZ = ?, CSRQ= ?, HKXZ = ?, HKSZDXZQH = ?, HKSZDXZ = ?, FZRQ = ?, YXQX = ?, ZP = ?"
                                                  +" WHERE ZJHM = ? AND LB = ?";
                            }
                        }
                    });
                    response.end(JsonUtils.toJson(JSON));
                }else{
                    JSON.setStatus(false).setMessage("没有查询到数据,请核对身份证号码.");
                }
            }else {
                JSON.setStatus(false).setMessage(responseModel.getBody().getMessage());
                response.end(JsonUtils.toJson(JSON));
            }
        }catch (Exception e){
            JSON.setStatus(false).setMessage("系统异常"+e.getMessage());
            e.printStackTrace();
            response.end(JsonUtils.toJson(JSON));
        }
    }

    public void insertLogs(ClientInfo client, String sfzh, String xml){
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
        params.add(2);
        List<JsonArray> paramsList = new ArrayList<JsonArray>();
        paramsList.add(params);
        sqlClient.getConnection(res -> {
            if (res.succeeded()) { //连接成功
                SQLConnection connection = res.result();
                connection.batchWithParams(insertSql, paramsList ,handle->{
                    if(handle.succeeded()){

                    }else{

                    }
                });
            } else { //连接失败

            }
        });

        //插入中继区数据库
        String insertSql2 = "insert into ZL_POLICE_LOG(SN, LOGINNAME, NAME, USERID, IP, DB, SFZH, cjsj, scbj, jg, CONTENT, LB)"
                +" values(sys_guid(), '"+client.getLoginname()+"', '"+client.getUsername()+"', '"+client.getUserid()+"', '"+client.getIp()+"', '"+client.getDb()+"', '"+sfzh+"', sysdate, '0', '"+jg+"', '"+xml+"','2')";
        JDBCUtils.execute(insertSql2);
    }

    public String getCondition(String sfzh) {
        return "<condition><item><ZJHM>"+sfzh+"</ZJHM></item></condition>";
    }

    public String getRequiredItems() {
        return "<requiredItems><item><ZJHM></ZJHM>"
                +"<XM></XM>"
                +"<CYM></CYM>"
                +"<XB></XB>"
                +"<MZ></MZ>"
                +"<HKSZDXZQH></HKSZDXZQH>"
                +"<HKSZDXZ></HKSZDXZ>"
                +"<FZRQ></FZRQ>"
                +"<YXQX></YXQX>"
                +"<ZP></ZP>"
                +"</item></requiredItems>";
    }

    public String getClientInfo(String loginname, String username, String userid, String db, String ip) {
        return "<clientInfo><loginName>" + loginname + "</loginName>" + "<userName>" + username + "</userName>"
                + "<userCardId>" + userid + "</userCardId>" + "<userDept>" + db + "</userDept>" + "<ip>" + ip + "</ip>"
                + "</clientInfo>";
    }
}
