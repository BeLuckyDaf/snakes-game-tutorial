package snakes;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 *
 */
public class BotLoader extends ClassLoader {

    /**
     *
     * @param classBinName
     * @return
     */
    public Bot getBotClass(String classBinName) {
        try {
            // Create a new JavaClassLoader
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            // Load the target class using its binary name
            Class<?> loadedMyClass = classLoader.loadClass(classBinName);
            boolean isBot = Arrays.asList(loadedMyClass.getInterfaces()).contains(Bot.class);
            if (isBot) {
                System.out.println("Loaded bot.");
            } else {
                System.out.println("Did not match interface.");
            }
            System.out.println("Loaded class name: " + loadedMyClass.getName());

            Class<? extends Bot> botClass = loadedMyClass.asSubclass(Bot.class);
            Constructor<? extends Bot> botClassCtor = botClass.getConstructor();
            return botClassCtor.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
