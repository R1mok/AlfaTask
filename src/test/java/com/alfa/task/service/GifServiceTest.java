package com.alfa.task.service;

import com.alfa.task.response.GifResponse;
import com.alfa.task.web.GifClient;
import com.alfa.task.web.WebUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GifServiceTest {

    private static final String CODE = "USD";
    private static final String RICH_URL = "https://gif/rich";
    private static final String BROKE_URL = "https://gif/broke";

    @InjectMocks
    private GifService gifService;
    @Mock
    private CourseService courseService;
    @Mock
    private WebUtils webUtils;
    @Mock
    private GifClient richGifClient;
    @Mock
    private GifClient brokeGifClient;

    /**
     * Тест проверяет возврат нужного url относительно того, какой запрос приходит от сервиса курса валют
     * @param courseValue значение, которое приходит от курса валют
     */
    @ParameterizedTest
    @ValueSource(booleans =  {true, false})
    public void getGifTest(boolean courseValue) {
        mockService(courseValue);
        Assertions.assertThat(gifService.getGif(CODE)).isEqualTo(courseValue ? RICH_URL : BROKE_URL);
    }

    private void mockService(boolean courseServiceValue) {
        when(courseService.getCourseDelta(CODE)).thenReturn(courseServiceValue);
        if (courseServiceValue) {
            when(richGifClient.getGif()).thenReturn(createGifResponse(RICH_URL));
            doReturn(richGifClient).when(webUtils).createClient(any(), eq(RICH_URL));
            gifService.setGifUrl(RICH_URL);
        } else {
            when(brokeGifClient.getGif()).thenReturn(createGifResponse(BROKE_URL));
            doReturn(brokeGifClient).when(webUtils).createClient(any(), eq(BROKE_URL));
            gifService.setGifUrl(BROKE_URL);
        }
    }

    private GifResponse createGifResponse(String url) {
        GifResponse gifResponse = new GifResponse();
        JsonObject previewWebp = new JsonObject();
        previewWebp.add("url", new JsonPrimitive(url));
        JsonObject imagesObject = new JsonObject();
        imagesObject.add("preview_webp", previewWebp);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("images", imagesObject);
        gifResponse.setData(jsonObject);
        return gifResponse;
    }
}
