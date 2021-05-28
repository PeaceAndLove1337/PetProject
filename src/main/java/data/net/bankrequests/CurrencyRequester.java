package data.net.bankrequests;

import data.Bank;
import kotlin.Pair;

public interface CurrencyRequester {
    Pair<Bank, String> getCurrencyResponse(String... params);
}

