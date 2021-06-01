package data.net.bankrequests;

import data.Bank;
import data.models.BaseResponse;
import kotlin.Pair;

public interface CurrencyRequester {
    BaseResponse<Pair<Bank, String>> getCurrencyResponse(String... params);
}

