/* Copyright � 2015 Oracle and/or its affiliates. All rights reserved. */
package huckster.cabinet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import com.google.gson.Gson;
import huckster.cabinet.repository.DbDao;
import huckster.cabinet.repository.UserData;
import org.apache.catalina.startup.Tomcat;
import org.apache.jorphan.collections.HashTree;

public class Main {
    public static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
    public static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));
    
    public static void main(String[] args) throws Exception {
        UserData user = new UserData("1admin");


/*        String contextPath = "/" ;
        String appBase = ".";
        Tomcat tomcat = new Tomcat();   
        tomcat.setPort(Integer.valueOf(PORT.orElse("8080") ));
        tomcat.setHostname(HOSTNAME.orElse("localhost"));
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);
        tomcat.start();
        tomcat.getServer().await();*/
    }
}