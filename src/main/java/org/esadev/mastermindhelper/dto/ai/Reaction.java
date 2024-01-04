package org.esadev.mastermindhelper.dto.ai;

import java.util.List;

public enum Reaction {
    FIRE, PILE_OF_POOP;

    public static final List<Reaction> SUCCESS_REACTIONS = List.of(FIRE);
    public static final List<Reaction> FAIL_REACTIONS = List.of(PILE_OF_POOP);
}
