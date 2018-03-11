package com.warthur.netty.obdserver.pojo.protocol;

import lombok.Data;

@Data
public class MsgHeader {

    private String msgType;
    private String msgAttr;
    private String deviceNo;
    private String serialNo;
}
