package com.lk.station.server;

import com.lk.station.entity.Client;
import com.lk.station.util.MessageUtil;
import com.lk.station.value.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public class MulticastServer implements Runnable {

    private static Logger log = LoggerFactory.getLogger(MulticastServer.class);

    private String host;

    private MulticastSocket serverSocket;

    public MulticastServer(){

    }

    public MulticastServer(String host){
            this.host = host;
    }

    public void start() {

        try {
            this.serverSocket = new MulticastSocket(Config.MULTICAST_PORT);
            this.serverSocket.setInterface(InetAddress.getByName(this.host));
            this.serverSocket.joinGroup(InetAddress.getByName(Config.MULTICAST_ADDRESS));
            while (true){
                this.accept();
            }

        } catch (SocketException e) {
            log.warn("组播异常！");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            log.warn("未知的主机！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void accept(){
        byte[] buff = new byte[65535];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        try {
            this.serverSocket.receive(datagramPacket);
            MessageUtil.test(datagramPacket.getData());
            Client client = MessageUtil.messageToObject(datagramPacket.getData());
            String sendIp = datagramPacket.getAddress().getHostAddress();
            if (!sendIp.equals(this.host) && client.getType() == 1){
//            if (!sendIp.equals(this.host))
                this.send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void send(){
        try {
            byte [] buff = MessageUtil.getMessage(this.host);
            DatagramPacket datagramPacket =
                    new DatagramPacket(buff, buff.length,InetAddress.getByName(Config.MULTICAST_ADDRESS), Config.MULTICAST_PORT);
            this.serverSocket.send(datagramPacket);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        this.start();
    }
}
