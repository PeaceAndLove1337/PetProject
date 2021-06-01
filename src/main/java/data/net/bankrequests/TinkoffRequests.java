package data.net.bankrequests;

import data.Bank;
import data.models.BaseResponse;
import data.net.core.LightRequest;
import kotlin.Pair;

import java.io.IOException;
import java.util.Locale;

public class TinkoffRequests implements CurrencyRequester {

    String tinkoffCurrencyApiURL = "https://www.tinkoff.ru/api/v1/currency_rates/";

    @Override
    public BaseResponse<Pair<Bank, String>> getCurrencyResponse(String... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addUrl(tinkoffCurrencyApiURL)
                .build();

        try {
            String response = request.getResponse();
            String responseInLower = response.toLowerCase(Locale.ROOT);
            if (!(responseInLower.contains("error") ||
                    responseInLower.contains("reject"))) {
                return new BaseResponse<Pair<Bank, String>>(true,
                        new Pair(Bank.TINKOFF, response),
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
