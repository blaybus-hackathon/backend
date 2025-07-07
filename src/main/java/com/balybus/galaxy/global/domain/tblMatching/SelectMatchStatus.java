package com.balybus.galaxy.global.domain.tblMatching;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.balybus.galaxy.global.domain.tblMatching.MatchState.*;

@Getter
@AllArgsConstructor
public enum SelectMatchStatus {
    ONLY_SELECT_INIT(Collections.singletonList(INIT)),
    ONLY_SELECT_MATCHING(Arrays.asList(PERMIT_TUNE, MATCH_REQUEST, REJECT, INIT)),
    ONLY_SELECT_MATCH_FIN(Arrays.asList(MATCH_FIN, PERMIT_TUNE, MATCH_REQUEST, REJECT, INIT));

    private final List<MatchState> includedStates;
}
