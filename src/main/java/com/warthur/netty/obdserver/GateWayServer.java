package com.warthur.netty.obdserver;

import com.warthur.netty.obdserver.common.util.FileUtils;
import com.warthur.netty.obdserver.server.ObdServer;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

@Slf4j
public class GateWayServer {

    private static String connection_protocol;
    private static String netty_server_ip;
    private static int netty_server_port;
    private static final String CONNECTION_PROTOCOL_KEY = "connection.protocol";
    private static final String NETTY_SERVER_IP_KEY = "netty.server.ip";
    private static final String NETTY_SERVER_PORT_KEY = "netty.server.port";
    private static final String CONFIG_PATH = "gateway.properties";
    private static final String LOG_CONFIG_PATH = "log4j.properties";

    public static void main(String[] args) {
        initialLog4j();
        getNettyServerInfo();
        if ("TCP".equalsIgnoreCase(connection_protocol)) {
            new ObdServer(netty_server_ip, netty_server_port).run();
        }
    }

    private static void initialLog4j() {
        String logConfigPath = FileUtils.getFileAbsolutePath(LOG_CONFIG_PATH);
        PropertyConfigurator.configure(logConfigPath);
    }
    private static void getNettyServerInfo() {
        Properties prop = FileUtils.readPropertiesFile(CONFIG_PATH);
        connection_protocol = prop.getProperty(CONNECTION_PROTOCOL_KEY, "TCP").trim();
        netty_server_ip = prop.getProperty(NETTY_SERVER_IP_KEY, "127.0.0.1").trim();
        netty_server_port = Integer.parseInt(prop.getProperty(NETTY_SERVER_PORT_KEY, "8020").trim());
        log.info("Server protocal is {}, the ip is {}, the port is {}", connection_protocol, netty_server_ip, netty_server_port);
    }
}
