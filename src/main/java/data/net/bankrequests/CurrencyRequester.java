package data.net.bankrequests;

import data.Bank;
import data.net.core.BaseResponse;
import kotlin.Pair;

/**
 * Интерфейс похода в сеть за валютами того или иного банка
 */
public interface CurrencyRequester {
    BaseResponse<Pair<Bank, String>> getCurrencyResponse(String... params);
}

