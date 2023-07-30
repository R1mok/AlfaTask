package com.alfa.task.service;

import com.alfa.task.response.CourseResponse;
import com.alfa.task.web.CourseClient;
import com.alfa.task.web.WebUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    private final static String COURSE = "USD";
    private final static Double YESTERDAY_VALUE = 1.3;
    private final static Double TODAY_VALUE = 1.2;
    private final static String HISTORICAL_COURSE_URL = "https://openexchangerates.org/api/historical/%s.json";

    private CourseService courseService;
    @Mock
    private WebUtils webUtils;
    @Mock
    private CourseClient todayCourseClient;
    @Mock
    private CourseClient yesterdayCourseClient;

    @BeforeEach
    public void setUp() {
        courseService = new CourseService(webUtils);
        courseService.setHistoricalCourseUrl(HISTORICAL_COURSE_URL);
    }

    /**
     * Тест проверяет вычисление курса валют в методе getCourseDelta
     * @param expectedValue ожидаемое значение, возвращаемое из метода сервиса
     */
    @ParameterizedTest
    @ValueSource(booleans =  {true, false})
    public void getCourseDeltaTest(boolean expectedValue) {
        callExpectedMock(expectedValue);
        Assertions.assertThat(courseService.getCourseDelta(COURSE)).isEqualTo(expectedValue);
    }

    private void callExpectedMock(boolean expectedValue) {
        if (expectedValue) {
            when(webUtils.createClient(any(), any())).thenReturn(todayCourseClient);
            when(todayCourseClient.getCourse()).thenReturn(createCourseResponse(TODAY_VALUE));
        } else {
            doReturn(todayCourseClient)
                    .when(webUtils).createClient(CourseClient.class, getCourseLink(LocalDate.now()));
            when(todayCourseClient.getCourse()).thenReturn(createCourseResponse(TODAY_VALUE));
            doReturn(yesterdayCourseClient)
                    .when(webUtils).createClient(CourseClient.class, getCourseLink(LocalDate.now().minusDays(1)));
            when(yesterdayCourseClient.getCourse()).thenReturn(createCourseResponse(YESTERDAY_VALUE));
        }
    }

    private CourseResponse createCourseResponse(double value) {
        CourseResponse courseResponse = new CourseResponse();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("USD", new JsonPrimitive(value));
        courseResponse.setRates(jsonObject);
        return courseResponse;
    }

    private String getCourseLink(LocalDate date) {
        return String.format(HISTORICAL_COURSE_URL, date);
    }
}
