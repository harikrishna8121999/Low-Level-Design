package org.nailyourinterview.lld.chess.model.pieces;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    protected boolean isValidMove(Board board, Cell from, Cell to) {
        int dRow = Math.abs(to.getRow() - from.getRow());
        int dCol = Math.abs(to.getCol() - from.getCol());
        boolean straight = dRow == 0 || dCol == 0;
        boolean diagonal = dRow == dCol;
        return (straight || diagonal) && board.isPathClear(from, to);   // rook + bishop
    }

    @Override
    public char getLetter() {
        return 'Q';
    }
}
