
public class Piece {
    private int[][] board = {{8, 9, 10, 12, 11, 10, 9, 8},
                             {7, 7, 7, 7, 7, 7, 7, 7},
                             {0, 0, 0, 0, 0, 0, 0, 0},
                             {0, 0, 0, 0, 0, 0, 0, 0},
                             {0, 0, 0, 0, 0, 0, 0, 0},
                             {0, 0, 0, 0, 0, 0, 0, 0},
                             {1, 1, 1, 1, 1, 1, 1, 1},
                             {2, 3, 4, 6, 5, 4, 3, 2}}; // 2D array representing the board, 0 = empty tile, 1-12 representing a piece
    
    // Coordinates for the black and white kings
    int white_king_X = 4, white_king_Y = 7;
    int black_king_X = 4, black_king_Y = 0;
    private boolean white_castle = false;
    private boolean black_castle = false;

    public Piece() {}

    public int[][] getBoard() {return board;} // Returns the board's state

    // Changes boards state
    public boolean updateBoard(int piece, int prev_x, int prev_y, int cur_x, int cur_y, boolean turn) {
        int piece_on_board = board[cur_y][cur_x]; // Variable to get whatever is on the board where piece is being moved to

        board[prev_y][prev_x] = 0;   // Change pieces previous location to being empty
        board[cur_y][cur_x] = piece; // Change pieces selected location to whatever piece was picked

        if (piece == 5) {
            white_king_X = cur_x;
            white_king_Y = cur_y;
        }

        if (piece == 11) {
            black_king_X = cur_x;
            black_king_Y = cur_y;
        }

        if (piece >= 1 && piece <= 6 && white_check() && turn) {
            board[prev_y][prev_x] = piece;
            board[cur_y][cur_x] = piece_on_board;
            if (piece == 5) {
                white_king_X = prev_x;
                white_king_Y = prev_y;
            }
            return false;
        }

        if (piece >= 7 && black_check() && turn == false) {
            board[prev_y][prev_x] = piece;
            board[cur_y][cur_x] = piece_on_board;
            if (piece == 11) {
                black_king_X = prev_x;
                black_king_Y = prev_y;
            }
            return false;
        }

        if (white_castle && white_check() == false) {
            if (cur_x > 4) {
                board[7][5] = 2;
                board[7][7] = 0;
            } else if (cur_x < 4) {
                board[7][3] = 2;
                board[7][0] = 0;
            }
            white_castle = false;
        }

        if (black_castle && black_check() == false) {
            if (cur_x > 4) {
                board[0][5] = 8;
                board[0][7] = 0;
            } else if (cur_x < 4) {
                board[0][3] = 8;
                board[0][0] = 0;
            }
            black_castle = false;
        }

        return true;
    }

    public boolean white_checkmate() { // Method to see if white king is in checkmate
        boolean valid = false;
        int piece = 0;

        if (white_check() == false) // If whites not in check, than cant have a checkmate
            return false;

        // For loops to have every piece on the board loop over every tile and see if a legal move can be made that breaks out of check
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {

                        int[][] copy = new int[8][8]; // Creating a copy of the board each iteration
                        for (int r = 0; r < 8; r++) {
                            for (int c = 0; c < 8; c++) {
                                copy[r][c] = board[r][c];
                            }
                        }

                        // Getting the original coordinates for the kings
                        int bkx = black_king_X;
                        int bky = black_king_Y;
                        int wkx = white_king_X;
                        int wky = white_king_Y;
                        // Getting castle conditions
                        boolean wc = white_castle;
                        boolean bc = black_castle;

                        piece = board[i][j];

                        if (piece > 6)
                            continue;

                        switch (piece) {
                            case 1:
                                valid = pawn(1, j, i, x, y);
                                break;
                            case 2:
                                valid = rook(j, i, x, y);
                                break;
                            case 3:
                                valid = knight(j, i, x, y);
                                break;
                            case 4:
                                valid = bishop(j, i, x, y);
                                break;
                            case 5:
                                valid = king(5, j, i, x, y);
                                break;
                            case 6:
                                valid = queen(j, i, x, y);
                                break;
                        }

                        if (valid == false)
                            continue;

                        boolean moved = updateBoard(piece, j, i, x, y, true);

                        for (int r = 0; r < 8; r++) { // Restoring the board
                            for (int c = 0; c < 8; c++) {
                                board[r][c] = copy[r][c];
                            }
                        }

                        // Restoring kings coordinates
                        black_king_X = bkx;
                        black_king_Y = bky;
                        white_king_X = wkx;
                        white_king_Y = wky;
                        // Restoring castle conditions
                        white_castle = wc;
                        black_castle = bc;

                        if (moved) // If updateBoard returns true, king is not in checkmate
                            return false;
                    }
                }
            }
        }
        return true; // king is in checkmate
    }

    public boolean black_checkmate() { // Method to see if black king is in checkmate
        boolean valid = false;
        int piece = 0;

        if (black_check() == false) // If blacks not in check, than cant have a checkmate
            return false;

        // For loops to have every piece on the board loop over every tile and see if a legal move can be made that breaks out of check
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {

                        int[][] copy = new int[8][8]; // Creating a copy of the board each iteration
                        for (int r = 0; r < 8; r++) {
                            for (int c = 0; c < 8; c++) {
                                copy[r][c] = board[r][c];
                            }
                        }

                        // Getting the original coordinates for the kings
                        int bkx = black_king_X;
                        int bky = black_king_Y;
                        int wkx = white_king_X;
                        int wky = white_king_Y;
                        // Getting castle conditions
                        boolean wc = white_castle;
                        boolean bc = black_castle;

                        piece = board[i][j];

                        if (piece >= 0 && piece <= 6)
                            continue;

                        switch (piece) {
                            case 7:
                                valid = pawn(7, j, i, x, y);
                                break;
                            case 8:
                                valid = rook(j, i, x, y);
                                break;
                            case 9:
                                valid = knight(j, i, x, y);
                                break;
                            case 10:
                                valid = bishop(j, i, x, y);
                                break;
                            case 11:
                                valid = king(11, j, i, x, y);
                                break;
                            case 12:
                                valid = queen(j, i, x, y);
                                break;
                        }

                        if (valid == false)
                            continue;

                        boolean moved = updateBoard(piece, j, i, x, y, false);

                        for (int r = 0; r < 8; r++) { // Restoring the board
                            for (int c = 0; c < 8; c++) {
                                board[r][c] = copy[r][c];
                            }
                        }

                        // Restoring kings coordinates
                        black_king_X = bkx;
                        black_king_Y = bky;
                        white_king_X = wkx;
                        white_king_Y = wky;
                        // Restoring castle conditions
                        white_castle = wc;
                        black_castle = bc;

                        if (moved) // If updateBoard returns true, king is not in checkmate
                            return false;
                    }
                }
            }
        }

        return true; // king is in checkmate
    }

    public boolean white_check() { // Method to check if whites king is in check
        for (int i = 0; i < 8; i++)  {
            for (int j = 0; j < 8; j++) {
                switch (board[i][j]) {
                    case 7:
                        if (pawn(7, j, i, white_king_X, white_king_Y))
                            return true;
                        break;
                    case 8:
                        if (rook(j, i, white_king_X, white_king_Y))
                            return true;
                        break;
                    case 9:
                        if (knight(j, i, white_king_X, white_king_Y))
                            return true;
                        break;
                    case 10:
                        if (bishop(j, i, white_king_X, white_king_Y))
                            return true;
                        break;
                    case 11:
                        if (king(11, j, i, white_king_X, white_king_Y))
                            return true;
                        break;
                    case 12:
                        if (queen(j, i, white_king_X, white_king_Y))
                            return true;
                        break;
                }
            }
        }
        
        return false;
    }

    public boolean black_check() { // Method to check if blacks king is in check
        for (int i = 0; i < 8; i++)  {
            for (int j = 0; j < 8; j++) {
                switch (board[i][j]) {
                    case 1:
                        if (pawn(1, j, i, black_king_X, black_king_Y))
                            return true;
                        break;
                    case 2:
                        if (rook(j, i, black_king_X, black_king_Y))
                            return true;
                        break;
                    case 3:
                        if (knight(j, i, black_king_X, black_king_Y))
                            return true;
                        break;
                    case 4:
                        if (bishop(j, i, black_king_X, black_king_Y))
                            return true;
                        break;
                    case 5:
                        if (king(5, j, i, black_king_X, black_king_Y))
                            return true;
                        break;
                    case 6:
                        if (queen(j, i, black_king_X, black_king_Y))
                            return true;
                        break;
                }
            }
        }

        return false;
    }


    // Methods for each individual piece to see if a valid move is selected for that piece

    public boolean pawn(int piece, int prev_x, int prev_y, int cur_x, int cur_y) {
        int valid_y;

        // If piece is white
        if (prev_x == 0) { // If white pawn is at left edge of board, will only check for black piece on right corner
            if (piece == 1 && board[prev_y - 1][prev_x + 1] >= 7) {
                if (cur_x == prev_x + 1 && cur_y == prev_y - 1)
                    return true;
            }
        } else if (prev_x == 7) { // If white pawn is at right edge, will only check for black piece on left corner
            if (piece == 1 && board[prev_y - 1][prev_x - 1] >= 7) {
                if (cur_x == prev_x - 1 && cur_y == prev_y - 1)
                    return true;
            }
        } else {
            if (piece == 1 && board[prev_y - 1][prev_x + 1] >= 7) { // If theres a black piece on right corner
                if (cur_x == prev_x + 1 && cur_y == prev_y - 1)
                    return true;
            }
        
            if (piece == 1 && board[prev_y - 1][prev_x - 1] >= 7) { // If theres a black piece on left corner
                if (cur_x == prev_x - 1 && cur_y == prev_y - 1)
                    return true;
            }
        }
        
        if (piece == 1 && prev_y == 6) { // If a white piece is at the start, can move two tiles up
            valid_y = prev_y - 2;

            if (board[valid_y][cur_x] != 0 && board[valid_y + 1][cur_x] != 0) {        // If both tiles are blocked
                return false;
            } else if (board[valid_y + 1][cur_x] != 0) {                               // If tile right in front is blocked
                return false;
            } else if (board[valid_y][cur_x] != 0 && board[valid_y + 1][cur_x] == 0) { // If only second tile is blocked
                valid_y = valid_y + 1; // Valid y-position becomes only one tile up not two
                if (cur_y == valid_y && cur_x == prev_x)
                    return true;
            }

            if (cur_y >= valid_y && cur_y < prev_y && cur_x == prev_x)
                return true;
        } 
        
        if (piece == 1) {
            valid_y = prev_y - 1;

            if (board[valid_y][cur_x] != 0) // If tile right in front is blocked
                return false;

            if (cur_y == valid_y && cur_x == prev_x)
                return true;
        }

        // If piece is black
        if (prev_x == 0) { // If black pawn is at left edge of board, will only check for white piece on right corner
            if (piece == 7 && board[prev_y + 1][prev_x + 1] <= 6 && board[prev_y + 1][prev_x + 1] > 0) {
                if (cur_x == prev_x + 1 && cur_y == prev_y + 1)
                    return true;
            }
        } else if (prev_x == 7) { // If black pawn is at right edge, will only check for white piece on left corner
            if (piece == 7 && board[prev_y + 1][prev_x - 1] <= 6 && board[prev_y + 1][prev_x - 1] > 0) {
                if (cur_x == prev_x - 1 && cur_y == prev_y + 1)
                    return true;
            }
        } else {
            if (piece == 7 && board[prev_y + 1][prev_x + 1] <= 6 && board[prev_y + 1][prev_x + 1] > 0) { // If theres a white piece on right corner
                if (cur_x == prev_x + 1 && cur_y == prev_y + 1)
                    return true;
            }

            if (piece == 7 && board[prev_y + 1][prev_x - 1] <= 6 && board[prev_y + 1][prev_x - 1] > 0) { // If theres a white piece on left corner
                if (cur_x == prev_x - 1 && cur_y == prev_y + 1)
                    return true;
            }
        }

        if (piece == 7 && prev_y == 1) { // If a black piece is at the start, can move two tiles up
            valid_y = prev_y + 2;

            if (board[valid_y][cur_x] != 0 && board[valid_y - 1][cur_x] != 0) {        // If both tiles are blocked
                return false;
            } else if (board[valid_y - 1][cur_x] != 0) {                               // If tile right in front is blocked
                return false;
            } else if (board[valid_y][cur_x] != 0 && board[valid_y + 1][cur_x] == 0) { // If only second tile is blocked
                valid_y = valid_y - 1; // Valid y-position becomes only one tile up not two
                if (cur_y == valid_y && cur_x == prev_x)
                    return true;
            }

            if (cur_y <= valid_y && cur_y > prev_y && cur_x == prev_x)
                return true;
        } 
        
        if (piece == 7) {
            valid_y = prev_y + 1;

            if (board[valid_y][cur_x] != 0) // If tile right in front is blocked
                return false;

            if (cur_y == valid_y && cur_x == prev_x)
                return true;
        }

        return false;
    }

    public boolean rook(int prev_x, int prev_y, int cur_x, int cur_y) {
        int x_left = 0, x_right = 7; // x_left is the left edge of the board, and x_right is the right edge of the board
        int y_up = 0, y_down = 7;    // y_up is the upper edge of the board, and y_down is the lower edge of the board

        // 4 for loops to make the edges whatever piece comes in front first for each direction respectiavley, othwerwise edge stays the same
        for (int i = prev_x + 1; i < 8; i++) { 
            if (board[prev_y][i] != 0) {
                x_right = i;
                break;
            }
        }
        for (int i = prev_x - 1; i >= 0; i--) {
            if (board[prev_y][i] != 0) {
                x_left = i;
                break;
            }
        }

        for (int i = prev_y + 1; i < 8; i++) {
            if (board[i][prev_x] != 0) {
                y_down = i;
                break;
            }
        }
        for (int i = prev_y - 1; i >= 0; i--) {
            if (board[i][prev_x] != 0) {
                y_up = i;
                break;
            }
        }

        if (cur_x >= x_left && cur_x <= x_right && cur_y == prev_y)   // If position picked is a straight path from either left of right
            return true;
        else if (cur_y >= y_up && cur_y <= y_down && cur_x == prev_x) // If position picked is a straight path from either up or down
            return true;

        return false;
    }

    public boolean bishop(int prev_x, int prev_y, int cur_x, int cur_y) {
        int y_up = prev_y - 1; // One above pieces starting position

        for (int i = prev_x + 1; i < 8; i++) { // Loop to see if x and y position picked is valid for the up and right diagonal path
            if (cur_x == i && cur_y == y_up)
                return true;
            else if (y_up < 0)
                break;
            else if (board[y_up][i] != 0)
                break;
            
            y_up-= 1;
        }

        y_up = prev_y - 1;

        for (int i = prev_x - 1; i >= 0; i--) { // Loop to see if x and y position picked is valid for the up and left diagonal path
            if (cur_x == i && cur_y == y_up)
                return true;
            else if (y_up < 0)
                break;
            else if (board[y_up][i] != 0)
                break;
            
            y_up-= 1;
        }

        int y_down = prev_y + 1; // One below pieces starting position

        for (int i = prev_x + 1; i < 8; i++) { // Loop to see if x and y position picked is valid for the down and right diagonal path
            if (cur_x == i && cur_y == y_down)
                return true;
            else if (y_down > 7)
                break;
            else if (board[y_down][i] != 0)
                break;
            
            y_down+= 1;
        }

        y_down = prev_y + 1;

        for (int i = prev_x - 1; i >= 0; i--) { // Loop to see if x and y position picked is valid for the down and left diagonal path
            if (cur_x == i && cur_y == y_down)
                return true;
            else if (y_down > 7)
                break;
            else if (board[y_down][i] != 0)
                break;
            
            y_down+= 1;
        }

        return false;
    }

    public boolean knight(int prev_x, int prev_y, int cur_x, int cur_y) {
        // Will return true if position picked is any of the 8 L-shaped directions for the knight
        if (cur_x == prev_x + 1 && cur_y == prev_y - 2)
            return true;
        if (cur_x == prev_x + 2 && cur_y == prev_y - 1)
            return true;
        if (cur_x == prev_x - 1 && cur_y == prev_y - 2)
            return true;
        if (cur_x == prev_x - 2 && cur_y == prev_y - 1)
            return true;
        if (cur_x == prev_x + 2 && cur_y == prev_y + 1)
            return true;
        if (cur_x == prev_x + 1 && cur_y == prev_y + 2)
            return true;
        if (cur_x == prev_x - 2 && cur_y == prev_y + 1)
            return true;
        if (cur_x == prev_x - 1 && cur_y == prev_y + 2)
            return true;

        return false;
    }

    public boolean king(int piece, int prev_x, int prev_y, int cur_x, int cur_y) {

        // If player makes a castle with the white king
        if (piece == 5 && prev_y == 7 && prev_x == 4) {
            if (board[7][5] == 0 && board[7][6] == 0 && board[7][7] == 2) { // Right hand side castle
                if (cur_x == prev_x + 2 && cur_y == prev_y) {
                    white_castle = true;
                    return true;
                }
            } else if (board[7][0] == 2 && board[7][1] == 0 && board[7][2] == 0 && board[7][3] == 0) { // Left hand side castle
                if (cur_x == prev_x - 2 && cur_y == prev_y) {
                    white_castle = true;
                    return true;
                }
            }
        }
        
        // If player makes a castle with the black king
        if (piece == 11 && prev_y == 0 && prev_x == 4) {
            if (board[0][5] == 0 && board[0][6] == 0 && board[0][7] == 8) { // Right hand side castle
                if (cur_x == prev_x + 2 && cur_y == prev_y) {
                    black_castle = true;
                    return true;
                }
            } else if (board[0][0] == 8 && board[0][1] == 0 && board[0][2] == 0 && board[0][3] == 0) { // Left hand side castle
                if (cur_x == prev_x - 2 && cur_y == prev_y) {
                    black_castle = true;
                    return true;
                }
            }
        } 

        // Checking if king is moved up or diagonally left up or diagonally right up
        if (cur_y == prev_y - 1) {
            if (cur_x == prev_x - 1 || cur_x == prev_x + 1 || cur_x == prev_x)
                return true;
        }

        // Checking if king is moved left or right
        if (cur_y == prev_y) {
            if (cur_x == prev_x - 1 || cur_x == prev_x + 1)
                return true;
        }

        // checking if king is moved down or diagonally left down or diagonally right down
        if (cur_y == prev_y + 1) {
            if (cur_x == prev_x - 1 || cur_x == prev_x + 1 || cur_x == prev_x)
                return true;
        }

        return false;
    }

    public boolean queen(int prev_x, int prev_y, int cur_x, int cur_y) {
        // Since queen has combined movments of bishop and rook, if either is valid based on cur_x and cur_y, then queens move is also valid
        if (bishop(prev_x, prev_y, cur_x, cur_y) || rook(prev_x, prev_y, cur_x, cur_y))
            return true;

        return false;
    }
}
