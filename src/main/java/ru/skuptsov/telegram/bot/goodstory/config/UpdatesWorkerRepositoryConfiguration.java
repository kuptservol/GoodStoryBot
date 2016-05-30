package ru.skuptsov.telegram.bot.goodstory.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 30/05/2016
 */
@Configuration
@Getter
public class UpdatesWorkerRepositoryConfiguration {

    @Value("${updates.worker.repository.queue.size:50}")
    private Integer queueSize;

    @Value("${updates.worker.repository.queue.block:true}")
    private Boolean block;
}
