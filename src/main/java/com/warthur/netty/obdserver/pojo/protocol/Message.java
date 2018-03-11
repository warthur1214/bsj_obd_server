package com.warthur.netty.obdserver.pojo.protocol;

import lombok.Data;

@Data
public class Message {

    private String head;
    private MsgHeader header;
    private MsgExtend msgExtend;
    private MsgContent content;
    private String verifyNo;
    private String tail;

    public Message() {
    }
}
