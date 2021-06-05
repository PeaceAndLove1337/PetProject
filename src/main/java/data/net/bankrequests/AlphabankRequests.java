package data.net.bankrequests;

import data.Bank;
import data.net.core.BaseResponse;
import data.net.core.LightRequest;
import kotlin.Pair;

public class AlphabankRequests extends BaseResponsibleRequester implements CurrencyRequester {

    String alphabankCurrencyApiURL = "https://alfabank.ru/_/rss/_currency.html";

    @Override
    public BaseResponse<Pair<Bank, String>> getCurrencyResponse(String... params) {
        LightRequest request = LightRequest
                .getBuilder()
                .addUrl(alphabankCurrencyApiURL)
                .build();


        return getCurrencyBaseResponse(request, Bank.ALPHABANK);
    }
}
