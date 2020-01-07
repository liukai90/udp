package com.lk.station.server;

import com.google.gson.Gson;
import com.lk.station.entity.Client;
import com.lk.station.util.MessageUtil;
import com.lk.station.value.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class BroadcastServer implements Runnable {

    private static Logger log = LoggerFactory.getLogger(BroadcastServer.class);

    private String host;

    private DatagramSocket broadcastSocket;

    public BroadcastServer() {

    }

    public BroadcastServer(String host){
        this.host = host;
    }

    public void accept() throws Exception{
        byte[] buff = new byte[65535];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        this.broadcastSocket.receive(datagramPacket);
        Client client = MessageUtil.messageToObject(datagramPacket.getData());
        if (client.getType() == 1){
            log.info(new Gson().toJson(client));
            this.send();
        }
    }

    public void send() throws Exception{
        byte [] buff = MessageUtil.getMessage(this.host);
        DatagramPacket datagramPacket =
                new DatagramPacket(buff, buff.length, InetAddress.getByName(Config.BROADCAST_ADDRESS), Config.BROADCAST_PORT);
        this.broadcastSocket.send(datagramPacket);

    }

    public void run() {

        try {
            this.broadcastSocket = new DatagramSocket(Config.BROADCAST_PORT);
            while(true){
                this.accept();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
