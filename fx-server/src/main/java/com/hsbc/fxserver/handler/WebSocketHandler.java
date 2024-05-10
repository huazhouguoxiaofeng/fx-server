package com.hsbc.fxserver.handler;

import com.hsbc.fxserver.manager.ImSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WS处理器
 * 保证处理器，在整个生命周期中就是以单例的形式存在，方便统计客户端的在线数量
 *
 * @author admin
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 客户端发送给服务端的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

        try {
            //接受客户端发送的消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame("afasfafff"));
        } catch (Exception e) {
            //发送消息
            log.error("websocket服务器推送消息发生错误：", e);
        }
    }

    /**
     * 客户端连接时候的操作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("一个客户端连接......" + ctx.channel().remoteAddress() + " 线程为： " + Thread.currentThread().getName());
        //添加到channelGroup
        ImSessionManager.channelGroup.add(ctx.channel());
        log.info("channelid为：{}", ctx.channel().id());
    }

    /**
     * 客户端掉线时的操作
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("收到客户端移除消息，channelid为：{}", ctx.channel().id().asLongText());
        ImSessionManager.channelGroup.remove(ctx.channel());
        log.info("一个客户端移除......" + ctx.channel().remoteAddress());
        ctx.close(); //关闭连接
    }

    /**
     * 发生异常时执行的操作
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String key = ctx.channel().id().asLongText();
        ImSessionManager.channelGroup.remove(ctx.channel());
        //关闭长连接
        ctx.close();
        log.error("异常发生,channelid为：{}", key);
        log.error("异常发生 " + cause.getMessage());
    }


}
