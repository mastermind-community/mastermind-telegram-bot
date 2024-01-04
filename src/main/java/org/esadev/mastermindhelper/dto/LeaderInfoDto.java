package org.esadev.mastermindhelper.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderInfoDto {
    private int position;
    private String name;
    private String percent;
}
