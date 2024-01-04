package org.esadev.mastermindhelper.dto.telegram;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InlineButton {
    private String text;
    private String callBackData;
    private String url;
}
