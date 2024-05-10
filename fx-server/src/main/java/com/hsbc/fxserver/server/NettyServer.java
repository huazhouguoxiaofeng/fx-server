package com.hsbc.fxserver.server;

import com.hsbc.fxserver.init.WebsocketInitialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * websocket服务
 */
@Slf4j
@Component
public class NettyServer {

    /**
     * 当然，生产中从配置文件取
     *  candidate 注
     */
    @Value("${websocket.port:8888}")
    private Integer port;

    @Resource
    private WebsocketInitialization websocketInitialization;

    /**
     * 启动websocket服务
     */
    @PostConstruct
    public void start() {
        log.info(Thread.currentThread().getName() + ":websocket启动中......port[{}]", port);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    // 启动websocket服务
                    websocketInitialization.init(port);
                    log.info(Thread.currentThread().getName() + ":websocket启动成功！！！port[{}]", port);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}