package data.net.bankrequests;

import data.Bank;
import data.net.core.LightRequest;
import kotlin.Pair;

public class VtbRequests implements CurrencyRequester {
    String vtbCurrencyApiURL = "https://www.vtb.ru/api/currency-exchange/table-info?contextItemId=%7BC5471052-2291-" +
            "4AFD-9C2D-1DBC40A4769D%7D&conversionPlace=0&conversionType=0&renderingId=ede2e4d0-eb6b-4730-" +
            "857b-06fd4975c06b&renderingParams=LegalStatus__%7BF2A32685-E909-44E8-A954-1E206D92FFF8%7D;" +
            "IsFromRuble__1;CardMaxPeriodDays__5;CardRecordsOnPage__5;ConditionsUrl__" +
            "%2Fpersonal%2Fplatezhi-i-perevody%2Fobmen-valjuty%2Fspezkassy%2F;Multiply100JPYand10SEK__1";

    @Override
    public Pair<Bank, String> getCurrencyResponse(String... params) {
        LightRequest request= LightRequest
                .getBuilder()
                .addEncoding("utf-8")
                .addUrl(vtbCurrencyApiURL)
                .build();
        return new Pair(Bank.VTB, request.getResponse());
    }

}
