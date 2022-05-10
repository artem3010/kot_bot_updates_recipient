package com.kotbot.kot_bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@Data
@NoArgsConstructor
public class Updates {
    private boolean ok;
    private Update[] result;

}
