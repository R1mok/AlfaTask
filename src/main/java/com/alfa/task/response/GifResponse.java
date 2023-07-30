package com.alfa.task.response;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Ответ по запросу получения gif
 */
@Component
public class GifResponse {
    @Getter
    @Setter
    private JsonObject data;

    /**
     * Получение gif изображения из данных
     * @return  url адреса gif изображения
     */
    public String getOriginalImage() {
        return data.get("images").getAsJsonObject().get("preview_webp").getAsJsonObject().get("url").getAsString();
    }
}
