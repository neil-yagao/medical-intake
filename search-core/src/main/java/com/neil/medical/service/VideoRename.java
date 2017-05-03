package com.neil.medical.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.transformer.FileToByteArrayTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by nhu on 4/23/2017.
 */
@Component
@EnableIntegrationManagement(defaultLoggingEnabled = "true")
public class VideoRename {

    private Logger LOGGER = LoggerFactory.getLogger(VideoRename.class);

    @Value("${file.location}")
    private String directoryToWatch;

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "60000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(directoryToWatch + "/img/head"));
        CompositeFileListFilter compositeFileListFilter = new CompositeFileListFilter();
        compositeFileListFilter.addFilter(new AcceptOnceFileListFilter());
        compositeFileListFilter.addFilter(new SimplePatternFileListFilter("*.webm"));
        source.setFilter(compositeFileListFilter);
        return source;
    }


    @Bean
    @Transformer(inputChannel = "fileInputChannel", outputChannel = "processFileChannel")
    public FileToByteArrayTransformer fileToStringTransformer() {
        return new FileToByteArrayTransformer();
    }

    @Bean
    public MessageChannel processFileChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "processFileChannel")
    public MessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(directoryToWatch + "/video"));
        handler.setDeleteSourceFiles(true);
        handler.setExpectReply(false);
        handler.setFileNameGenerator(new FileNameGenerator() {
            @Override
            public String generateFileName(Message<?> message) {
                LocalDateTime datetime = LocalDateTime.now();
                return datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd(HH-mm-ss)")) + ".webm";
            }
        });
        return handler;
    }


}
