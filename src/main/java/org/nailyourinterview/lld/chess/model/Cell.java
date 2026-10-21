package org.nailyourinterview.lld.chess.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.nailyourinterview.lld.chess.model.pieces.Piece;

@Getter
@Setter
@RequiredArgsConstructor
public class Cell {
    private final int row;      // 0..7 -> rank 1..8
    private final int col;      // 0..7 -> file a..h
    private Piece piece;

    public boolean isEmpty() {
        return piece == null;
    }

    public String getName() {
        return "" + (char) ('a' + col) + (row + 1);   // e.g. "e2"
    }
}
