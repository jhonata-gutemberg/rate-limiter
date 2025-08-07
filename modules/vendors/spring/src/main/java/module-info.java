module spring {
    requires redis;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires com.fasterxml.jackson.databind;
    requires spring.data.redis;
    requires org.jobrunr.core;
    requires software.amazon.awssdk.services.s3;
    requires software.amazon.awssdk.auth;
    requires spring.beans;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.core;
}
