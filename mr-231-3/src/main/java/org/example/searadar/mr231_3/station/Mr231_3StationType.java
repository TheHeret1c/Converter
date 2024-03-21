package org.example.searadar.mr231_3.station;

import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.example.searadar.mr231_3.convert.Mr231_3Converter;

import java.nio.charset.Charset;

/**
 * Класс станции
 */
public class Mr231_3StationType {

    /**
     * Статические константы, хранящие тип станции и название кодека
     */
    private static final String STATION_TYPE = "МР-231-3";
    private static final String CODEC_NAME = "mr231-3";

    /**
     * Функция doInitialise создаёт фабрику кодеков текстовых строк
     */
    protected void doInitialize() {
        TextLineCodecFactory textLineCodecFactory = new TextLineCodecFactory(
                Charset.defaultCharset(),
                LineDelimiter.UNIX,
                LineDelimiter.CRLF
        );
    }

    /**
     * Функция для создания конвертера сообщений станции
     * @return - объект конвертера
     */
    public Mr231_3Converter createConverter() {
        return new Mr231_3Converter();
    }
}
