package com.tmall.chat_server.handler;

import com.tmall.common.chat_enumeration.MessageType;
import com.tmall.common.dto.ChatMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class AbstractChatMessageHandler {
    //广播的时候作为消息发送方。
    protected static final String SYSTEM_SENDER = "系统提示";

    abstract public void handle(ChatMessage message, Selector server, SelectionKey client, AtomicInteger onlineUsers) throws InterruptedException;

    protected void broadcast(byte[] data, Selector server) throws IOException {
        for (SelectionKey selectionKey : server.keys()) {
            Channel channel = selectionKey.channel();
            if (channel instanceof SocketChannel) {
                SocketChannel dest = (SocketChannel) channel;
                if (dest.isConnected()) {
                    dest.write(ByteBuffer.wrap(data));
                }
            }
        }
    }

    /**
     * 判断消息处理类是否支持该类型消息的处理
     * @param messageType 消息类型
     * @return
     */
    abstract public boolean supports(MessageType messageType);
}
