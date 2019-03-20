package bell.yusipov.broker.yahoo.service;


import bell.usipov.broker.dtomodule.model.DtoWeather;
import bell.yusipov.broker.yahoo.jms.WeatherSender;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

/**
 * {@inheritDoc}
 */
@ApplicationScoped
public class YahooServiceImpl implements YahooService {

    private final YahooRequest yahooRequest;
    private final WeatherSender weatherSender;

    @Inject
    public YahooServiceImpl(YahooRequest yahooRequest, WeatherSender weatherSender) {
        this.yahooRequest = yahooRequest;
        this.weatherSender = weatherSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void request(String location) {

        if (location == null || location.isEmpty()) {
            return;
        }
        DtoWeather weather = yahooRequest.requestToYahoo(location);
        sendWeather(weather);

    }

    /**
     * Отправка погоды в dbModule
     */
    private void sendWeather(DtoWeather weather) {
        if (weather != null) {
            weatherSender.send(weather);
        }
    }

}

