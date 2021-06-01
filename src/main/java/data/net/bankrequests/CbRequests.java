package data.net.bankrequests;

import data.Bank;
import data.models.BaseResponse;
import data.net.core.LightRequest;
import kotlin.Pair;

import java.io.IOException;
import java.util.Locale;

public class CbRequests implements CurrencyRequester {
    String cbCurrencyApiURL = "http://cbr.ru/scripts/XML_daily.asp?date_req=&!1PARAM!";


    /**
     * @param params Параметры в формате "23/04/2021" - дата обновления данных
     */
    @Override
    public BaseResponse<Pair<Bank, String>> getCurrencyResponse(String ... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addUrl(cbCurrencyApiURL)
                .addParams(params)
                .build();

        try {
            String response = request.getResponse();
            String responseInLower = response.toLowerCase(Locale.ROOT);
            if (!(responseInLower.contains("error") ||
                    responseInLower.contains("reject"))) {
                return new BaseResponse<Pair<Bank, String>>(true,
                        new Pair(Bank.CB, response),
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
