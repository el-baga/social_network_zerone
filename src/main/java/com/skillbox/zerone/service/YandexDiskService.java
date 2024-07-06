package com.skillbox.zerone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.zerone.aop.LoggableDebug;
import com.skillbox.zerone.dto.response.GetUploadUrlResponse;
import com.skillbox.zerone.dto.response.YandexDiskErrorResponse;
import com.skillbox.zerone.exception.YandexDiskException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Slf4j
@LoggableDebug
public class YandexDiskService {
    private static final String DISK_RESOURCE_URL = "https://cloud-api.yandex.net/v1/disk/resources/upload";

    private static final String CREATE_FOLDER_URL = "https://cloud-api.yandex.net/v1/disk/resources";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${logging.log-storage.token.yandex}")
    private String apiToken;

    public void upload(String srcFolderPath) {
        String folderPath = createFolder();
        try (Stream<Path> paths = Files.walk(Paths.get(srcFolderPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(p -> uploadFile(p.toFile(), folderPath));
            log.info("Логи были отправлены в удаленное хранилище");
        } catch (IOException | YandexDiskException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    public void uploadFile(File file, String folderPath) {
        URL urlForUpload;
        try {
            urlForUpload = new URL(Objects.requireNonNull(getURL(file, folderPath)));
        } catch (IOException exception) {
            throw new YandexDiskException(exception.getMessage());
        }
        Request uploadRequest = new Request.Builder()
                .url(urlForUpload)
                .put(RequestBody.create(file, null))
                .build();
        try {
            Response uploadResponse = client.newCall(uploadRequest).execute();
            if (!uploadResponse.isSuccessful()) {
                handleError(uploadResponse);
            }
        } catch (IOException exception) {
            throw new YandexDiskException(exception.getMessage());
        }
    }

    private String getURL(File file, String folderPath) {
        Request request = new Request.Builder()
                .url(DISK_RESOURCE_URL + "?path=Logs/" + folderPath + file.getName())
                .addHeader("Authorization", apiToken)
                .get()
                .build();
        GetUploadUrlResponse getUploadUrlResponse = new GetUploadUrlResponse();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                getUploadUrlResponse = objectMapper.readValue(response.body().string(), GetUploadUrlResponse.class);
            } else {
                handleError(response);
            }
        } catch (IOException exception) {
            log.error(exception.getLocalizedMessage());
        }
        return getUploadUrlResponse.getHref();
    }

    private String createFolder() {
        String folderName = getFolderName();
        try {
            Request createFolder = new Request.Builder()
                    .url(CREATE_FOLDER_URL + "?path=Logs/" + folderName)
                    .addHeader("Authorization", apiToken)
                    .put(RequestBody.create(new byte[0], null))
                    .build();
            client.newCall(createFolder).execute().close();
        } catch (IOException ex) {
            throw new YandexDiskException(ex.getMessage());
        }
        return folderName;
    }

    private void handleError(Response response) {
        YandexDiskErrorResponse yandexDiskErrorResponse;
        try {
            assert response.body() != null;
            yandexDiskErrorResponse = objectMapper.readValue(response.body().string(), YandexDiskErrorResponse.class);
        } catch (IOException exception) {
            throw new YandexDiskException("Ошибка при парсинге ответа от Яндекс диск");
        }
        throw new YandexDiskException(yandexDiskErrorResponse.getMessage());
    }

    public String getFolderName() {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dateTimeFormatter) + "/";
    }
}
