package org.esadev.mastermindhelper.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public record AiProps(String key,String completionUrl) {
}
