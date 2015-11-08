package com.wds.netty.selfprotocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wds on 2015/11/2.
 */
public class Header implements Serializable {
    private int crcCode = 0xabef0101;

    /**
     * 消息长度
     */
    private int length;
    /**
     * 会话ID
     */
    private long sessionId;

    /**
     * 消息类别
     */
    private byte type;

    /**
     * 消息优先级
     */
    private byte priority;

    /**
     * 附件
     */
    private Map<String, Object> attchment = new HashMap<>();

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionId=" + sessionId +
                ", type=" + type +
                ", priority=" + priority +
                ", attchment=" + attchment +
                '}';
    }

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttchment() {
        return attchment;
    }

    public void setAttchment(Map<String, Object> attchment) {
        this.attchment = attchment;
    }
}
