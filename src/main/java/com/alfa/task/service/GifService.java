package com.alfa.task.service;

import com.alfa.task.web.GifClient;
import com.alfa.task.web.WebUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки и получения gif
 */
@Service
public class GifService {
    private static final String API_KEY = "api_key";
    private static final String TAG = "tag";
    private static final String RATING = "rating";
    public static final String RICH = "rich";
    public static final String BROKE = "broke";

    private final CourseService courseService;
    private final WebUtils webUtils;

    /**
     * Токен для получения gif с сайта https://giphy.com/
     */
    @Value("${giphy_code}")
    @Getter
    @Setter
    private String giphyCode;
    /**
     * Возрастное ограничение gif изображения
     */
    @Value("${rating}")
    @Getter
    @Setter
    private String rating;
    /**
     * Ссылка на получение случайного gif изображения
     */
    @Value("${random_gif_url}")
    @Getter
    @Setter
    private String gifUrl;

    public GifService(CourseService courseService, WebUtils webUtils) {
        this.courseService = courseService;
        this.webUtils = webUtils;
    }

    public String getGif(String code) {
        if (courseService.getCourseDelta(code)) {
            return getResultGif(RICH);
        } else {
            return getResultGif(BROKE);
        }
    }

    private String getResultGif(String mark) {
        GifClient gifClient = (GifClient) webUtils.createClient(GifClient.class, createLink(mark));
        return gifClient.getGif().getOriginalImage();
    }

    private String createLink(String tag) {
        StringBuilder result = new StringBuilder(gifUrl);
        webUtils.addParameter(result, "?", API_KEY, giphyCode);
        webUtils.addParameter(result, "&", TAG, tag);
        webUtils.addParameter(result, "&", RATING, rating);
        return result.toString();
    }
}
