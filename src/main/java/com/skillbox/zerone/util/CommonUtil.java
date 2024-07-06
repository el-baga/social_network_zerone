package com.skillbox.zerone.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Component
public class CommonUtil {

    private static HttpServletRequest httpRequest;

    private CommonUtil() {

    }

    @Autowired
    public void initHttpRequest(HttpServletRequest httpRequest){
        CommonUtil.httpRequest = httpRequest;
    }

    public static int offsetToPageNum(@NotNull Integer offset, @NotNull @Min(1) Integer pageSize) {
        return offset / pageSize;
    }

    public static Long getCurrentUserId() {
        return Long.valueOf(httpRequest.getUserPrincipal().getName());
    }

    public static JsonNode getFromWeb(String url) {
        try {
            return new ObjectMapper().readTree(new RestTemplate().getForObject(url, String.class));
        }
        catch (Exception e) {
            log.error("getFromWeb: Wrong json response", e);
        }
        return null;
    }
}
