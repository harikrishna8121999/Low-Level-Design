package org.nailyourinterview.lld.chess.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nailyourinterview.lld.chess.enums.Color;

@Getter
@RequiredArgsConstructor
public class Player {
    private final String name;
    private final Color color;
}
