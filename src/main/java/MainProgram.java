import BankResponseModels.CurrencyResponseModel;
import data.Bank;
import data.net.ParallelApiMapper;
import data.net.bankrequests.*;
import data.parsing.ParallelParser;
import data.parsing.bankparsers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainProgram {

    public static void main(String[] args) {

        //CbRequests cbRequests = new CbRequests();
        //        Pair<Bank, String> req = cbRequests.getCurrencyResponse("27/05/2021");

        Map<CurrencyRequester, String[]> currencyRequests = new HashMap<CurrencyRequester, String[]>() {{
            put(new SberbankRequests(), new String[]{"840"});
            put(new CbRequests(), new String[]{"27/05/2021"});
            put(new AlphabankRequests(), new String[]{});
            put(new VtbRequests(), new String[]{});
            put(new TinkoffRequests(), new String[]{});
        }};

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ParallelApiMapper parallelApiMapper = new ParallelApiMapper(executorService);

        Map<Bank, String> resultOfRequesting = parallelApiMapper.takeParallelCurrencyResponses(currencyRequests);

        Map<Bank, CurrencyParser> currencyParsers = new HashMap<Bank, CurrencyParser>() {{
            put(Bank.ALPHABANK, new AlphabankParsing());
            put(Bank.CB, new CbParsing());
            put(Bank.SBERBANK, new SberbankParsing());
            put(Bank.TINKOFF, new TinkoffParsing());
            put(Bank.VTB, new VtbParsing());
        }};

        ParallelParser parallelParser = new ParallelParser(executorService, currencyParsers);
        List<CurrencyResponseModel> models = parallelParser.parallelCurrencyParse(resultOfRequesting);

        // todo касаемо механизмов паралл. похода в сеть и парсинга:
        //  Сами по себе реквест и парсинг при данной реализации - задачи синхронизированные и последовательные
        //  Проблемы существующей реализации: 1) Использование метода .get() у future, который блокироует вызывающий поток
        //  , из-за чего задача получения результатов становится синхронной
        //  2) Возможно имеет смысл вытащить future в return методов
        //  Ближайшей задачей необходимо реализовать механизм кеширования с временем( как давно это кеширование было произведено...)
        //  т.е. время жизни кеша и отсюда необходимо реализовать механизм который выбирает откуда тянуть данные (с сети либо с кэша)


        System.out.println("Hello world!");

    }
}

