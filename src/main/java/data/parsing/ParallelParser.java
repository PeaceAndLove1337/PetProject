package data.parsing;

import BankResponseModels.CurrencyResponseModel;
import data.Bank;
import data.parsing.bankparsers.CurrencyParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ParallelParser {

    ExecutorService executorService;
    Map<Bank, CurrencyParser> currencyParsers;

    //TODO решить вопрос с currencyParsers в конструкторе, т.к. ParallelParser по сути своей механизм общего назначения,
    // а в него кладется конкретная сущность
    public ParallelParser(ExecutorService executorService, Map<Bank, CurrencyParser> currencyParsers) {
        this.executorService = executorService;
        this.currencyParsers = currencyParsers;
    }

    public List<CurrencyResponseModel> parallelCurrencyParse(Map<Bank, String> responsesToParse) {
        List<CurrencyResponseModel> resultList = new ArrayList<>();

        List<Future<List<? extends CurrencyResponseModel>>> futures = new ArrayList<>();

        for (Bank key : responsesToParse.keySet()) {
            futures.add(executorService.submit(new CurrencyParserCallable(currencyParsers.get(key),
                    responsesToParse.get(key))));
        }

        //todo анно решить задачу get'a
        futures.forEach(
                it -> {
                    try {
                        List<? extends CurrencyResponseModel> current=  it.get(2000, TimeUnit.MILLISECONDS);
                        resultList.addAll(current);
                    } catch (InterruptedException | TimeoutException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );

        //executorService.shutdown();

        return resultList;
    }

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
    }

}
