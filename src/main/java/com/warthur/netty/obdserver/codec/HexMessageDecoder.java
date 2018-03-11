package com.warthur.netty.obdserver.codec;

import com.warthur.netty.obdserver.common.util.Constants;
import com.warthur.netty.obdserver.pojo.protocol.MsgExtend;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.util.List;

@Slf4j
public class HexMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 20) {
            return;
        }
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        String msg = Hex.encodeHexString(bytes);

        if (!msg.substring(0,2).equalsIgnoreCase(Constants.MSG_DELIMITER)) {
            return;
        }

        MsgExtend extend = new MsgExtend(msg.substring(6, 2));
        String message = msg.substring(0, 24 + extend.getContentLength());

        out.add(msg);
    }
}
