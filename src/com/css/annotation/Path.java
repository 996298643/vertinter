package com.css.annotation;

import io.vertx.core.Handler;
import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.TYPE)
public @interface Path {

    String value();

    String type();

    Class<? extends Handler> handler();
}
