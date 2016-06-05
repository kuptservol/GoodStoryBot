package ru.skuptsov.telegram.bot.goodstory.client.impl;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import ru.skuptsov.telegram.bot.goodstory.client.NextOffsetStrategy;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.google.common.collect.ImmutableMap.of;
import static ru.skuptsov.telegram.bot.goodstory.client.utils.JavaTypeUtils.listTypeOf;
import static ru.skuptsov.telegram.bot.goodstory.client.utils.JavaTypeUtils.simpleTypeOf;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public class TelegramBotApiImpl implements TelegramBotApi {
    private final Logger log = LoggerFactory.getLogger(TelegramBotApiImpl.class);

    @Autowired
    private NextOffsetStrategy nextOffsetStrategy;

    private final TelegramBotHttpClient client;

    public TelegramBotApiImpl(TelegramBotHttpClient client) {
        this.client = client;
    }

    @Override
    @Timed(name = "bot.api.client.getNextUpdates", absolute = true)
    public List<Update> getNextUpdates(Integer poolingLimit, Integer poolingTimeout) {
        List<Update> updates = new ArrayList<>();

        try {
            Future<List<Update>> futureUpdates = client.executeGet(
                    "getUpdates",
                    of("offset", nextOffsetStrategy.getNextOffset().toString(),
                            "timeout", poolingTimeout.toString(),
                            "limit", poolingLimit.toString()),
                    listTypeOf(Update.class));

            updates = futureUpdates.get();

            nextOffsetStrategy.saveCurrentOffset(updates);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Can't get updates with exception {}", e);
        }

        return updates;
    }

    @Override
    public Future<Message> sendMessage(SendMessage sendMessage) {
        return client.executePost(
                sendMessage.getPath(),
                sendMessage,
                simpleTypeOf(Message.class));
    }
}
