package com.css.handlers.test;

import com.css.annotation.Path;
import com.css.common.BaseHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

@Path(value="/hello", type = "get", handler= HelloHandler.class)
public class HelloHandler extends BaseHandler {


    @Override
    public void handle(RoutingContext event) {
        HttpServerResponse response = event.response();
        HttpServerRequest request = event.request();
        String bh = request.getParam("bh");
        response.putHeader("content-type","text/html;charset=UTF-8");

        if(bh == null || bh == "") {
            response.end("请输入编号");
        }else {
            StringBuffer buffer = new StringBuffer();
            StringBuffer sqlconf = new StringBuffer();
            sqlconf.append("select bh,xm,jg.mc as mc from gy_da_jbxx jbxx left join gy_xt_jg jg on jg.scbj=? and jg.bm = jbxx.db where jbxx.scbj=? and jbxx.bh=?");
            JsonArray params = new JsonArray();
            params.add("0");
            params.add("0");
            params.add(bh);
            sqlClient.queryWithParams(sqlconf.toString(), params, res -> {
                if (res.succeeded()) {
                    ResultSet resultSet = res.result();
                    List<JsonArray> results = resultSet.getResults();
                    for (JsonArray row : results) {
                        String code = row.getString(0);
                        String xm = row.getString(1);
                        String db = row.getString(2);
                        buffer.append("编号=" + code + ",姓名=" + xm + ",监区=" + db);
                    }
                    response.end("结果:" + buffer.toString());
                }
            });
        }
    }

    public HelloHandler(Vertx vertx, SQLClient sqlClient) {
        super(vertx, sqlClient);
    }


}
