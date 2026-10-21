package org.nailyourinterview.lld.chess.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nailyourinterview.lld.chess.model.pieces.Piece;

@Getter
@RequiredArgsConstructor
public class Move {
    private final Player player;
    private final Cell from;
    private final Cell to;
    private final Piece piece;
    private final Piece captured;   // null if nothing was taken

    @Override
    public String toString() {
        return player.getName() + ": " + piece.getSymbol() + " " + from.getName() + " -> " + to.getName()
                + (captured != null ? " (takes " + captured.getSymbol() + ")" : "");
    }
}
