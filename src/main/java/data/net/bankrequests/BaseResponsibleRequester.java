package data.net.bankrequests;

import data.Bank;
import data.net.core.BaseResponse;
import data.net.core.LightRequest;
import kotlin.Pair;

import java.io.IOException;
import java.util.Locale;

/**
 * Данный абстрактный класс создан с целью минимизации дублирования кода в БанкRequests классах
 */
public abstract class BaseResponsibleRequester {


    /**
     *
     * @param request Собранный реквест на валютную API банка
     * @param bank  Банк, для которого делается реквест
     * @return Результат реквеста в обертке {@link BaseResponse }
     */
    public BaseResponse<Pair<Bank, String>> getCurrencyBaseResponse(LightRequest request, Bank bank){

        try {
            String response = request.getResponse();
            String responseInLower = response.toLowerCase(Locale.ROOT);
            //Проверка на содержание в респонсе фраз-маркеров, говорящих о том, что запрос был неудачен
            //todo Возможно, стоит применить другой подход?(Другие слова-маркеры?)
            if (!(responseInLower.contains("error") ||
                    responseInLower.contains("reject"))) {
                return new BaseResponse<Pair<Bank, String>>(true,
                        new Pair(bank, response),
                        null);
            } else {
                return new BaseResponse<>(false,
                        null,
                        response);
            }
        } catch (IOException e) {
            return new BaseResponse<>(false,
                    null,
                    e.getMessage());
        }

    }
}
