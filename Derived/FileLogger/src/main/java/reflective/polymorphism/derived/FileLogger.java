package reflective.polymorphism.derived;

import reflective.polymorphism.base.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements Logger {
    private OutputStream outputStream;

    public FileLogger() {
        File file = new File("/tmp/reflective-polymorphism.log");
        try {
            file.createNewFile();
            this.outputStream = new FileOutputStream(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void log(String message) {
        message = String.format("%s - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), message);
        try {
            outputStream.write(message.getBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
