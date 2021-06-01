package data.net.bankrequests;

import data.Bank;
import data.models.BaseResponse;
import data.net.core.LightRequest;
import kotlin.Pair;

import java.io.IOException;
import java.util.Locale;

public class AlphabankRequests implements CurrencyRequester {

    String alphabankCurrencyApiURL = "https://alfabank.ru/_/rss/_currency.html";

    @Override
    public BaseResponse<Pair<Bank, String>> getCurrencyResponse(String... params) {
        LightRequest request = LightRequest
                .getBuilder()
                .addUrl(alphabankCurrencyApiURL)
                .build();

        //todo Проблема множественного повторения кода...
        // данный код дублируется во всех классах ___Requests
        // Нужно изящное решение...
        // Скорее всего смотреть в сторону наследования
        try {
            String response = request.getResponse();
            String responseInLower = response.toLowerCase(Locale.ROOT);
            if (!(responseInLower.contains("error") ||
                    responseInLower.contains("reject"))) {
                return new BaseResponse<Pair<Bank, String>>(true,
                        new Pair(Bank.ALPHABANK, response),
                        null);
            } else {
                return new BaseResponse<>(false,
                        null,
                        response);
            }
        } catch (IOException e) {
            return new BaseResponse<>(false,
                    null,
                    e.getMessage());
        }

    }
}
