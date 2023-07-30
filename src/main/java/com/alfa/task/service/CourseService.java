package com.alfa.task.service;

import com.alfa.task.web.CourseClient;
import com.alfa.task.web.WebUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Сервис для получения и обработки курса валют
 */
@Service
public class CourseService {

    private static final String APP_ID = "app_id";
    private static final String BASE = "base";

    /**
     * Токен APP_ID для получения курсов валют. Имеет лимит на 1000 получений
     */
    @Value("${app_id}")
    @Getter
    @Setter
    private String appId;
    /**
     * Базовая валюта, относительно которой нужно смотреть курс
     */
    @Value("${base_currency}")
    @Getter
    @Setter
    private String currency;

    /**
     * Ссылка на получение курсов валют за конкретный день
     */
    @Value("${historical_course_url}")
    @Getter
    @Setter
    private String historicalCourseUrl;

    private final WebUtils webUtils;

    public CourseService(WebUtils webUtils) {
        this.webUtils = webUtils;
    }

    /**
     * Метод возвращает true, если курс валюты сегодня выше, и false в обратном случае
     * @param code код валюты
     */
    public boolean getCourseDelta(String code) {
        return getYesterdayValue(code) <= getCurrentValue(code);
    }

    private Double getCurrentValue(String code) {
        return getValue(String.format(getHistoricalCourseUrl(), LocalDate.now()), code);
    }

    private Double getYesterdayValue(String code) {
        return getValue(String.format(getHistoricalCourseUrl(), LocalDate.now().minusDays(1)), code);
    }

    private Double getValue(String url, String code) {
        CourseClient courseClient = (CourseClient) webUtils.createClient(CourseClient.class, createLink(url));
        double result;
        try {
            result = courseClient.getCourse().getRates().get(code).getAsDouble();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    private String createLink(String url) {
        StringBuilder result = new StringBuilder(url);
        webUtils.addParameter(result, "?", APP_ID, getAppId());
        webUtils.addParameter(result, "&", BASE, getCurrency());
        return result.toString();
    }
}