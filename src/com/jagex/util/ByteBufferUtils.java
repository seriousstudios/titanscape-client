package com.jagex.util;

import java.nio.ByteBuffer;

public class ByteBufferUtils {

    public static int readU24Int(ByteBuffer buffer) {
        return (buffer.get() & 0x0ff) << 16 | (buffer.get() & 0x0ff) << 8 | (buffer.get() & 0x0ff);
    }

    public static void write24Int(ByteBuffer buffer, int value) {
        buffer.put((byte) (value >> 16)).put((byte) (value >> 8)).put((byte) value);
    }

}
