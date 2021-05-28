package data.net;


import data.Bank;
import data.net.bankrequests.CurrencyRequester;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * Общий механизм для параллельных походов на банковские API. Паттерн "Декоратор"
 */
public class ParallelApiMapper {

    ExecutorService executorService;

    /**
     * @param executorService - сюда нужно положить глобально созданный Executors.newFixedThreadPool(№),
     *                        где № - колво банков, на которые нужно ходить параллельно
     */
    public ParallelApiMapper(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Данный метод принимает в себя набор объектов, делающих запросы к API банков, реализующих интерфейс CurrencyRequest
     */
    public Map<Bank, String> takeParallelCurrencyResponses(Map<CurrencyRequester, String[]> currencyRequestsWithParams) {// String[] - параметры запроса...
        Map<Bank, String> result = new HashMap<>();
        List<Future<Pair<Bank, String>>> futures = new ArrayList<>();
        for(Map.Entry<CurrencyRequester, String[]> entry : currencyRequestsWithParams.entrySet()) {
            futures.add(executorService.submit(new CurrencyRequesterCallable(entry.getKey(), entry.getValue())));
        }

        //todo Решить проблему get'a, ибо код становится синхронным при его вызове... (юзать isDone/cancelled?)
        // Что будем делать если одна из апишек крашнется?
        futures.forEach(
                it -> {
                    try {
                        Pair<Bank, String> currentElem = it.get(2000, TimeUnit.MILLISECONDS);
                        result.put(currentElem.component1(), currentElem.component2());
                    } catch (InterruptedException | TimeoutException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
        );

        //executorService.shutdown();

        return result;
    }

}

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

}
