package com.skillbox.zerone.scheduler;

import com.skillbox.zerone.aop.LoggableDebug;
import com.skillbox.zerone.service.YandexDiskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(name = "logging.remote-store", matchIfMissing = true)
@Slf4j
@LoggableDebug
public class YandexScheduler {

    private final YandexDiskService diskService;

    @Value("${LOG_PATH}")
    private String path;

    @Scheduled(cron = "${scheduler.log-upload}")
    public void uploadLogToYandexDisk() {
        log.info("Вызван планировщик отправки логов в Яндекс Диск!!!");
        String folderName = diskService.getFolderName();
        String folderPath = path + "/archive/" + folderName;
        diskService.upload(folderPath);
    }
}
