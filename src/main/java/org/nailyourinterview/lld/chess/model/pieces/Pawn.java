package org.nailyourinterview.lld.chess.model.pieces;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    protected boolean isValidMove(Board board, Cell from, Cell to) {
        int dir = getColor() == Color.WHITE ? 1 : -1;        // white moves up, black moves down
        int startRow = getColor() == Color.WHITE ? 1 : 6;
        int dRow = to.getRow() - from.getRow();
        int dCol = Math.abs(to.getCol() - from.getCol());

        // 1 step forward into an empty cell
        if (dCol == 0 && dRow == dir) {
            return to.isEmpty();
        }
        // 2 steps forward from the start row, both cells empty
        if (dCol == 0 && dRow == 2 * dir && from.getRow() == startRow) {
            return to.isEmpty() && board.isPathClear(from, to);
        }
        // 1 step diagonal, only to capture
        return dCol == 1 && dRow == dir && !to.isEmpty();
    }

    @Override
    public char getLetter() {
        return 'P';
    }
}
