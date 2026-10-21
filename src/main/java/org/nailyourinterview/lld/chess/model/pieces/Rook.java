package org.nailyourinterview.lld.chess.model.pieces;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

public class Rook extends Piece {
    public Rook(Color color) {
        super(color, PieceType.ROOK);
    }

    @Override
    protected boolean isValidMove(Board board, Cell from, Cell to) {
        boolean straight = from.getRow() == to.getRow() || from.getCol() == to.getCol();
        return straight && board.isPathClear(from, to);
    }

    @Override
    public char getLetter() {
        return 'R';
    }
}
