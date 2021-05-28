package data.parsing.bankparsers;

import BankResponseModels.AlphabankResponseModel;
import BankResponseModels.CurrencyResponseModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Bank;
import org.json.XML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlphabankParsing implements CurrencyParser {

    public List<? extends CurrencyResponseModel> parseCurrency(String response) {
        List<AlphabankResponseModel> resultArray = new ArrayList<>();
        String date = response.substring(
                response.indexOf("<lastBuildDate>") + 15,
                response.indexOf("</lastBuildDate>")
        );

        String descriptionOfResponse = response.substring(
                response.indexOf("<![CDATA[<?xml version=\"1.0\" encoding=\"WINDOWS-1251\"?>") + 54,
                response.indexOf("]]></description>")
        );

        String responseInJson = XML.toJSONObject(descriptionOfResponse).toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = null;
        try {
            tree = objectMapper.readTree(responseInJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode mainNode = tree.get("div").get("div").get(0).get("table").get("tr");

        Iterator<JsonNode> iterator = mainNode.elements();
        iterator.next(); // Нулевой элемент нас не интересует

        while (iterator.hasNext()) {
            JsonNode currNode = iterator.next().get("td");
            resultArray.add(
                    new AlphabankResponseModel(
                            Bank.ALPHABANK,
                            date,
                            currNode.get(1).get("title").asText(),
                            currNode.get(1).get("content").asDouble(),
                            currNode.get(2).get("content").asDouble()
                    )
            );
        }

        return resultArray;
    }
}