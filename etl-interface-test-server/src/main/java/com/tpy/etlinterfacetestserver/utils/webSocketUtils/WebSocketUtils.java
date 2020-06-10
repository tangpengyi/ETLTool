package com.tpy.etlinterfacetestserver.utils.webSocketUtils;

import com.tpy.etlinterfacetestserver.api.ResponseDate;
import com.tpy.etlinterfacetestserver.utils.ResponseDateUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebSocketUtils {

    private static Log log = LogFactory.getLog(WebSocketUtils.class);

    private static WebSocketClient client = null;
    private static String onMessage = null;


    public static void main(String[] args) throws ParseException {
        ResponseDate content = connection("ws://121.40.165.18:8800");
        System.out.println(content.toString());
        ResponseDate responseDate = sendMsg("hello");
        System.out.println(responseDate);
    }

    public static ResponseDate connection(String url) throws ParseException {

        try {
            client = new WebSocketClient(new URI(url), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("握手成功");
                }

                @Override
                public void onMessage(String msg) {
                    log.info("收到消息==========" + msg);

                    onMessage = msg;
                    log.info("消息===="+onMessage);
                    if (msg.equals("over")) {
                        client.close();
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("链接已关闭");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    log.info("发生错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.connect();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = df.parse(df.format(new Date()));

        //logger.info(client.getDraft());
        while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            log.info("正在连接...");
            Date newNow = df.parse(df.format(new Date()));
            long l=newNow.getTime()-now.getTime();
            if(l == 5000){
                return ResponseDateUtils.getSuccessDate("连接时间超时");
            }
        }
        return ResponseDateUtils.getSuccessDate("连接成功");
    }

    public static ResponseDate sendMsg(String news){
        if(client == null){
            return ResponseDateUtils.getFailureDate("未连接服务器");
        }
        client.send(news);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseDateUtils.getSuccessDate(onMessage);
    }

    public static ResponseDate disconnect(){
        client.close();
        return ResponseDateUtils.getSuccessDate("断开连接");
    }

}
