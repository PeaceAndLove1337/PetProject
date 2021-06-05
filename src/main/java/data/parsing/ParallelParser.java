package data.parsing;

import BankResponseModels.CurrencyResponseModel;
import data.Bank;
import data.parsing.wrappers.ParsedCurrencyResponseWrapper;
import data.parsing.bankparsers.CurrencyParser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Общий механизм для параллельного парсинга реквестов с банковских API
 */
public class ParallelParser {

    ExecutorService executorService;
    Map<Bank, CurrencyParser> currencyParsers;

    /**
     * @param executorService - сюда нужно положить глобально созданный Executors.newFixedThreadPool(№),
     *                        где № - колво банков, на которые нужно ходить параллельно
     * @param currencyParsers - карта заранее созданных парсеров, которые будут использоваться при парсинге
     */
    public ParallelParser(ExecutorService executorService, Map<Bank, CurrencyParser> currencyParsers) {
        this.executorService = executorService;
        this.currencyParsers = currencyParsers;
    }

    /**
     * Данный метод принимает в себя набор респонсов на парсинг и возвращает обёрнутый список моделей респонсов
     */
    public ParsedCurrencyResponseWrapper parallelCurrencyParse(Map<Bank, String> responsesToParse) {

        ParsedCurrencyResponseWrapper result = new ParsedCurrencyResponseWrapper();
        List<CurrencyResponseModel> inWrapperList = result.getCurrencyResponseModelsList();
        for (Bank key : responsesToParse.keySet()) {
            CompletableFuture.supplyAsync(() ->
                    currencyParsers.get(key)
                            .parseCurrency(responsesToParse.get(key))
                    , executorService)
            .thenAccept(inWrapperList::addAll);
        }



        //executorService.shutdown();

        return result;
    }


  /*  //После переписывания на CompletableFuture данный класс не нужон.
    class CurrencyParserCallable implements Callable<List<? extends CurrencyResponseModel>> {

        CurrencyParser currencyParser;
        String response;

        public CurrencyParserCallable(CurrencyParser currencyParser, String response) {
            this.currencyParser = currencyParser;
            this.response = response;
        }

        @Override
        public List<? extends CurrencyResponseModel> call() {
            return currencyParser.parseCurrency(response);
        }
    }*/

}
