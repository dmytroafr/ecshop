// package com.echem.ecshop.config;

// import org.apache.catalina.connector.Connector;
// import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
// import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class HttpConfiguration {
//     @Bean
//     public ServletWebServerFactory servletContainer() {
//         TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//         tomcat.addAdditionalTomcatConnectors(httpConnector());
//         return tomcat;
//     }

//     private Connector httpConnector() {
//         Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//         connector.setScheme("http");
//         connector.setPort(80);
//         connector.setSecure(false);
//         connector.setRedirectPort(443);
//         return connector;
//     }
// }
