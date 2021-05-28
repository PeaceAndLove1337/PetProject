package data.parsing.bankparsers;

import BankResponseModels.CurrencyResponseModel;
import BankResponseModels.SberbankResponseCurrencyModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

public class SberbankParsing implements CurrencyParser {

    public List<? extends CurrencyResponseModel> parseCurrency(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = null;
        try {
            tree = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode mainNode = tree.elements().next()
                .elements().next()
                .elements().next();

        String isoCur = null;
        String nameInRussian = null;
        String nameInEnglish = null;
        Double buyCost = null;
        Double sellCost = null;
        String activeFrom = null;
        Iterator<Map.Entry<String, JsonNode>> iterator = mainNode.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> currentNode = iterator.next();
            switch (currentNode.getKey()) {
                case "isoCur":
                    isoCur = currentNode.getValue().asText();
                case "currencyName":
                    nameInRussian = currentNode.getValue().asText();
                case "currencyNameEng":
                    nameInEnglish = currentNode.getValue().asText();
                case "buyValue":
                    buyCost = currentNode.getValue().asDouble();
                case "sellValue":
                    sellCost = currentNode.getValue().asDouble();
                case "activeFrom":
                    activeFrom = currentNode.getValue().asText();
            }
        }

        return Stream.of(new SberbankResponseCurrencyModel(
                Bank.SBERBANK,
                activeFrom,
                isoCur,
                nameInRussian,
                nameInEnglish,
                buyCost,
                sellCost))
                .collect(toCollection(ArrayList::new));
    }
}
