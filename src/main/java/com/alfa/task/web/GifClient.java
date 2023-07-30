package com.alfa.task.web;

import com.alfa.task.response.GifResponse;
import feign.RequestLine;

/**
 * Запрос для получения gif картинки
 */
public interface GifClient extends AbstractClient {
    @RequestLine("GET")
    GifResponse getGif();
}
