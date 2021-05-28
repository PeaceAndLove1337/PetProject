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


        System.out.println("Hello world!");

    }
}

