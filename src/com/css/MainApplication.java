package com.css;

import com.css.annotation.Path;
import com.css.utils.ClassHelper;
import com.css.utils.PropertiesUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.function.Consumer;

/**
 *  入口
 *  @author lzq
 * */
public class MainApplication extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();
        final Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        //数据库配置
        JsonObject sqlconfig = new JsonObject();
        //本地
        sqlconfig.put("url", PropertiesUtils.getAppConf().get("db.url"));
        sqlconfig.put("driver_class", PropertiesUtils.getAppConf().get("db.driver"));
        sqlconfig.put("user", PropertiesUtils.getAppConf().get("db.user"));
        sqlconfig.put("password", PropertiesUtils.getAppConf().get("db.password"));
        sqlconfig.put("max_pool_size", Integer.parseInt(PropertiesUtils.getAppConf().get("max_pool_size")));

        SQLClient sqlClient = JDBCClient.createShared(vertx, sqlconfig);

        Set<Class<?>> classes = ClassHelper.getClzFromPkg("com.css");
        for(Class clazz : classes){
            if(clazz.isAnnotationPresent(Path.class)){
                Path path = (Path)clazz.getAnnotation(Path.class);
                Class<? extends Handler> pathHandler = path.handler();
                Constructor<? extends Handler> pathcla = pathHandler.getDeclaredConstructor(Vertx.class, SQLClient.class);
                if("get".equals(path.type())){
                    router.get(path.value()).handler(pathcla.newInstance(vertx, sqlClient));
                }else if("post".equals(path.type())){
                    router.post(path.value()).handler(pathcla.newInstance(vertx, sqlClient));
                }
            }
        }

        //创建服务端监听
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                router.accept(httpServerRequest);
            }
        }).listen(Integer.parseInt(PropertiesUtils.getAppConf().get("port")));
    }

    public static void main(String[] args) throws Exception {
        String verticleId  = MainApplication.class.getName();
        VertxOptions options =new VertxOptions();
        Consumer<Vertx> runner = vertx1 -> {
            vertx1.deployVerticle(verticleId);
        };
        Vertx vertx = Vertx.vertx(options);
        runner.accept(vertx);
    }

    /*
    public static List<Class<?>> getClassName(String packageName) {
        String filePath = ClassLoader.getSystemResource("").getPath();
        List<Class<?>> fileNames = getClassName(filePath, null);
        return fileNames;
    }

    private static List<Class<?>> getClassName(String filePath, List<String> className) {
        String fileDir = MainApplication.class.getClassLoader().getResource("").getPath();
        List<Class<?>> classNames = new ArrayList<Class<?>>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                classNames.addAll(getClassName(childFile.getPath(), className));
            } else {
                if(childFile.getPath().toString().endsWith(".class")) {
                    String p = childFile.getPath().replace(".class","");
                    File dir = new File(fileDir);
                    p = p.substring((dir.getPath() + File.separator).length(), childFile.getPath().lastIndexOf(".class"));
                    p = p.replaceAll("\\\\", ".").replaceAll("/", ".");
                    try {
                        Class<? extends Object> clazz = Class.forName(p);
                        if(clazz.getSuperclass() == BaseHandler.class) {
                            classNames.add(forName(p));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return classNames;
    } */
}
