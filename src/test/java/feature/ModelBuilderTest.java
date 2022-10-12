package feature;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.math.BigInteger.ONE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model Builder Test")
class ModelBuilderTest {

  @Nested
  @DisplayName("build")
  class BuildTestCase {
    @Test
    @DisplayName("expect to Human model not null and all property is settled")
    void success_created() {
      String expectedSomething = "anything";
      int expectedSomeNumber = 2;

      Human finalHuman = new ModelBuilder<>(Human.class)
        .with("something", expectedSomething)
        .with("someNumber", expectedSomeNumber)
        .build();

      assertAll(
        () -> assertNotNull(finalHuman),
        () -> assertTrue(finalHuman.isActive()),
        () -> assertEquals(1L, finalHuman.getLongPrimitiveNumber()),
        () -> assertEquals(1L, finalHuman.getLongNumber()),
        () -> assertEquals(1, finalHuman.getInteger()),
        () -> assertEquals(1, finalHuman.getIntPrimitive()),
        () -> assertEquals(ONE, finalHuman.getBigInteger()),
        () -> assertEquals(expectedSomeNumber, finalHuman.getSomeNumber()),
        () -> assertEquals(expectedSomething, finalHuman.getSomething()),
        () -> assertNotNull(finalHuman.getName()),
        () -> assertNotNull(finalHuman.getBirth()),
        () -> assertNotNull(finalHuman.getAddress()),
        () -> assertNotNull(finalHuman.getStringList()),
        () -> assertNotNull(finalHuman.getStringMap()),
        () -> assertNotNull(finalHuman.getJob()));
    }

    @Test
    @DisplayName("expect to throw error when set custom property with invalid type")
    void error_created() {
      Exception exception = assertThrows(Exception.class, () -> new ModelBuilder<>(Human.class)
        .with("someNumber", "something")
        .build());

      assertThat(exception.getMessage(), containsString("Can not set"));
    }
  }

  @Data
  static class Birth {
    String place;
    Date date;
  }

  @Data
  static class Address {
    String city;
    String street;
    int number;
  }

  @Data
  static class Name {
    String firstName;
    String lastName;
  }

  @Data
  @NoArgsConstructor
  static class Human {
    Name name;
    Birth birth;
    Address address;
    String job;
    boolean active;
    Long longNumber;
    long longPrimitiveNumber;
    Integer integer;
    int intPrimitive;
    List<String> stringList;
    Map<String, String> stringMap;
    String something;
    int someNumber;
    BigInteger bigInteger;
  }
}
