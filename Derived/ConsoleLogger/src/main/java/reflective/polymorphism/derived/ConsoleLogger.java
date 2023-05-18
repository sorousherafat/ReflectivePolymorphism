package reflective.polymorphism.derived;

import reflective.polymorphism.base.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.printf("%s - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), message);
    }
}
