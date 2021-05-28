package data.parsing.bankparsers;

import BankResponseModels.CurrencyResponseModel;

import java.util.List;

public interface CurrencyParser {

    List<? extends CurrencyResponseModel> parseCurrency(String response);

}
