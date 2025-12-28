open module ${package}.infrastructure {
    requires annotations;
    requires com.fasterxml.jackson.annotation;
    requires com.h2database;
    requires com.querydsl.core;
    requires com.querydsl.jpa;
    requires io.inugami.framework.api;
    requires io.inugami.framework.commons.spring.data;
    requires io.inugami.framework.commons.spring;
    requires io.inugami.framework.commons;
    requires io.inugami.framework.configurations.configuration;
    requires io.inugami.framework.interfaces;
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires jdk.compiler;
    requires lombok;
    requires org.hibernate.orm.core;
    requires org.jspecify;
    requires org.mapstruct;
    requires org.slf4j;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot.starter.data.jpa;
    requires spring.boot;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.tx;
    //
    requires ${package}.api;
    //
    exports ${package}.infrastructure.database.dao;
    exports ${package}.infrastructure.database.entity;
    exports ${package}.infrastructure.database.listener;
    exports ${package}.infrastructure.database.mapper;
    exports ${package}.infrastructure.database.repository;
}
