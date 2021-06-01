package data.models;

public class BaseResponse <T> {

    private boolean isSuccessful;
    private T body;
    private String error;

    public BaseResponse(boolean isSuccessful, T body, String error){
        this.isSuccessful=isSuccessful;
        this.body=body;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public T getBody() {
        return body;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
