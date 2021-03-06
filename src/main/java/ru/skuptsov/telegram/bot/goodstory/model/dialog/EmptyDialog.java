package ru.skuptsov.telegram.bot.goodstory.model.dialog;

import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 13/06/2016
 */
public enum EmptyDialog implements Dialog {
    ;

    @Override
    public String getText() {
        return "";
    }

    @Override
    public String getCallbackData() {
        return "";
    }

    @Override
    public void updateStoryQuery(@NotNull StoryQuery storyQuery, @NotNull String callbackData) {

    }
}
