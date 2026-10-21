package org.nailyourinterview.lld.chess.factory;

import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.model.pieces.*;

public class PieceFactory {
    public static Piece createPiece(PieceType type, Color color) {
        return switch (type) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case ROOK -> new Rook(color);
            case BISHOP -> new Bishop(color);
            case KNIGHT -> new Knight(color);
            case PAWN -> new Pawn(color);
        };
    }
}
