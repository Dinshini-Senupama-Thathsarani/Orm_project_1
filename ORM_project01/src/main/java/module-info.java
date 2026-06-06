module lk.orm.project01 {

    requires javafx.controls;
    requires javafx.fxml;


    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires ehcache;



    requires java.naming;
    requires java.sql;


    requires jbcrypt;


    requires org.slf4j;


    opens lk.orm.project01.tm         to javafx.fxml, javafx.base;
    opens lk.orm.project01.controller to javafx.fxml;
    opens lk.orm.project01.dto        to javafx.base;





    opens lk.orm.project01.entity to org.hibernate.orm.core;


    exports lk.orm.project01.tm;
    exports lk.orm.project01.controller;
    exports lk.orm.project01.entity;
    exports lk.orm.project01.dto;
    exports lk.orm.project01.bo;
    exports lk.orm.project01.dao;
    exports lk.orm.project01.config;
    exports lk.orm.project01.exception;
    exports lk.orm.project01.util;

}
