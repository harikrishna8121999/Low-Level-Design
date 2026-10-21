package org.nailyourinterview.lld.chess.model.pieces;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    protected boolean isValidMove(Board board, Cell from, Cell to) {
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());
        return dRow * dCol == 2;   // L-shape: (1,2) or (2,1). Jumps, so no path check.
    }

    @Override
    public char getLetter() {
        return 'N';
    }
}
