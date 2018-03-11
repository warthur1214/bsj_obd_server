package com.warthur.netty.obdserver.pojo.protocol;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MsgExtend {

    private String extend;
    private String splitType;
    private String encryptType;
    private int contentLength;

    public MsgExtend(String hex) {
        String binary = left(Integer.toBinaryString(Integer.parseInt(hex, 16)), hex.length() * 4);
        extend = binary.substring(0, 2);
        splitType = binary.substring(2, 1);
        encryptType = binary.substring(4, 3);
        contentLength = Integer.parseInt(binary.substring(7), 2);
    }

    private static String left(String s, int pos){

        StringBuilder sb = new StringBuilder();
        if(s.length()<pos){
            for(int i=0;i<pos-s.length();i++){
                sb.append("0");
            }
        }
        return sb.toString()+s;
    }
}
