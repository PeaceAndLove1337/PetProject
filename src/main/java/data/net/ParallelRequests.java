package data.net;


import data.Bank;
import data.net.bankrequests.CurrencyRequester;
import data.net.core.BaseResponse;
import data.net.wrappers.ResponsesWrapper;
import kotlin.Pair;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;


/**
 * Общий механизм для параллельных походов на банковские API. Паттерн "Декоратор"
 */
//todo Переименовать класс в соответствии с юзаемым паттерном (Понять что за паттерн, отталкиваться от того, что
// в конструкторе лежит ExecutorService?)
public class ParallelRequests {

    ExecutorService executorService;

    /**
     * Максимальное количество перезапросов в случае неудачи
     */
    final int maxCountOfAttempts = 3;

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
            CompletableFuture.supplyAsync(() -> {
                        BaseResponse<Pair<Bank, String>> baseResponse = entry.getKey().getCurrencyResponse(entry.getValue());
                        int countOfAttempts = 0;
                        while (!baseResponse.isSuccessful() || countOfAttempts < maxCountOfAttempts) {
                            baseResponse = entry.getKey().getCurrencyResponse(entry.getValue());
                            countOfAttempts++;
                        }
                        return baseResponse;
                    }, executorService
            )
                    .thenAccept(currResult ->
                            {
                                /*
                                 todo Открытый вопрос: что делать с BaseRespons'ом если он остался неуспешным?!
                                  Бросать exception?

                                 */
                                if (currResult.isSuccessful())
                                    responsesInWrapper.put(
                                            currResult.getBody().component1(),
                                            currResult.getBody().component2());
                            }
                    );
        }


        //executorService.shutdown();

        return result;
    }


}
