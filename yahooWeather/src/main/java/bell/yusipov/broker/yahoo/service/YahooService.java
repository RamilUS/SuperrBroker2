package bell.yusipov.broker.yahoo.service;


/**
 * Сервис модуля
 */
public interface YahooService {

    /**
     * Построение запроса в YAHOO
     *
     * @param location - название города
     */
    void request(String location);

}
