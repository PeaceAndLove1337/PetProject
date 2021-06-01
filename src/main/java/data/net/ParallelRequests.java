package data.net;


import data.Bank;
import data.net.bankrequests.CurrencyRequester;
import data.wrappers.ResponsesWrapper;
import kotlin.Pair;

import java.util.Map;
import java.util.concurrent.*;


/**
 * Общий механизм для параллельных походов на банковские API. Паттерн "Декоратор"
 */
//todo Переименовать класс в соответствии с юзаемым паттерном (Понять что за паттерн, отталкиваться от того, что
// в конструкторе лежит ExecutorService?)
public class ParallelRequests {

    ExecutorService executorService;

    //private static final int MAX_RETRIES = 3;
    /**
     * @param executorService - сюда нужно положить глобально созданный Executors.newFixedThreadPool(№),
     *                        где № - колво банков, на которые нужно ходить параллельно
     */
    public ParallelRequests(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Данный метод принимает в себя набор объектов, делающих запросы к API банков, реализующих интерфейс CurrencyRequest
     * и возвращает обёртку, из которой можно получить коллекцию респонсов
     */
    public ResponsesWrapper takeParallelCurrencyResponses(Map<CurrencyRequester, String[]> currencyRequestsWithParams) {// String[] - параметры запроса...
        ResponsesWrapper result = new ResponsesWrapper();

        Map<Bank, String> responsesInWrapper = result.getResponses();
        for (Map.Entry<CurrencyRequester, String[]> entry : currencyRequestsWithParams.entrySet()) {
            CompletableFuture.supplyAsync(() ->
                    entry.getKey().getCurrencyResponse(entry.getValue()), executorService
            )
                    .thenAccept(currResult ->
                            responsesInWrapper.put(currResult.getBody().component1(),
                            currResult.getBody().component2()));
        }

        //executorService.shutdown();

        return result;
    }

  /*
    //todo не очень разумное решение?! Грузим стек...
    private void asyncTakeCurrency(CurrencyRequester currencyRequester, String[] params,
                                   ResponsesWrapper responsesWrapper, Integer attempt){
        CompletableFuture.supplyAsync(() ->
                currencyRequester.getCurrencyResponse(params), executorService
        )
                .thenAccept(currResult -> {
                            if (currResult.isSuccessful())
                                responsesWrapper.getResponses()
                                        .put(currResult.getBody().component1(), currResult.getBody().component2());
                            else if (attempt<MAX_RETRIES){
                                asyncTakeCurrency(currencyRequester, params, responsesWrapper, attempt+1);
                            }
                        }
                );
    }*/

}


/*//После переписывания на CompletableFuture данный класс не нужон.
class CurrencyRequesterCallable implements Callable<Pair<Bank, String>> {

    CurrencyRequester currencyRequester;
    String params[];

    public CurrencyRequesterCallable(CurrencyRequester currencyRequester, String... params) {
        this.currencyRequester = currencyRequester;
        this.params = params;
    }

    @Override
    public Pair<Bank, String> call() {
        if (params == null)
            return currencyRequester.getCurrencyResponse();
        else
            return currencyRequester.getCurrencyResponse(params);
    }

}*/
