package bell.yusipov.broker.yahoo.service;

import bell.usipov.broker.dtomodule.model.DtoWeather;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YahooRequestTest {

    private YahooRequest yahooRequeste = new YahooRequestImpl();

    @Before
    public void checkNull() {
        Assert.assertNotNull(yahooRequeste);
    }

    /**
     * Тестирование отправки запроса
     */
    @Test
    public void createRequestTest() {

        String location = "Moscow";

        DtoWeather response = yahooRequeste.requestToYahoo(location);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.getLocation()!=null);
        Assert.assertTrue(response.getCurrentObservation()!=null);
        Assert.assertTrue(response.getForecasts()!=null);

    }

    /**
     * Тестирование игнорирования пустой строки
     */
   @Test
    public void ignoreEmptyString() {
        String location = "";

        DtoWeather response = yahooRequeste.requestToYahoo(location);

        assertEquals(response, new DtoWeather());
    }

    /**
     * Тестирование при null параметре
     */
   @Test
    public void ignoreNullString() {
        String location = null;

        DtoWeather response = yahooRequeste.requestToYahoo(location);

        assertEquals(response, new DtoWeather());
    }
}
