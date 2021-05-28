package data.net.bankrequests;

import data.Bank;
import data.net.core.LightRequest;
import kotlin.Pair;

public class AlphabankRequests implements CurrencyRequester {

    String alphabankCurrencyApiURL = "https://alfabank.ru/_/rss/_currency.html";

    @Override
    public Pair<Bank, String> getCurrencyResponse(String... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addUrl(alphabankCurrencyApiURL)
                .build();
        return new Pair(Bank.ALPHABANK, request.getResponse());
    }
}
