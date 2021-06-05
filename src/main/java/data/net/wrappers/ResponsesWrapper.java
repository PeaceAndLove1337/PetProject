package data.net.wrappers;

import data.Bank;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponsesWrapper {
    private final Map<Bank, String> responses;

    public ResponsesWrapper(){
        responses = new ConcurrentHashMap<>();
    }

    public Map<Bank, String> getResponses() {
        return responses;
    }
}
