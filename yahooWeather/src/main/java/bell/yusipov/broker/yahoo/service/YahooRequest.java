package bell.yusipov.broker.yahoo.service;

import bell.usipov.broker.dtomodule.model.DtoWeather;

/**
 * Сервис запроса в YAHOO
 */
public interface YahooRequest {

    /**
     * Отправка запроса в YAHOO и получение погоды
     *
     * @param location - название города
     * @return - Объект с погодой
     */
    DtoWeather requestToYahoo(String location);


    /**
     * Конвектирование json строки в java-объект
     * @param jsonStr
     * @return -java-объект
     */
   // DtoWeather jsonToObject(String jsonStr);

}
