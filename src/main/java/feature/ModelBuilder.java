package feature;

import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.*;

import static java.math.BigInteger.ONE;

public class ModelBuilder<T> {
  private T object;

  @SneakyThrows
  public ModelBuilder(Class<T> clazz) {
    Constructor<?> constructor = clazz.getDeclaredConstructor();
    this.dataBinding(clazz.cast(constructor.newInstance()));
    this.initiatePropertyValue(this.object);
  }

  public T build() {
    return this.object;
  }

  @SneakyThrows
  public ModelBuilder<T> with(String property, Object value) {
    Field field = FieldUtils.getField(object.getClass(), property, true);
    FieldUtils.writeField(object, field.getName(), value, true);

    return this;
  }

  private void initiatePropertyValue(Object object) {
    List<Field> fields = Arrays.asList(FieldUtils.getAllFields(object.getClass()));

    fields.forEach(field -> {
      if (!Modifier.isStatic(field.getModifiers())) {
        this.handleBoolean(object, field);
        this.handleString(object, field);
        this.handleInteger(object, field);
        this.handleDate(object, field);
        this.handleLong(object, field);
        this.handleCustomObject(object, field);
        this.handleList(object, field);
        this.handleMap(object, field);
      }
    });
  }

  @SneakyThrows
  private void handleCustomObject(Object object, Field field) {
    if (isValidObject(field.getType())) {
      Constructor<?> constructor = field.getType().getDeclaredConstructor();
      Object fieldObject = constructor.newInstance();
      FieldUtils.writeField(object, field.getName(), fieldObject, true);

      this.initiatePropertyValue(fieldObject);
    }
  }

  @SneakyThrows
  private void handleList(Object object, Field field) {
    if (Collection.class.isAssignableFrom(field.getType()) && field.getType().equals(List.class)) {
      FieldUtils.writeField(object, field.getName(), new ArrayList<>(), true);
    }
  }

  @SneakyThrows
  private void handleMap(Object object, Field field) {
    if (field.getType().equals(Map.class)) {
      FieldUtils.writeField(object, field.getName(), new HashMap<>(), true);
    }
  }

  private boolean isValidObject(Class<?> clazz) {
    return !clazz.getName().startsWith("java.") && clazz.getDeclaredFields().length != 0;
  }

  private void dataBinding(T object) {
    this.object = object;
  }

  @SneakyThrows
  private void handleLong(Object object, Field field) {
    if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
      FieldUtils.writeField(object, field.getName(), 1L, true);
    }
  }

  @SneakyThrows
  private void handleString(Object object, Field field) {
    if (field.getType().equals(String.class)) {
      FieldUtils.writeField(object, field.getName(), "anyString", true);
    }
  }

  @SneakyThrows
  private void handleBoolean(Object object, Field field) {
    if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
      FieldUtils.writeField(object, field.getName(), true, true);
    }
  }

  @SneakyThrows
  private void handleInteger(Object object, Field field) {
    if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
      FieldUtils.writeField(object, field.getName(), 1, true);
    }

    if (field.getType().equals(BigInteger.class)) {
      FieldUtils.writeField(object, field.getName(), ONE, true);
    }
  }

  @SneakyThrows
  private void handleDate(Object object, Field field) {
    if (field.getType().equals(Date.class)) {
      FieldUtils.writeField(object, field.getName(), new Date(), true);
    }
  }
}
