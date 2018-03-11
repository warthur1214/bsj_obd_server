package com.warthur.netty.obdserver.codec;

import com.warthur.netty.obdserver.pojo.protocol.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class ObdMessageDecoder extends MessageToMessageDecoder<Message> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {

    }
}
