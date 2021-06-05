package data.net.core;

/**
 * Класс стандартного респонса, имеющий поле isSuccessful
 * @param <T> Тело респонса
 */
public class BaseResponse <T> {

    private final boolean isSuccessful;
    private final T body;
    private final String error;

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
