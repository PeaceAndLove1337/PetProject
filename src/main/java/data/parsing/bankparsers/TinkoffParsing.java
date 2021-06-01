package data.parsing.bankparsers;

import BankResponseModels.CurrencyResponseModel;
import BankResponseModels.TinkoffResponseCurrencyModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TinkoffParsing implements CurrencyParser {

    public List<? extends CurrencyResponseModel> parseCurrency(String response) {
        List<TinkoffResponseCurrencyModel> resultList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = null;
        try {
            tree = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode mainNode = tree.get("payload");
        JsonNode ratesNode = mainNode.get("rates");

        Iterator<JsonNode> iterator = ratesNode.elements();
        ArrayList<JsonNode> debitCardTransfersNodes = new ArrayList<>(); // Берем только цены для владельцев дебетовых карт

        while (iterator.hasNext()) {
            JsonNode currentNode = iterator.next();
            if (currentNode.get("category").asText().equals("DebitCardsTransfers"))
                debitCardTransfersNodes.add(currentNode);
        }
        ArrayList<JsonNode> nodesWithRUB = debitCardTransfersNodes
                .stream().filter(it -> it.get("toCurrency").get("name").toString().equals("\"RUB\""))
                .collect(Collectors.toCollection(ArrayList::new));

        JsonNode lastUpdateNode = mainNode.get("lastUpdate");
        String lastUpdate = lastUpdateNode.get("milliseconds").asText();

        //Почему то иногда падает NPE ??????
        // Падает потому что почему-то иногда в респонс не прилетает Buy/cost... у некоторых валют..

        nodesWithRUB.forEach(it -> resultList.add(
                new TinkoffResponseCurrencyModel(
                        Bank.TINKOFF,
                        lastUpdate,
                        it.get("fromCurrency").get("name").asText(),
                        it.get("buy").asDouble(),
                        it.get("sell").asDouble()
                )
        ));


        return  resultList;
    }

}
