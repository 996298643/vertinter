package com.css.common;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;

public abstract class BaseHandler implements Handler<RoutingContext> {

    protected Vertx vertx;

    protected SQLClient sqlClient;

    public BaseHandler(Vertx vertx, SQLClient sqlClient) {
        this.vertx = vertx;
        this.sqlClient = sqlClient;
    }

    @Override
    public abstract void handle(RoutingContext event);
}
