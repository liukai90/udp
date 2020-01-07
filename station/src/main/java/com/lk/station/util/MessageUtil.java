package com.lk.station.util;

import com.google.gson.Gson;
import com.lk.station.entity.Client;
import com.lk.station.value.Config;

public class MessageUtil {

    public static void test(byte[] data) throws Exception{
        int magic = (data[1] & 0x0ff) + ((data[0] & 0x0ff) << 8);
        if (magic != Config.MAGIC) {
            System.out.println("magic error!");
            throw new Exception();
        }
        int len = (data[3] & 0x0ff) + ((data[2] & 0x0ff) << 8);
        String result = new String(data, 4, len);
        System.out.println(result);
    }


    public static byte[] getMessage(String host) {
        String msg = "{\n" +
                "    \"name\":\"router1\",\n" +
                "    \"mac\":\"ec-d2-3d-1e-1c\",\n" +
                "    \"type\":2,\n" +
                "    \"host\":\"" + host + "\",\n" +
                "    \"protocol\":\"http\",\n" +
                "    \"modules\":{\n" +
                "        \"launch\":{\n" +
                "            \"port\":3010\n" +
                "        },\n" +
                "        \"message\":{\n" +
                "            \"port\":3010\n" +
                "        },\n" +
                "        \"apps\":{\n" +
                "            \"port\":3010\n" +
                "        }\n" +
                "    }\n" +
                "}";
        byte[] data = msg.getBytes();
        byte[] buff = new byte[data.length + 4];
        int len = data.length;
        buff[0] = (byte) ((Config.MAGIC >> 8) & 0xff);
        buff[1] = (byte) (Config.MAGIC & 0xff);
        buff[2] = (byte) ((len >> 8) & 0xff);
        buff[3] = (byte) (len & 0xff);
        System.arraycopy(data, 0, buff, 4, len);
        return buff;
    }

    public static Client messageToObject(byte[] data) throws Exception {
        int magic = (data[1] & 0x0ff) + ((data[0] & 0x0ff) << 8);
        if (magic != Config.MAGIC) {
            System.out.println("magic error!");
            throw new Exception();
        }
        int len = (data[3] & 0x0ff) + ((data[2] & 0x0ff) << 8);
        String result = new String(data, 4, len);
        System.out.println(result);
        Gson gson = new Gson();
        Client client = gson.fromJson(result, Client.class);
        return client;
    }


}
