package com.hsbc.fxserver.business;

import com.hsbc.fxserver.manager.ImSessionManager;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这是获取外汇交易并且发送给web端的业务代码
 */
@Slf4j
@RestController
@RequestMapping("/fx")
public class FxHandler {

    @GetMapping("/sendMsgToWeb")
    public void getUserById() {

        //获取所有连接的客户端，并发送汇率数据
        ChannelGroup channelGroup = ImSessionManager.channelGroup;
        for (Channel channel : channelGroup) {
            //推送消息
            channel.writeAndFlush(new TextWebSocketFrame("这是汇率数据"));
        }

    }

}
