package annotations;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ObjectConverter {
    public String apply(Object object) throws JsonSerializationException {
        try {
            checkIfAnnoteted(object);
            runAnnotateMethod(object);
            return object.toString();
        } catch (Exception e) {
            throw new JsonSerializationException(e.getMessage());
        }
    }

    private void checkIfAnnoteted(Object object) {
        if (Objects.isNull(object)) {
            throw new JsonSerializationException("Can't convert a null object");
        }
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            throw new JsonSerializationException("The class " + clazz.getSimpleName() + " is not annotated with JsonSerializable");
        }
    }

    private void runAnnotateMethod(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Init.class)) {
            	method.setAccessible(true);
                method.invoke(object);                
            }
        }
    }
}