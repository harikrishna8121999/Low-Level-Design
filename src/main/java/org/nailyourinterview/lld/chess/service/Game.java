package org.nailyourinterview.lld.chess.service;

import lombok.Getter;
import org.nailyourinterview.lld.chess.enums.Color;
import org.nailyourinterview.lld.chess.enums.GameStatus;
import org.nailyourinterview.lld.chess.enums.PieceType;
import org.nailyourinterview.lld.chess.factory.PieceFactory;
import org.nailyourinterview.lld.chess.model.Board;
import org.nailyourinterview.lld.chess.model.Cell;
import org.nailyourinterview.lld.chess.model.Move;
import org.nailyourinterview.lld.chess.model.Player;
import org.nailyourinterview.lld.chess.model.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game {
    private final Board board;
    private final Player[] players;
    private final List<Move> moves;
    private int turn;                 // index into players: 0 = white, 1 = black
    private GameStatus status;

    public Game(Player white, Player black) {
        this.board = new Board();
        this.players = new Player[]{white, black};
        this.moves = new ArrayList<>();
        this.turn = 0;
        this.status = GameStatus.ACTIVE;
    }

    public Player getCurrentPlayer() {
        return players[turn];
    }

    public boolean makeMove(String fromName, String toName) {
        Player player = getCurrentPlayer();
        Cell from = board.getCell(fromName);
        Cell to = board.getCell(toName);

        // 1. validate
        String error = validate(player, from, to);
        if (error != null) {
            System.out.println("Invalid move: " + error);
            return false;
        }

        // 2. apply
        Piece piece = from.getPiece();
        Piece captured = to.getPiece();
        to.setPiece(piece);
        from.setPiece(null);

        Move move = new Move(player, from, to, piece, captured);
        moves.add(move);
        System.out.println(move);
        promoteIfNeeded(to);

        // 3. check the opponent, then pass the turn
        updateStatus(player.getColor().opposite());
        turn = 1 - turn;
        return true;
    }

    private String validate(Player player, Cell from, Cell to) {
        if (status != GameStatus.ACTIVE) {
            return "game is over";
        }
        if (from == null || to == null) {
            return "use squares like e2 e4";
        }
        Piece piece = from.getPiece();
        if (piece == null) {
            return "no piece on " + from.getName();
        }
        if (piece.getColor() != player.getColor()) {
            return "that is not your piece";
        }
        if (!piece.canMove(board, from, to)) {
            return piece.getType() + " cannot move " + from.getName() + " -> " + to.getName();
        }
        if (leavesKingInCheck(from, to)) {
            return "your king would be in check";
        }
        return null;
    }

    // Try the move, look at the king, then undo it.
    private boolean leavesKingInCheck(Cell from, Cell to) {
        Piece piece = from.getPiece();
        Piece captured = to.getPiece();
        to.setPiece(piece);
        from.setPiece(null);

        boolean inCheck = board.isInCheck(piece.getColor());

        from.setPiece(piece);
        to.setPiece(captured);
        return inCheck;
    }

    // A pawn on the last row becomes a queen.
    private void promoteIfNeeded(Cell cell) {
        Piece piece = cell.getPiece();
        int lastRow = piece.getColor() == Color.WHITE ? Board.SIZE - 1 : 0;
        if (piece.getType() == PieceType.PAWN && cell.getRow() == lastRow) {
            cell.setPiece(PieceFactory.createPiece(PieceType.QUEEN, piece.getColor()));
            System.out.println("Pawn promoted to Queen on " + cell.getName());
        }
    }

    private void updateStatus(Color opponent) {
        boolean inCheck = board.isInCheck(opponent);
        boolean canMove = hasAnyLegalMove(opponent);

        if (inCheck && !canMove) {
            status = opponent == Color.WHITE ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
            System.out.println("Checkmate! " + getCurrentPlayer().getName() + " wins.");
        } else if (!canMove) {
            status = GameStatus.STALEMATE;
            System.out.println("Stalemate! It is a draw.");
        } else if (inCheck) {
            System.out.println("Check!");
        }
    }

    // Brute force: every piece of this colour x every cell. 64 x 64 is small.
    private boolean hasAnyLegalMove(Color color) {
        for (Cell[] row : board.getGrid()) {
            for (Cell from : row) {
                Piece piece = from.getPiece();
                if (piece == null || piece.getColor() != color) {
                    continue;
                }
                for (Cell[] targetRow : board.getGrid()) {
                    for (Cell to : targetRow) {
                        if (piece.canMove(board, from, to) && !leavesKingInCheck(from, to)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void resign() {
        Player loser = getCurrentPlayer();
        status = loser.getColor() == Color.WHITE ? GameStatus.BLACK_WINS : GameStatus.WHITE_WINS;
        System.out.println(loser.getName() + " resigns.");
    }
}
