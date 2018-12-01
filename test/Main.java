import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;
import java.util.function.Consumer;

public class Main extends AbstractVerticle {


    @Override
    public void start() throws Exception {
        super.start();
        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        //数据库配置
        JsonObject sqlconfig = new JsonObject()
                .put("url", "jdbc:dm://192.168.0.254:5236")
                .put("driver_class", "dm.jdbc.driver.DmDriver")
                .put("user", "CQJY")
                .put("password", "CQJY123_!@#")
                .put("max_pool_size", 50);

        //访问路由
        router.get("/hello").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                event.response().putHeader("content-type","text/html").end("hello world");
            }
        });

        router.get("/hello2").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext event) {
                HttpServerResponse response = event.response();
                HttpServerRequest request = event.request();
                String bh = request.getParam("bh");
                response.putHeader("content-type","text/html;charset=UTF-8");
                SQLClient client = JDBCClient.createShared(vertx, sqlconfig);

                StringBuffer buffer = new StringBuffer();

                StringBuffer sqlconf = new StringBuffer();

                sqlconf.append("select bh,xm,jg.mc as mc from gy_da_jbxx jbxx left join gy_xt_jg jg on jg.scbj=? and jg.bm = jbxx.db where jbxx.scbj=? and jbxx.bh=?");
                JsonArray params = new JsonArray();
                params.add("0");
                params.add("0");
                params.add(bh);
                client.queryWithParams(sqlconf.toString(), params, res->{
                    if(res.succeeded()){
                        ResultSet resultSet = res.result();
                        List<JsonArray> results = resultSet.getResults();
                        for (JsonArray row : results) {
                            String code = row.getString(0);
                            String xm = row.getString(1);
                            String db = row.getString(2);
                            buffer.append("编号="+code+",姓名="+xm+",监区="+db);
                        }
                        response.end("结果:"+buffer.toString());
                    }
                });
            }
        });

        //创建服务端监听
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                router.accept(httpServerRequest);
            }
        }).listen(8080);
    }


    public static void main(String[] args) {
        String verticleId  = Main.class.getName();
        VertxOptions options =new VertxOptions();
        Consumer<Vertx> runner = vertx1 -> {
            vertx1.deployVerticle(verticleId);
        };
        Vertx vertx = Vertx.vertx(options);
        runner.accept(vertx);
    }

}