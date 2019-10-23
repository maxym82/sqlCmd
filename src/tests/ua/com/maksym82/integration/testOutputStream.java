package ua.com.maksym82.integration;

import java.io.IOException;
import java.io.OutputStream;

public class testOutputStream extends OutputStream {
    private String log;

    @Override
    public void write(int i) throws IOException {
        this.log += String.valueOf((char)i);

    }

    public String getData() {
        return this.log;
    }

    public void clearLog() {
        this.log = "";
    }
}
