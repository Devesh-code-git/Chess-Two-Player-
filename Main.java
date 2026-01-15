import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JPanel implements MouseListener{
    Piece game = new Piece();
    private int[][] board = game.getBoard();
    private int[][] color = {{0, 1, 0, 1, 0, 1, 0, 1},
                             {1, 0, 1, 0, 1, 0, 1, 0},
                             {0, 1, 0, 1, 0, 1, 0, 1},
                             {1, 0, 1, 0, 1, 0, 1, 0},
                             {0, 1, 0, 1, 0, 1, 0, 1},
                             {1, 0, 1, 0, 1, 0, 1, 0},
                             {0, 1, 0, 1, 0, 1, 0, 1},
                             {1, 0, 1, 0, 1, 0, 1, 0}}; // 2D array representing the board, 0 = white tile, 1 = black tile

    // White pieces
    Image pawnW; Image rookW; Image knightW; Image bishopW; Image kingW; Image queenW;

    // Black pieces
    Image pawnB; Image rookB; Image knightB; Image bishopB; Image kingB; Image queenB;

    private int selected_x = 0, selected_y = 0, current_piece = 0, current_x = 0, current_y = 0;
    private boolean piece_selected = false;
    private boolean piece_moved = false;
    private boolean highlight = false;
    private boolean player_turn = true;

    public static void main(String[] args) {
        new Frame();
    }

    public Main() {
        // Setting up the panel
        this.setPreferredSize(new Dimension(800, 800));
        this.setBackground(new Color(129, 84, 56));
        this.setLayout(null);

        // Adding all pieces to the panel
        pawnW = new ImageIcon("pawn-w.png").getImage();
        rookW = new ImageIcon("rook-w.png").getImage();
        knightW = new ImageIcon("knight-w.png").getImage();
        bishopW = new ImageIcon("bishop-w.png").getImage();
        kingW = new ImageIcon("king-w.png").getImage();
        queenW = new ImageIcon("queen-w.png").getImage();

        pawnB = new ImageIcon("pawn-b.png").getImage();
        rookB = new ImageIcon("rook-b.png").getImage();
        knightB = new ImageIcon("knight-b.png").getImage();
        bishopB = new ImageIcon("bishop-b.png").getImage();
        kingB = new ImageIcon("king-b.png").getImage();
        queenB = new ImageIcon("queen-b.png").getImage();

        addMouseListener(this);
    }
    
    // Paint component method for 2D graphics
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        super.paintComponent(g);

        // Makes the graphics more better, less pixelated
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < 8; i++) { // Making the board
            for (int j = 0; j < 8; j++) {
                if (color[i][j] == 0) {
                    g2D.setColor(new Color(240, 234, 214));
                    g2D.fillRect(i*100, j*100, 100, 100);
                }
            }
        }

        // If white gets checkmated than white king will be highlighted red
        if (game.white_checkmate()) {
            g2D.setColor(new Color(139, 0, 0));
            g2D.fillRect(game.white_king_X*100, game.white_king_Y*100, 100, 100);
        }

        // If black gets checkmated than black king will be highlighted red
        if (game.black_checkmate()) {
            g2D.setColor(new Color(139, 0, 0));
            g2D.fillRect(game.black_king_X*100, game.black_king_Y*100, 100, 100);
        }

        if (highlight) { // When a piece is selected gets highlighted
            if (color[selected_y][selected_x] == 0) { // If on white tile, will get brighter highlight
                g2D.setColor(new Color(251, 236, 93));
                g2D.fillRect(selected_x*100, selected_y*100, 100, 100);
            } else {                                  // Else if on black tile, will get darker highlight
                g2D.setColor(new Color(211, 196, 53));
                g2D.fillRect(selected_x*100, selected_y*100, 100, 100);
            }
        }

        if (piece_moved) { // When a piece is moved, new position is also highlighted
            if (color[current_y][current_x] == 0) { // If on white tile, will get brighter highlight
                g2D.setColor(new Color(251, 236, 93));
                g2D.fillRect(current_x*100, current_y*100, 100, 100);
            } else {                                // Else if on black tile, will get darker highlight
                g2D.setColor(new Color(211, 196, 53));
                g2D.fillRect(current_x*100, current_y*100, 100, 100);
            }
        }

        for (int i = 0; i < 8; i++) { // Drawing the pieces on the board, based on the boards state
            for(int j = 0; j < 8; j++) {
                switch(board[i][j]) {
                    case 1:
                        g2D.drawImage(pawnW, j*100, i*100, 100, 100, null);
                        break;
                    case 2:
                        g2D.drawImage(rookW, j*100, i*100, 100, 100, null);
                        break;
                    case 3:
                        g2D.drawImage(knightW, j*100, i*100, 100, 100, null);
                        break;
                    case 4:
                        g2D.drawImage(bishopW, j*100, i*100, 100, 100, null);
                        break;
                    case 5:
                        g2D.drawImage(kingW, j*100, i*100, 100, 100, null);
                        break;
                    case 6:
                        g2D.drawImage(queenW, j*100, i*100, 100, 100, null);
                        break;
                    case 7:
                        g2D.drawImage(pawnB, j*100, i*100, 100, 100, null);
                        break;
                    case 8:
                        g2D.drawImage(rookB, j*100, i*100, 100, 100, null);
                        break;
                    case 9:
                        g2D.drawImage(knightB, j*100, i*100, 100, 100, null);
                        break;
                    case 10:
                        g2D.drawImage(bishopB, j*100, i*100, 100, 100, null);
                        break;
                    case 11:
                        g2D.drawImage(kingB, j*100, i*100, 100, 100, null);
                        break;
                    case 12:
                        g2D.drawImage(queenB, j*100, i*100, 100, 100, null);
                        break;
                }
            }
        }
    }

    private ImageIcon loadScaledIcon(String path, int size) {
        Image img = new ImageIcon(path).getImage();
        Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    // Method to create a option pane for pawn promotion
    private int showPromotionDialog(boolean white) {
        ImageIcon queen   = loadScaledIcon(white ? "queen-w.png" : "queen-b.png", 100);
        ImageIcon rook    = loadScaledIcon(white ? "rook-w.png"  : "rook-b.png", 100);
        ImageIcon bishop  = loadScaledIcon(white ? "bishop-w.png": "bishop-b.png", 100);
        ImageIcon knight  = loadScaledIcon(white ? "knight-w.png": "knight-b.png", 100);

        Object[] options = { queen, rook, bishop, knight };

        JOptionPane optionPane = new JOptionPane(
                null,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                options[0]
        );

        JDialog dialog = optionPane.createDialog(this, "Pawn Promotion");
        dialog.setSize(600, 175);              
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);               

        Object selectedValue = optionPane.getValue();

        int choice = -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selectedValue)) {
                choice = i;
                break;
            }
        }

        // Return board value based on choice
        if (white) {
            switch (choice) {
                case 0: return 6;  // Queen
                case 1: return 2;  // Rook
                case 2: return 4;  // Bishop
                case 3: return 3;  // Knight
                default: return 6;
            }
        } else {
            switch (choice) {
                case 0: return 12; // Queen
                case 1: return 8;  // Rook
                case 2: return 10; // Bishop
                case 3: return 9;  // Knight
                default: return 12;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.white_checkmate() == false && game.black_checkmate() == false) {
            if (piece_selected == true) { // If a piece is already being selected to move
                current_x = e.getX() / 100;
                current_y = e.getY() / 100;

                if (player_turn && (board[current_y][current_x] >= 1 && board[current_y][current_x] <= 6)) { // If white player picks another white piece
                    selected_x = current_x;
                    selected_y = current_y;
                    current_piece = board[current_y][current_x];
                    repaint();
                } else if (player_turn == false && (board[current_y][current_x] >= 7 && board[current_y][current_x] <= 12)) { // If black player picks another a black piece
                    selected_x = current_x;
                    selected_y = current_y;
                    current_piece = board[current_y][current_x];
                    repaint();
                } else { // Player makes a move
                    switch(current_piece) {
                        case 1: // If a pawn is selected
                        case 7:
                            if (game.pawn(current_piece, selected_x, selected_y, current_x, current_y)) {
                                if (game.updateBoard(current_piece, selected_x, selected_y, current_x, current_y, player_turn)) {

                                    // WHITE PAWN PROMOTION
                                    if (current_piece == 1 && current_y == 0) {
                                        int promotedPiece = showPromotionDialog(true);
                                        board[current_y][current_x] = promotedPiece;
                                    }

                                    // BLACK PAWN PROMOTION
                                    if (current_piece == 7 && current_y == 7) {
                                        int promotedPiece = showPromotionDialog(false);
                                        board[current_y][current_x] = promotedPiece;
                                    }

                                    piece_moved = true;
                                    piece_selected = false;
                                    player_turn = !player_turn; // Switch players turn (white -> black or black -> white)
                                    repaint();
                                }
                            }
                            break;
                        case 2: // If a rook is selected
                        case 8:
                            if (game.rook(selected_x, selected_y, current_x, current_y)) {
                                if (game.updateBoard(current_piece, selected_x, selected_y, current_x, current_y, player_turn)) {
                                    piece_moved = true;
                                    piece_selected = false;
                                    player_turn = !player_turn; // Switch players turn (white -> black or black -> white)
                                    repaint();
                                }
                            }
                            break;
                        case 3: // If a knight is selected
                        case 9:
                            if (game.knight(selected_x, selected_y, current_x, current_y)) {
                                if (game.updateBoard(current_piece, selected_x, selected_y, current_x, current_y, player_turn)) {
                                    piece_moved = true;
                                    piece_selected = false;
                                    player_turn = !player_turn; // Switch players turn (white -> black or black -> white)
                                    repaint();
                                }
                            }
                            break;
                        case 4: // If a bishop is selected
                        case 10:
                            if (game.bishop(selected_x, selected_y, current_x, current_y)) {
                                if (game.updateBoard(current_piece, selected_x, selected_y, current_x, current_y, player_turn)) {
                                    piece_moved = true;
                                    piece_selected = false;
                                    player_turn = !player_turn; // Switch players turn (white -> black or black -> white)
                                    repaint();
                                }
                            }
                            break;
                        case 5: // If a king is selected
                        case 11:
                            if (game.king(current_piece, selected_x, selected_y, current_x, current_y)) {
                                if (game.updateBoard(current_piece, selected_x, selected_y, current_x, current_y, player_turn)) {
                                    piece_moved = true;
                                    piece_selected = false;
                                    player_turn = !player_turn; // Switch players turn (white -> black or black -> white)
                                    repaint();
                                }
                            }
                            break;
                        case 6: // If a queen is selected
                        case 12:
                            if (game.queen(selected_x, selected_y, current_x, current_y)) {
                                if (game.updateBoard(current_piece, selected_x, selected_y, current_x, current_y, player_turn)) {
                                    piece_moved = true;
                                    piece_selected = false;
                                    player_turn = !player_turn; // Switch players turn (white -> black or black -> white)
                                    repaint();
                                }
                            }
                            break;
                    }
                }
            } else if (board[e.getY() / 100][e.getX() / 100] != 0) { // If a piece hasnt been selected, will then get selected
                current_piece = board[e.getY() / 100][e.getX() / 100];

                if (player_turn && (current_piece >= 1 && current_piece <= 6)) {               // If its whites turn
                    piece_moved = false;
                    selected_x = e.getX() / 100;
                    selected_y = e.getY() / 100;
                    piece_selected = true;
                    highlight = true;
                    repaint();
                } else if (player_turn == false && current_piece >= 7 && current_piece <= 12) { // If its blacks turn
                    piece_moved = false;
                    selected_x = e.getX() / 100;
                    selected_y = e.getY() / 100;
                    piece_selected = true;
                    highlight = true;
                    repaint();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}
