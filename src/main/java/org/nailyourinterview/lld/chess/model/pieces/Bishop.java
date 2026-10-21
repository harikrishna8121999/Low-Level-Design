package org.nailyourinterview.lld.chess.model.pieces;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    protected boolean isValidMove(Board board, Cell from, Cell to) {
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());
        return dRow == dCol && board.isPathClear(from, to);
    }

    @Override
    public char getLetter() {
        return 'B';
    }
}
