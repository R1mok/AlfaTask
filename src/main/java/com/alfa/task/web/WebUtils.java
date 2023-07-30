package com.alfa.task.web;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.stereotype.Component;

/**
 * Вспомогательный класс для запросов
 */
@Component
public class WebUtils {

    /**
     * Метод добавляет параметры в StringBuilder
     * @param result получившийся StringBuilder
     * @param separator разделитель между параметрами
     * @param name имя параметра
     * @param value значение параметра
     */
    public void addParameter(StringBuilder result, String separator, String name, String value) {
        result.append(separator).append(name).append("=").append(value);
    }

    /**
     * Метод, который создает запрос
     * @param tClass класс клиента, наследуемый от AbstractClient
     * @param url ссылка, по которой делается запрос
     */
    public <T extends AbstractClient> AbstractClient createClient(Class<T> tClass, String url) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(tClass))
                .logLevel(Logger.Level.FULL)
                .target(tClass, url);
    }
}