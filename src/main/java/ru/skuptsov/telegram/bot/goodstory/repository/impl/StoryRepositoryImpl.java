package ru.skuptsov.telegram.bot.goodstory.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skuptsov.telegram.bot.goodstory.model.Story;
import ru.skuptsov.telegram.bot.goodstory.model.dialog.Sorting;
import ru.skuptsov.telegram.bot.goodstory.model.query.StoryQuery;
import ru.skuptsov.telegram.bot.goodstory.repository.StoryRepository;

import javax.validation.constraints.NotNull;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
@Repository
public class StoryRepositoryImpl implements StoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Story getStoryUnseen(@NotNull StoryQuery storyQuery, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM story s LEFT JOIN story_seen ss ON ss.story_id = s.id " +
                        "WHERE ss.story_id IS NULL " +
                        "AND ss.user_id = ? " +
                        "AND genre = ? " +
                        "AND length = ? " +
                        "AND type = ? " +
                        "AND language = ? " +
                        "ORDER BY ? DESC ;",
                new Object[]{
                        userId,
                        storyQuery.getGenre().ordinal(),
                        storyQuery.getLength().ordinal(),
                        storyQuery.getType().ordinal(),
                        storyQuery.getLanguage().ordinal(),
                        storyQuery.getSorting()== Sorting.NEWEST ? "added" : "rating"
                },
                (rs, rowNum) -> Story.builder()
                        .id(rs.getLong("id"))
                        .text(rs.getString("text"))
                        .build())
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void markStoryAsSeen(long storyId, int userId) {
        jdbcTemplate.update("INSERT INTO story_seen(user_id,  story_id) VALUES (?,?)", userId, storyId);
    }
}
