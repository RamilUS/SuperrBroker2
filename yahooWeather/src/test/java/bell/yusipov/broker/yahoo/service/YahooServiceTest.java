package bell.yusipov.broker.yahoo.service;


import bell.usipov.broker.dtomodule.model.DtoWeather;
import bell.yusipov.broker.yahoo.jms.WeatherSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Тестирование сервиса модуля Yahoo
 */
@RunWith(MockitoJUnitRunner.class)
public class YahooServiceTest {


    private String jsonString = "{\"location\":" +
            "{\"woeid\":44418," +
            "\"city\":\"London\"," +
            "\"region\":\" England\"," +
            "\"country\":\"United Kingdom\"," +
            "\"lat\":51.506401," +
            "\"long\":-0.12721," +
            "\"timezone_id\":\"Europe/London\"" +
            "}," +
            "\"current_observation\":{" +
            "\"wind\":{" +
            "\"chill\":10," +
            "\"direction\":205," +
            "\"speed\":21.0" +
            "}," +
            "\"atmosphere\":{" +
            "\"humidity\":70," +
            "\"visibility\":10.0," +
            "\"pressure\":1007.0," +
            "\"rising\":0" +
            "}," +
            "\"astronomy\":{" +
            "\"sunrise\":\"6:16 am\"," +
            "\"sunset\":\"6:05 pm\"" +
            "}," +
            "\"condition\":{" +
            "\"text\":\"Mostly Cloudy\"," +
            "\"code\":28," +
            "\"temperature\":11" +
            "}," +
            "\"pubDate\":1552676400" +
            "}," +
            "\"forecasts\":" +
            "[{\"day\":\"Fri\"," +
            "\"date\":1552608000," +
            "\"low\":8," +
            "\"high\":14," +
            "\"text\":\"Mostly Cloudy\"," +
            "\"code\":28}]}";

    @Mock
    private YahooRequest yahooRequest;

    @Mock
    private WeatherSender weatherSender;

    @Spy
    @InjectMocks
    private YahooServiceImpl yahooServiceImpl;

    /**
     * Проверка объектов теста на null
     */
    @Before
    public void checkNull() {
        Assert.assertNotNull(yahooServiceImpl);
        Assert.assertNotNull(weatherSender);
        Assert.assertNotNull(yahooRequest);
    }

    /**
     * Тестирование метода отправки запроса в Yahoo
     */
    @Test
    public void requestTest() {
        String location = "Moscow";
        DtoWeather weather = mock(DtoWeather.class);

        when(yahooRequest.requestToYahoo(location)).thenReturn(weather);
        yahooServiceImpl.request(location);
        verify(yahooRequest, atLeast(1)).requestToYahoo(location);

    }

    /**
     * Проверка запроса при null параметре
     */
    @Test
    public void nullRequestTest() {
        String nullLocation = null;
        yahooServiceImpl.request(nullLocation);
        verifyZeroInteractions(yahooRequest);
    }

    /**
     * Проверка запроса при пустом параметре
     */
    @Test
    public void emptyRequestTest() {
        String emptyLocation = "";

        yahooServiceImpl.request(emptyLocation);

        verifyZeroInteractions(yahooRequest);
    }

}

