package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    public static Faker fakereng = new Faker(new Locale("eng"));

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;

    }

    @Value
    public static class WrongPhoneUserInfo {
        String city;
        String name;
        String wrongPhone;
    }

    @Value
    public static class WrongCityUserInfo {
        String wrongCity;
        String name;
        String phone;
    }

    @Value
    public static class WrongNameUserInfo {
        String city;
        String wrongName;
        String phone;
    }

    public static String generateDate(int shift) {
        // TODO: добавить логику для объявления переменной date и задания её значения, для генерации строки с датой
        // Вы можете использовать класс LocalDate и его методы для получения и форматирования даты
        LocalDate calendar = LocalDate.now();
        LocalDate delivery = calendar.plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = formatter.format(delivery);
        return date;
    }

    public static String generateCity(String locale) {
        // TODO: добавить логику для объявления переменной city и задания её значения, генерацию можно выполнить
        // с помощью Faker, либо используя массив валидных городов и класс Random
        Random random = new Random();
        String[] cities = {"Москва", "Санкт-Петербург", "Самара"};
        int index = random.nextInt(cities.length);
        return (cities[index]);
    }

    public static String generateWrongCity(String locale) {
        // TODO: добавить логику для объявления переменной city и задания её значения, генерацию можно выполнить
        // с помощью Faker, либо используя массив валидных городов и класс Random
        Random random = new Random();
        String[] cities = {"первый", "второй", "третий"};
        int index = random.nextInt(cities.length);
        return (cities[index]);
    }

    public static String generateName(String locale) {
        // TODO: добавить логику для объявления переменной name и задания её значения, для генерации можно
        // использовать Faker
        Faker faker = new Faker(new Locale(locale));
        return faker.name().lastName() + "-" + faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String generateWrongName(String locale) {
        // TODO: добавить логику для объявления переменной name и задания её значения, для генерации можно
        // использовать Faker
        Faker faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName() + "="+ faker.name().firstName();
    }

    public static String generatePhone(String locale) {
        // TODO: добавить логику для объявления переменной phone и задания её значения, для генерации можно
        // использовать Faker
        Faker faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static String generateWrongPhone(String locale) {
        // TODO: добавить логику для объявления переменной phone и задания её значения, для генерации можно
        // использовать Faker
        return fakereng.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            // TODO: добавить логику для создания пользователя user с использованием методов generateCity(locale),
            // generateName(locale), generatePhone(locale)
            return new UserInfo(generateCity(locale), generateName(locale), generatePhone(locale));
        }

    }
    public static class RegistrationWrongPhone {
        private RegistrationWrongPhone() {
        }

        public static WrongPhoneUserInfo generateWrongUserPhone(String locale) {
            // TODO: добавить логику для создания пользователя user с использованием методов generateCity(locale),
            // generateName(locale), generatePhone(locale)
            return new WrongPhoneUserInfo (generateCity(locale), generateName(locale), generateWrongPhone(locale));
        }
    }

    public static class RegistrationWrongCity {
        private RegistrationWrongCity() {
        }

        public static WrongCityUserInfo generateWrongUserCity(String locale) {
            // TODO: добавить логику для создания пользователя user с использованием методов generateCity(locale),
            // generateName(locale), generatePhone(locale)
            return new WrongCityUserInfo (generateWrongCity(locale), generateName(locale), generatePhone(locale));
        }
    }
    public static class RegistrationWrongName {
        private RegistrationWrongName() {
        }

        public static WrongNameUserInfo generateWrongUserName(String locale) {
            // TODO: добавить логику для создания пользователя user с использованием методов generateCity(locale),
            // generateName(locale), generatePhone(locale)
            return new WrongNameUserInfo (generateCity(locale), generateWrongName(locale), generatePhone(locale));
        }
    }
}
