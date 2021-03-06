package ru.skuptsov.telegram.bot.goodstory.repository;

import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
public interface StoryRepository {

    Story getStoryUnseen(@NotNull StoryQuery storyQuery, long chatId);

    void markStoryAsSeen(long storyId, long chatId);

    void add(Story story);
}
