package data.parsing.bankparsers;

import BankResponseModels.CurrencyResponseModel;
import BankResponseModels.VtbResponseModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VtbParsing implements CurrencyParser {

    public List<? extends CurrencyResponseModel> parseCurrency(String response) {
        List<VtbResponseModel> resultList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = null;
        try {
            tree = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode mainNode = tree.get("GroupedRates");

        ArrayList<JsonNode> filteredNodes = new ArrayList<>();

        Iterator<JsonNode> iterator = mainNode.elements();
        while (iterator.hasNext()) {
            JsonNode currNode = iterator.next();
            filteredNodes.add(currNode.get("MoneyRates").get(0));
        }

        filteredNodes.forEach(it -> {
                    String description = it.get("NodeDescription").asText();
                    String date = it.get("StartDate").asText();
                    JsonNode fromCurrencyNode = it.get("FromCurrency");
                    String isoCur = fromCurrencyNode.get("Code").asText();
                    String fullNameInRussian = fromCurrencyNode.get("Name").asText();
                    Double buyCost = it.get("BankBuyAt").asDouble();
                    Double sellCost = it.get("BankSellAt").asDouble();
                    resultList.add(
                            new VtbResponseModel(
                                    Bank.VTB,
                                    date,
                                    description, isoCur, fullNameInRussian, buyCost, sellCost
                            )
                    );
                }
        );

        return resultList;
    }
}
