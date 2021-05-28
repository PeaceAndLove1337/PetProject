package data.net.bankrequests;

import data.Bank;
import data.net.core.LightRequest;
import kotlin.Pair;

public class CbRequests implements CurrencyRequester {
    String cbCurrencyApiURL = "http://cbr.ru/scripts/XML_daily.asp?date_req=&!1PARAM!";


    /**
     * @param params Параметры в формате "23/04/2021" - дата обновления данных
     */
    @Override
    public Pair<Bank, String> getCurrencyResponse(String ... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addUrl(cbCurrencyApiURL)
                .addParams(params)
                .build();

        return new Pair(Bank.CB, request.getResponse());
    }
}
