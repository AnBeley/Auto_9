package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import data.DataGenerator;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import java.time.Duration;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AppReplanDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("AllureSelenide");
    }

    @Test
    void shouldReplanDelivery() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("ru");
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);

        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstDelivery);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Запланировать")).click();

        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + firstDelivery));
        $("[data-test-id=success-notification] button").click();

        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondDelivery);
        $$("button").find(exactText("Запланировать")).click();

        $("[data-test-id=replan-notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();

        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + secondDelivery));
    }

    @Test
    void shouldNotPlanDeliveryToLatinName() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("eng");

        $("[data-test-id='city'] input").setValue(user.getCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotPlanDeliveryWithoutAgreement() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("ru");

        $("[data-test-id='city'] input").setValue(user.getCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldNotPlanDeliveryWithIncorrectPhone() {

        open("http://localhost:9999");
        var user = DataGenerator.RegistrationWrongPhone.generateWrongUserPhone("ru");

        $("[data-test-id='city'] input").setValue(user.getCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getWrongPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotPlanDeliveryWithIncorrectCity() {

        open("http://localhost:9999");
        var user = DataGenerator.RegistrationWrongCity.generateWrongUserCity("ru");

        $("[data-test-id='city'] input").setValue(user.getWrongCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotPlanDeliveryWithIncorrectName() {

        open("http://localhost:9999");
        var user = DataGenerator.RegistrationWrongName.generateWrongUserName("ru");

        $("[data-test-id='city'] input").setValue(user.getCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getWrongName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotPlanDeliveryWithIncorrectDate() {

        open("http://localhost:9999");
        var user = DataGenerator.RegistrationWrongName.generateWrongUserName("ru");

        $("[data-test-id='city'] input").setValue(user.getCity());

        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 1;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        //var secondDate = 8;
        // var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getWrongName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));

    }
    @Test
    void shouldReplanDelieryEmptyCity() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);


        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldReplanDelieryEmptyName() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);

        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldReplanDelieryEmptyPhone() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        var firstDate = 4;
        var firstDelivery = DataGenerator.generateDate(firstDate);
        var secondDate = 8;
        var secondDelivery = DataGenerator.generateDate(secondDate);
        $("[data-test-id='date'] input").setValue(firstDelivery);

        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldReplanDelieryEmptyDate() {

        open("http://localhost:9999");
        var user = DataGenerator.Registration.generateUser("ru");


        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);

        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(exactText("Неверно введена дата"));
    }

}