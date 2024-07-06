package com.skillbox.zerone.config;

import com.dropbox.core.*;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxConfig {
    @Value("${dropbox.access-token}")
    private String accessToken;

    @Value("${dropbox.refresh-token}")
    private String refreshToken;

    @Value("${dropbox.app-key}")
    private String appKey;

    @Value("${dropbox.secret-key}")
    private String secretKey;

    @Bean
    public DbxClientV2 dbxClientV2() {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("zerone_app").build();
        DbxCredential credentials = new DbxCredential(accessToken, 0L, refreshToken, appKey, secretKey);
        return new DbxClientV2(config, credentials);
    }
}
