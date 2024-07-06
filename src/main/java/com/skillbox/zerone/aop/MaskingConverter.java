package com.skillbox.zerone.aop;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;


@Plugin(name = "MaskingConverter", category = "Converter")
@ConverterKeys({"mask"})
public class MaskingConverter extends LogEventPatternConverter {

    private final PatternLayout patternLayout;

    protected MaskingConverter(String[] options) {
        super("mask", "mask");
        this.patternLayout = createPatternLayout(options);
    }

    public static MaskingConverter newInstance(String[] options) {
        return new MaskingConverter(options);
    }

    private PatternLayout createPatternLayout(String[] options) {
        if (options == null || options.length == 0) {
            throw new IllegalArgumentException("Options for MaskingConverter are missing.");
        }
        return PatternLayout.newBuilder().withPattern(options[0]).build();
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        String formattedMessage = patternLayout.toSerializable(event);
        String maskedMessage = maskSensitiveValues(formattedMessage);
        toAppendTo.setLength(0);
        toAppendTo.append(maskedMessage);
    }

    private String maskSensitiveValues(String message) {
        message = message.replaceAll("(?i)password=\\s*[A-z0-9@$!%*?.;]+", "password=****");
        message = message.replaceAll("(?i)email=\\s*[A-z0-9@$!%*?.;]+", "email=****");
        message = message.replaceAll("(?i)firstname=\\s*[A-z0-9@$!%*?.;]+", "firstname=****");
        message = message.replaceAll("(?i)lastname=\\s*[A-z0-9@$!%*?.;]+", "lastname=****");
        return message;
    }
}