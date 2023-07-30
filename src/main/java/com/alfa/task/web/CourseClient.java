package com.alfa.task.web;

import com.alfa.task.response.CourseResponse;
import feign.RequestLine;

/**
 * Запрос для получения курса валют
 */
public interface CourseClient extends AbstractClient {
    @RequestLine("GET")
    CourseResponse getCourse();
}
