package com.epam.esm.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServerIpInfoContributor implements InfoContributor {

    private static final Logger log = LoggerFactory.getLogger(ServerIpInfoContributor.class);

    @Override
    public void contribute(Info.Builder builder) {
        try {
            String address = InetAddress.getLocalHost().getHostAddress();
            builder.withDetail("server.address", address);
        } catch (UnknownHostException e) {
            log.error("Unknown Host Exception", e);
        }
    }
}
