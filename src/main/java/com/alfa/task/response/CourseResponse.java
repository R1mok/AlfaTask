package com.alfa.task.response;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Ответ по запросу получения курса валют
 */
@Component
public class CourseResponse {
    /**
     * Базовая валюта, относительно которой считаются остальные валюты
     */
    @Getter
    @Setter
    private String base;
    /**
     * Список валют
     */
    @Getter
    @Setter
    private JsonObject rates;
}
