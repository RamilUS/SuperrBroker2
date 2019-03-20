package bell.yusipov.broker.yahoo.service;

import bell.usipov.broker.dtomodule.model.DtoWeather;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * {@inheritDoc}
 */
@ApplicationScoped
public class YahooRequestImpl implements YahooRequest {
    private RestTemplate restTemplate = new RestTemplate();

    /**
     * {@inheritDoc}
     */
    @Override
    public DtoWeather requestToYahoo(String location) {
        if (location == null || location.isEmpty()) {
            return new DtoWeather();
        }


        /**
         * Ключи, полученные  от YAHOO для API:
         * App ID
         * Client ID (Consumer Key)
         * Client Secret (Consumer Secret)
         * url для запроса
         */
        final String appId = "jZ44NU5i";
        final String consumerKey = "dj0yJmk9V01hU0lHWkV6bWpuJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTZj";
        final String consumerSecret = "2cd0f271984f908cb917fed59851ef081604ae45";
        final String url = "https://weather-ydn-yql.media.yahoo.com/forecastrss";

        long timestamp = new Date().getTime() / 1000;
        byte[] nonce = new byte[32];
        Random rand = new Random();
        rand.nextBytes(nonce);
        String oauthNonce = new String(nonce).replaceAll("\\W", "");

        List<String> parameters = createParameters(consumerKey, timestamp, oauthNonce, location);
        StringBuilder parametersList = appendSeparators(parameters);
        String signature = createSignature(consumerSecret, parametersList, url);
        String authorizationLine = createAuthorizationLine(consumerKey, timestamp, oauthNonce, signature);

        HttpEntity<?> entity = createHeaders(appId, authorizationLine);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url + "?location=" + location + "&u=c&format=json",
                        HttpMethod.GET,
                        entity,
                        String.class);
        DtoWeather weather = jsonToObject(responseEntity.getBody());

        return weather;
    }


    /**
     * Формирование заголовков для отправки на Yahoo
     *
     * @param appId
     * @param authorizationLine - линия авторизации
     * @return объект HTTP-запроса с зашифрованными данными
     */
    private HttpEntity<?> createHeaders(String appId, String authorizationLine) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationLine);
        headers.set("Yahoo-App-Id", appId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    /**
     * Создание подписи
     *
     * @param consumerSecret
     * @param parametersList
     * @param url
     * @return строка подписи
     */
    private String createSignature(String consumerSecret, StringBuilder parametersList, String url) {

        String signatureString;
        try {
            signatureString = "GET&" +
                    URLEncoder.encode(url, "UTF-8") + "&" +
                    URLEncoder.encode(parametersList.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding error", e);
        }

        String signature;
        try {
            SecretKeySpec signingKey = new SecretKeySpec((consumerSecret + "&").getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHMAC = mac.doFinal(signatureString.getBytes());
            Base64.Encoder encoder = Base64.getEncoder();
            signature = encoder.encodeToString(rawHMAC);
        } catch (Exception e) {
            throw new RuntimeException("Encoding error", e);
        }
        return signature;
    }

    /**
     * Создание строки авторизации
     *
     * @param consumerKey
     * @param timestamp
     * @param oauthNonce
     * @param signature
     * @return строка авторизации
     */
    private String createAuthorizationLine(String consumerKey, long timestamp, String oauthNonce, String signature) {
        return "OAuth " +
                "oauth_consumer_key=\"" + consumerKey + "\", " +
                "oauth_nonce=\"" + oauthNonce + "\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_signature_method=\"HMAC-SHA1\", " +
                "oauth_signature=\"" + signature + "\", " +
                "oauth_version=\"1.0\"";
    }

    /**
     * Добавление разделителей к списку парамтров
     *
     * @param parameters
     * @return список параметров с раздеителями
     */
    private StringBuilder appendSeparators(List<String> parameters) {
        StringBuilder parametersList = new StringBuilder();
        for (int i = 0; i < parameters.size(); i++) {
            parametersList.append((i > 0) ? "&" : "").append(parameters.get(i));
        }
        return parametersList;
    }

    /**
     * Создание списка параметров
     *
     * @param consumerKey
     * @param timestamp
     * @param oauthNonce
     * @param location
     * @return список параметров
     */
    private List<String> createParameters(String consumerKey, long timestamp, String oauthNonce, String location) {

        List<String> parameters = new ArrayList<>();
        parameters.add("oauth_consumer_key=" + consumerKey);
        parameters.add("oauth_nonce=" + oauthNonce);
        parameters.add("oauth_signature_method=HMAC-SHA1");
        parameters.add("oauth_timestamp=" + timestamp);
        parameters.add("oauth_version=1.0");
        try {
            parameters.add("location=" + URLEncoder.encode(location, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding error", e);
        }
        parameters.add("format=json");
        parameters.add("u=c");
        Collections.sort(parameters);
        return parameters;
    }

    /**
     * Конвертация строки в объект
     * @param jsonStr
     * @return java-объект
     */
    private DtoWeather jsonToObject(String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            return new DtoWeather();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        DtoWeather weather;

        try {
            weather = objectMapper.readValue(jsonStr, DtoWeather.class);
            return weather;
        } catch (IOException e) {
            throw new RuntimeException("Mapper read error", e);
        }
    }
}