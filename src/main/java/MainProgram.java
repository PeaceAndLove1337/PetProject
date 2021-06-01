import data.Bank;
import data.net.ParallelRequests;
import data.net.bankrequests.*;
import data.wrappers.ParsedCurrencyResponseWrapper;
import data.wrappers.ResponsesWrapper;
import data.parsing.ParallelParser;
import data.parsing.bankparsers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainProgram {

    public static void main(String[] args) {

        /*todo
        *  1) Обработка ошибок при походе в сеть?!
        *  2) Паттерны проектирования -> поменять название ParallelRequests
        *  3) Кэширование/переход в domain-слой?
        * */
        Map<CurrencyRequester, String[]> currencyRequests = new HashMap<CurrencyRequester, String[]>() {{
            put(new SberbankRequests(), new String[]{"840"});
            put(new CbRequests(), new String[]{"27/05/2021"});
            put(new AlphabankRequests(), new String[]{});
            put(new VtbRequests(), new String[]{});
            put(new TinkoffRequests(), new String[]{});
        }};

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ParallelRequests parallelRequester = new ParallelRequests(executorService);

        ResponsesWrapper responsesWrapper = parallelRequester.takeParallelCurrencyResponses(currencyRequests);


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<Bank, CurrencyParser> currencyParsers = new HashMap<Bank, CurrencyParser>() {{
            put(Bank.ALPHABANK, new AlphabankParsing());
            put(Bank.CB, new CbParsing());
            put(Bank.SBERBANK, new SberbankParsing());
            put(Bank.TINKOFF, new TinkoffParsing());
            put(Bank.VTB, new VtbParsing());
        }};

        ParallelParser parallelParser = new ParallelParser(executorService, currencyParsers);
        ParsedCurrencyResponseWrapper modelsSHT = parallelParser.parallelCurrencyParse(responsesWrapper.getResponses());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello world!");

    }
}

