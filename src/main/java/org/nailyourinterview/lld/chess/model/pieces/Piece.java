package org.nailyourinterview.lld.chess.model.pieces;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;

@Getter
@RequiredArgsConstructor
public abstract class Piece {
    private final Color color;
    private final PieceType type;

    // Rules shared by every piece. Each subclass only adds its own shape of move.
    public boolean canMove(Board board, Cell from, Cell to) {
        if (from == to) {
            return false;
        }
        if (!to.isEmpty() && to.getPiece().getColor() == color) {
            return false;   // cannot capture your own piece
        }
        return isValidMove(board, from, to);
    }

    protected abstract boolean isValidMove(Board board, Cell from, Cell to);

    public abstract char getLetter();

    // White = upper case (K), Black = lower case (k)
    public char getSymbol() {
        return color == Color.WHITE ? getLetter() : Character.toLowerCase(getLetter());
    }
}
