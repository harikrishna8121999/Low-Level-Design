package org.nailyourinterview.lld.chess.model.pieces;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

public class King extends Piece {
    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    protected boolean isValidMove(Board board, Cell from, Cell to) {
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());
        return dRow <= 1 && dCol <= 1;   // one step in any direction
    }

    @Override
    public char getLetter() {
        return 'K';
    }
}
