package data.net.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

//todo при проектировании многопоточных походов в сеть нужно будет использовать тредпулы(колво которых будет
// равно колчичеству юзаемых API (заметим, что в сеть нужно ходить многопоточно по всем апишкам сразу)

/**
 * Общий механизм для походов в сеть. Паттерн "Builder"
 */
public class LightRequest {

    private final URL url;
    private final String encoding;

    private LightRequest(URL url, String encoding) {
        this.url = url;
        this.encoding = encoding;
    }

    /**
     *  Делает запрос по сформированному URL'у и вовзвращает ответ в виде строки
     *  Под капотом использует java.net и java.io (для преобразования ответа в строку)
     */
    public String getResponse() {
        String result = null;
        try {
            URLConnection uRLConnection = url.openConnection();
            InputStream inputStream = uRLConnection.getInputStream();
            result = new BufferedReader(new InputStreamReader(inputStream, encoding))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static Builder getBuilder() {
        return new Builder();
    }


    public static class Builder {
        URL iURL;

        String encoding;

        public Builder() {
            encoding = Charset.defaultCharset().name();
        }

        /**
         * Добавить URL к запросу (Запрос невозможен без URL'a)
         * Если имеются какие-либо параметры к запросу, то URL  должен иметь следующий вид:
         * все подстроки URL'a в которых указываются параметры должны быть заменены строкой &!KPARAM!,
         * где K- номер параметра; затем при формировании запроса необходимо использовать метод
         * addParams, с порядком параметров соответствующим номеру K
         * */
        public Builder addUrl(String url) {
            try {
                iURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return this;
        }


        /**
         * Добавляет кодировку с которой возвращается ответ
         */
        public Builder addEncoding(String charsetName) {
            encoding = charsetName;
            return this;
        }


        /**
         * Добавить параметры к запросу
         */
        public Builder addParams(String ... params) {
            if (iURL!=null){
                String urlInString = iURL.toString();
                for (int i=1;i<params.length+1; i++){
                    urlInString=urlInString.replace("&!"+i+"PARAM!",params[i-1]);
                }
                try {
                    iURL=new URL(urlInString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        public LightRequest build() {
            return new LightRequest(iURL, encoding);
        }

    }
}
