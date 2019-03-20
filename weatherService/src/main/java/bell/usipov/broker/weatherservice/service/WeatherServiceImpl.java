package bell.usipov.broker.weatherservice.service;

import bell.usipov.broker.weatherservice.view.LocalView;
import bell.usipov.broker.dtomodule.model.DtoLocation;
import bell.usipov.broker.dtomodule.model.DtoWeather;
import bell.usipov.broker.dtomodule.service.GetWeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final GetWeatherData getWeatherData;

    @Autowired
    public WeatherServiceImpl(GetWeatherData getWeatherData) {
        this.getWeatherData = getWeatherData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DtoWeather getWeather(String location) {

        if (location == null || location.isEmpty()) {
            return new DtoWeather();
        }
        return getWeatherData.getWeather(location);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalView> list() {

        List<DtoLocation> dtoList = getWeatherData.list();
        List<LocalView> views = new ArrayList<>();

        for (DtoLocation dto : dtoList) {
            views.add(new LocalView(dto.getCity()));
        }

        return views;
    }

}
