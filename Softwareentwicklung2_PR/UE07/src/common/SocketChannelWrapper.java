package common;

import contacts.Contact;
import contacts.Entity;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelWrapper implements Closeable {
    private final SocketChannel channel;

    private final ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_CAP);
    private CharBuffer charBuffer = null;

    public SocketChannelWrapper(SocketChannel channel) {
        this.channel = channel;
    }

    public CharBuffer getCharBuffer() {
        return charBuffer;
    }

    public void write (Contact con) throws IOException {
        Entity[] ent = con.getPair();
        this.write(ent[0].getId() + ";" + ent[0].getX() + ";" + ent[0].getY() + ";" + ent[1].getId() + ";" + ent[1].getX() + ";" + ent[1].getY());
    }

    public void write (String msg) throws IOException {
        this.write(CharBuffer.wrap(msg));
    }

    public void write (CharBuffer charBuffer) throws IOException {
        channel.write(Constants.CSET.encode(charBuffer));
    }

    public int readData() throws IOException {
        buffer.clear();

        int bytesRead = channel.read(buffer);
        if (bytesRead >= 0) {
            buffer.flip();
            charBuffer = Constants.CSET.decode(buffer);
            System.out.println("Read " + bytesRead + " bytes");
        } else {
            charBuffer = null;
        }
        return bytesRead;
    }

    public void killOutput() throws IOException {
        channel.close();
    }

    @Override
    public void close() { /* Leave unimplemented so that Runnables can finishd their actions */ }
}
