package com.hsbc.fxserver.manager;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 *
 * @author admin
 * @description: 管理用户的session
 */
public class ImSessionManager {
    /**
     * 存储channelgroup
     */
    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}
