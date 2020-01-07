package com.lk.station.server;

import com.lk.station.entity.Client;
import com.lk.station.util.MessageUtil;
import com.lk.station.value.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class UnicastServer implements Runnable {

    private static Logger log = LoggerFactory.getLogger(UnicastServer.class);

    private String host;

    private DatagramSocket datagramSocket;

    public UnicastServer() {

    }

    public UnicastServer(String host) {
        this.host = host;
    }

    public void accept() {

        try {
            this.datagramSocket = new DatagramSocket(Config.UNICAST_PORT, InetAddress.getByName(this.host));
            byte[] buff = new byte[65535];
            DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
            while (true) {
                try {
                    this.datagramSocket.receive(datagramPacket);
                    MessageUtil.test(datagramPacket.getData());
                    Client client = MessageUtil.messageToObject(datagramPacket.getData());
                    log.info("remote host=" + datagramPacket.getSocketAddress());
                    this.sendMessage(datagramPacket);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(DatagramPacket datagramPacket) {
        try {
            byte[] buff = MessageUtil.getMessage(this.host);
            datagramPacket.setData(buff);
            this.datagramSocket.send(datagramPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.accept();
    }
}
