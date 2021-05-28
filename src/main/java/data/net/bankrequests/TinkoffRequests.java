package data.net.bankrequests;

import data.Bank;
import data.net.core.LightRequest;
import kotlin.Pair;

public class TinkoffRequests implements CurrencyRequester {

    String tinkoffCurrencyApiURL = "https://www.tinkoff.ru/api/v1/currency_rates/";

    @Override
    public Pair<Bank, String> getCurrencyResponse(String... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addUrl(tinkoffCurrencyApiURL)
                .build();

        return new Pair(Bank.TINKOFF, request.getResponse());
    }
}
