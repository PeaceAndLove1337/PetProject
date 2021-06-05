package data.net.bankrequests;

import data.Bank;
import data.net.core.BaseResponse;
import data.net.core.LightRequest;
import kotlin.Pair;

import java.util.HashMap;

public class SberbankRequests extends BaseResponsibleRequester implements CurrencyRequester {

    String sberbankCurrencyApiURL = "https://www.sberbank.ru/portalserver/proxy/?pipe=" +
            "shortCachePipe&url=http%3A%2F%2Flocalhost%2Frates-web%2FrateService%2Frate%2Fcurrent%3Fregion" +
            "Id%3D77%26rateCategory%3Dbase%26currencyCode%3D&!1PARAM!";

    static HashMap <String, Integer> sberbankCurrencyCodes = new HashMap<String, Integer>() {{
        put("CAD", 124 );
        put("CNY", 156);
        put("CZK", 203);
        put("DKK", 208);
        put("JPY", 392);
        put("KZT", 398);
        put("NOK", 578);
        put("SGD", 702);
        put("SEK", 752);
        put("CHF", 756);
        put("GBP", 826);
        put("USD", 840);
        put("BYN", 933);
        put("EUR", 978);
        put("PLN", 985);
    }};

    /**
     * @param params Параметры в формате "ххх" - цифровой номер валюты, например "840"
     */
    @Override
    public BaseResponse<Pair<Bank, String>> getCurrencyResponse(String ... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addEncoding("utf-8")
                .addUrl(sberbankCurrencyApiURL)
                .addParams(params)
                .build();

        return getCurrencyBaseResponse(request, Bank.SBERBANK);
    }
}
