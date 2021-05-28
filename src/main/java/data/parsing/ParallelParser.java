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

      /*  for (Map.Entry<Bank, String> entry : responsesToParse.entrySet()) {
            switch (entry.getKey()) {
                case CB:
                    futures.add(
                            executorService.submit(
                            new CurrencyParserCallable(currencyParsers.get(Bank.CB), entry.getValue())
                            )
                    );
                    break;
                case VTB:
                    futures.add(
                            executorService.submit(
                                    new CurrencyParserCallable(currencyParsers.get(Bank.VTB), entry.getValue())
                            )
                    );
                    break;
                case TINKOFF:
                    futures.add(
                            executorService.submit(
                                    new CurrencyParserCallable(currencyParsers.get(Bank.TINKOFF), entry.getValue())
                            )
                    );
                    break;
                case SBERBANK:
                    futures.add(
                            executorService.submit(
                                    new CurrencyParserCallable(currencyParsers.get(Bank.SBERBANK), entry.getValue())
                            )
                    );
                    break;
                case ALPHABANK:
                    futures.add(
                            executorService.submit(
                                    new CurrencyParserCallable(currencyParsers.get(Bank.ALPHABANK), entry.getValue())
                            )
                    );
                    break;

            }
        }*/

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
