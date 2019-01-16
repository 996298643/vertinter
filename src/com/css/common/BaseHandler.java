package com.css.common;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.List;

public abstract class BaseHandler implements Handler<RoutingContext> {

    protected Vertx vertx;

    protected SQLClient sqlClient;

    public BaseHandler(Vertx vertx, SQLClient sqlClient) {
        this.vertx = vertx;
        this.sqlClient = sqlClient;
    }

    @Override
    public abstract void handle(RoutingContext event);


    /**
     * 更新
     * */
    public void updateParam(String update, JsonArray params){
        sqlClient.getConnection(res -> {
            if (res.succeeded()) { //连接成功
                SQLConnection connection = res.result();
                connection.updateWithParams(update, params ,handle->{
                    if(handle.succeeded()){

                    }
                });
            } else { //连接失败

            }
        });
    }

    /**
     * 插入
     * */
    public void insertBatch(String insertSql, List<JsonArray> paramsList){
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
    }

}
