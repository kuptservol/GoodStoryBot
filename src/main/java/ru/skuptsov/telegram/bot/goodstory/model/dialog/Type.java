package ru.skuptsov.telegram.bot.goodstory.model.dialog;

import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum Type implements Dialog {
    STORY("рассказ", "type.story"),
    PART("отрывок", "type.part");

    private static final Map<String, Type> valuesMap =
            stream(Type.values())
                    .collect(toMap(Type::getCallbackData, identity()));
    private final String text, callbackData;

    Type(String text, String callbackData) {
        this.text = text;
        this.callbackData = callbackData;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getCallbackData() {
        return callbackData;
    }

    @Override
    public void updateStoryQuery(@NotNull StoryQuery storyQuery, @NotNull String callbackData) {
        ofNullable(valuesMap.get(callbackData))
                .ifPresent(storyQuery::setType);
    }
}
