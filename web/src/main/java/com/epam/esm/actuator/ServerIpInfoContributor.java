package com.epam.esm.actuator;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServerIpInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        try {
            String address = InetAddress.getLocalHost().getHostAddress();
            builder.withDetail("server.address", address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
