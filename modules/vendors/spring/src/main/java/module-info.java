module spring {
    requires redis;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires com.fasterxml.jackson.databind;
    requires spring.data.redis;
    requires org.jobrunr.core;
}
