package com.css.handlers.test;

import com.css.annotation.Path;
import com.css.common.BaseHandler;
import com.css.utils.JDBCUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

@Path(value="/insert", type = "get", handler= InsertHandler.class)
public class InsertHandler extends BaseHandler {

    public InsertHandler(Vertx vertx, SQLClient sqlClient) {
        super(vertx, sqlClient);
    }

    @Override
    public void handle(RoutingContext event) {
        String insertSql = "insert into ZL_POLICE_USER(SN, ZJHM, XM, CYM, XB, MZ, CSRQ, HKXZ, HKSZDXZQH, HKSZDXZ, FZRQ, YXRQ, ZP, CJSJ, SCBJ, LB, JG)"
                +"values(SYS_GUID(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?)";
        JsonArray insertParams = new JsonArray();
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        insertParams.add("２");
        List<JsonArray> paramsList = new ArrayList<JsonArray>();
        paramsList.add(insertParams);
        sqlClient.getConnection(res -> {
            if (res.succeeded()) { //连接成功
                System.out.println("连接成功1");
                SQLConnection connection = res.result();
                connection.batchWithParams(insertSql, paramsList, res1->{
                    if(res1.succeeded()){
                        System.out.println("client插入成功");
                    }else{
                        System.out.println("client插入失败:"+res1.cause());
                    }
                });
            } else { //连接失败

            }
        });
        String insertSql2 = "insert into ZL_POLICE_USER(SN, ZJHM, XM, CYM, XB, MZ, CSRQ, HKXZ, HKSZDXZQH, HKSZDXZ, FZRQ, YXRQ, ZP, CJSJ, SCBJ, LB, JG)"
                +"values(SYS_GUID(), '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', '"+2+"', SYSDATE, '"+2+"', '"+2+"', '"+2+"')";
        JDBCUtils.execute(insertSql2);
        event.response().putHeader("content-type","text/html;charset=UTF-8").end("插入成功");
    }
}
