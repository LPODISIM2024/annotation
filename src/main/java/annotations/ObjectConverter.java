package annotations;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectConverter {
    public String apply(Object object) throws JsonSerializationException {
        try {
            checkIfAnnoteted(object);
            runAnnotateMethod(object);
            return getJsonString(object);
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
    
    
    private String getJsonString(Object object) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
//        Map<String, String> jsonElementsMap = new HashMap<>();
        String jsonString = "";
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(JsonElement.class)) {
                jsonString += field.getName() + ":" + (String) field.get(object) + ";";
            }
        }

        
        return "{" + jsonString.substring(0,jsonString.length()-1) + "}";
    }

    private String getKey(Field field) {
        String value = field.getAnnotation(JsonElement.class)
            .key();
        return value.isEmpty() ? field.getName() : value;
    }
}