package com.wds.netty.selfprotocol;

/**
 * Created by wds on 2015/11/2.
 */
public class NettyMessage {
    //消息头
    private Header header;
    //消息体
    private Object boyd;

    public final Header getHeader() {
        return header;
    }

    public final void setHeader(Header header) {
        this.header = header;
    }

    public final Object getBoyd() {
        return boyd;
    }

    public final void setBoyd(Object boyd) {
        this.boyd = boyd;
    }

    @Override
    public String toString() {
        return "Metty Message [header=" + header + "]";
    }
}
