package data.parsing.bankparsers;

import BankResponseModels.CbResponseModel;
import BankResponseModels.CurrencyResponseModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Bank;
import org.json.XML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CbParsing implements CurrencyParser {

    public List<? extends CurrencyResponseModel> parseCurrency(String response) {
        String responseInJson = XML.toJSONObject(response).toString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode tree = null;
        try {
            tree = objectMapper.readTree(responseInJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode mainNode = tree.get("ValCurs");
        String date = mainNode.get("Date").asText();
        JsonNode valuteNodes = mainNode.get("Valute");
        List<CbResponseModel> resultArray = new ArrayList<>();

        Iterator<JsonNode> iterator = valuteNodes.elements();
        while (iterator.hasNext()) {
            JsonNode currentNode = iterator.next();
            resultArray.add(
                    new CbResponseModel(
                            Bank.CB,
                            date,
                            currentNode.get("CharCode").asText(),
                            currentNode.get("Nominal").asInt(),
                            currentNode.get("Name").asText(),
                            Double.parseDouble(currentNode.get("Value").asText().replace(",", "."))
                    )
            );
        }
        return resultArray;
    }
}
