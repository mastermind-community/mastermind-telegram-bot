package org.esadev.mastermindhelper.service;


import org.esadev.mastermindhelper.dto.LeaderInfoDto;

import java.util.List;

public interface MessageFormatService {
    String formatLeaderInfo(List<LeaderInfoDto> info);

}
