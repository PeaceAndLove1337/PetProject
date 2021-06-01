package data.wrappers;

import BankResponseModels.CurrencyResponseModel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParsedCurrencyResponseWrapper {

    private final List<CurrencyResponseModel> currencyResponseModelsList;

    public ParsedCurrencyResponseWrapper(){
        currencyResponseModelsList=new CopyOnWriteArrayList<>();
    }

    public List<CurrencyResponseModel> getCurrencyResponseModelsList() {
        return currencyResponseModelsList;
    }
}
