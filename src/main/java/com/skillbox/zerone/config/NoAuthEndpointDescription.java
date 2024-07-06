package com.skillbox.zerone.config;

import com.skillbox.zerone.exception.BadRequestRs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
@Operation(
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK"
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = BadRequestRs.class)
                        )
                )
        },
        security = {
                @SecurityRequirement(name = "")
        }
)
public @interface NoAuthEndpointDescription {

    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary();
}
