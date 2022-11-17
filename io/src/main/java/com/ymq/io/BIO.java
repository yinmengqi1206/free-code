package com.ymq.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * 阻塞id
 * @author yinmengqi
 * @version 1.0
 * @date 2022/9/16 11:18
 */
@Slf4j
public class BIO {

    @SneakyThrows
    public static void main(String[] args){
        ServerSocket serverSocket = new ServerSocket(1206);
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            log.info("{},连接到服务端",socket.getRemoteSocketAddress());
            new Thread(()->{
                byte[] bytes = new byte[1024];
                int len = 0;
                try {
                    while ((len = inputStream.read(bytes))>0){
                        String trim = new String(bytes).trim();
                        if(Objects.equals("exit",trim)){
                            socket.close();
                            return;
                        }
                        log.info("{}发送数据:{}",socket.getRemoteSocketAddress(),trim);
                        outputStream.write(bytes,0,len);
                    }
                    Thread.currentThread().interrupt();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
            log.info("程序结束");
        }
    }
}
