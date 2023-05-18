package reflective.polymorphism.program;

import reflective.polymorphism.base.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Program {
    private static final String usage = "Usage: program <logger-name>";

    public static void main(String[] args) {
        checkInput(args);
        ReflectiveLoggerProvider typeProvider = new ReflectiveLoggerProvider(args[0]);
        Logger logger = typeProvider.getLogger();
        logUserInput(logger);
    }

    private static void logUserInput(Logger logger) {
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.nextLine();
            if (input.equals("exit"))
                break;
            logger.log(input);
        }
    }


    private static void checkInput(String[] args) {
        if (args.length != 1) {
            System.out.println(usage);
            System.exit(1);
        }
    }

    static class ReflectiveLoggerProvider {
        private final String loggerName;

        public ReflectiveLoggerProvider(String loggerName) {
            this.loggerName = getQualifiedTypeName(loggerName);
        }

        public Logger getLogger() {
            Class<?> loggerClass = getLoggerClass();
            Constructor<?> loggerConstructor = getConstructor(loggerClass);
            Object loggerObject = getObject(loggerConstructor);
            return getLogger(loggerClass, loggerObject);
        }

        private Logger getLogger(Class<?> derivedClass, Object reflectedInstance) {
            if (!derivedClass.isInstance(reflectedInstance)) {
                displayErrorAndExit("Generated object is not an instance of class: " + loggerName);
            }
            return (Logger) reflectedInstance;
        }

        private Object getObject(Constructor<?> relfectedConstructor) {
            Object reflectedInstance = null;
            try {
                reflectedInstance = relfectedConstructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                displayErrorAndExit("An error occurred when instantiating an object of class: " + loggerName + "\n" + exception.getMessage());
            }
            return reflectedInstance;
        }

        private Constructor<?> getConstructor(Class<?> derivedClass) {
            Constructor<?> relfectedConstructor = null;
            try {
                relfectedConstructor = derivedClass.getConstructor();
            } catch (NoSuchMethodException exception) {
                displayErrorAndExit("Could not find a parameterless constructor for class: " + loggerName);
            }
            return relfectedConstructor;
        }

        private Class<?> getLoggerClass() {
            Class<?> reflectedClass = null;
            try {
                reflectedClass = Class.forName(loggerName);
            } catch (ClassNotFoundException exception) {
                displayErrorAndExit("Could not find the class: " + loggerName);
            }
            return reflectedClass;
        }

        private void displayErrorAndExit(String message) {
            System.err.println(message);
            System.exit(1);
        }

        private String getQualifiedTypeName(String args) {
            return "reflective.polymorphism.derived." + args;
        }
    }
}
