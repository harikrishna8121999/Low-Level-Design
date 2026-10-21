package org.nailyourinterview.lld.chess.model;

import lombok.Getter;
import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.factory.PieceFactory;
import org.nailyourinterview.lld.chess.model.pieces.Piece;

import static org.nailyourinterview.lld.chess.enums.PieceType.*;

@Getter
public class Board {
    public static final int SIZE = 8;
    private static final PieceType[] BACK_ROW = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};

    private final Cell[][] grid = new Cell[SIZE][SIZE];

    public Board() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }
        setupPieces();
    }

    private void setupPieces() {
        for (int col = 0; col < SIZE; col++) {
            grid[0][col].setPiece(PieceFactory.createPiece(BACK_ROW[col], Color.WHITE));
            grid[1][col].setPiece(PieceFactory.createPiece(PAWN, Color.WHITE));
            grid[6][col].setPiece(PieceFactory.createPiece(PAWN, Color.BLACK));
            grid[7][col].setPiece(PieceFactory.createPiece(BACK_ROW[col], Color.BLACK));
        }
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return null;
        }
        return grid[row][col];
    }

    // "e2" -> grid[1][4]. Returns null for bad input.
    public Cell getCell(String name) {
        if (name == null || name.length() != 2) {
            return null;
        }
        int col = name.charAt(0) - 'a';
        int row = name.charAt(1) - '1';
        return getCell(row, col);
    }

    // Walk one step at a time from 'from' towards 'to'. Every cell in between must be empty.
    // Works for straight lines and diagonals (rook, bishop, queen, pawn double step).
    public boolean isPathClear(Cell from, Cell to) {
        int stepRow = Integer.signum(to.getRow() - from.getRow());
        int stepCol = Integer.signum(to.getCol() - from.getCol());
        int row = from.getRow() + stepRow;
        int col = from.getCol() + stepCol;

        while (row != to.getRow() || col != to.getCol()) {
            if (!grid[row][col].isEmpty()) {
                return false;
            }
            row += stepRow;
            col += stepCol;
        }
        return true;
    }

    public Cell findKing(Color color) {
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                Piece piece = cell.getPiece();
                if (piece != null && piece.getType() == KING && piece.getColor() == color) {
                    return cell;
                }
            }
        }
        throw new IllegalStateException("King not found for " + color);
    }

    // Can any piece of the 'attacker' colour move to this cell?
    public boolean isUnderAttack(Cell target, Color attacker) {
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                Piece piece = cell.getPiece();
                if (piece != null && piece.getColor() == attacker && piece.canMove(this, cell, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheck(Color color) {
        return isUnderAttack(findKing(color), color.opposite());
    }

    public void printBoard() {
        System.out.println();
        for (int row = SIZE - 1; row >= 0; row--) {
            System.out.print((row + 1) + "  ");
            for (int col = 0; col < SIZE; col++) {
                Piece piece = grid[row][col].getPiece();
                System.out.print((piece == null ? '.' : piece.getSymbol()) + " ");
            }
            System.out.println();
        }
        System.out.println("   a b c d e f g h");
        System.out.println();
    }
}
