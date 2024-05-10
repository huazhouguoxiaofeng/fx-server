package com.hsbc.fxserver.init;

import com.hsbc.fxserver.handler.WebSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class WebsocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private WebSocketHandler webSocketHandler;

    @Value("${websocket.url:fxws}")
    private String websocketUrl;

    /**
     * 通道初始化
     *
     * @param socketChannel
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //获取pipeline通道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //因为基于http协议，使用http的编码和解码器
        pipeline.addLast(new HttpServerCodec());
        //是以块方式写，添加ChunkedWriteHandler处理器
        pipeline.addLast(new ChunkedWriteHandler());
        /*
          说明
          1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
          2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
        */
        pipeline.addLast(new HttpObjectAggregator(8192));
        /* 说明
          1. 对应websocket ，它的数据是以 帧(frame) 形式传递
          2. 可以看到WebSocketFrame 下面有六个子类
          3. 浏览器请求时  表示请求的uri
          4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
          5. 是通过一个 状态码 101
        */
        pipeline.addLast(new WebSocketServerProtocolHandler(websocketUrl));
        //自定义的handler ，处理业务逻辑
        pipeline.addLast(webSocketHandler);
        /*
         * 客户端发现 180 秒未从服务端读取到消息，主动断开连接
         */
//        pipeline.addLast(new ReadTimeoutHandler(180, TimeUnit.SECONDS));
        /*
         * 服务端每 60 秒向服务端发起一次心跳消息，保证客户端端可以读取到消息。有3次机会保证不断开
         */
//        pipeline.addLast(new IdleStateHandler(60, 0, 0));

    }
}