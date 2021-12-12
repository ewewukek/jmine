package ewewukek.jmine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

public class Main extends JFrame {
    private static final long serialVersionUID = 1;

    private class Panel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
        private static final long serialVersionUID = 1;

        private static final int BUTTON_NORMAL = 0;
        private static final int BUTTON_DOWN = 1;
        private static final int BUTTON_SCARED = 2;
        private static final int BUTTON_WON = 3;
        private static final int BUTTON_DEAD = 4;

        private static final int CELL_OPEN = 0;
        private static final int CELL_CLOSED = 9;
        private static final int CELL_FLAG = 10;
        private static final int CELL_MINE = 11;
        private static final int CELL_MINE_EXPLODED = 12;
        private static final int CELL_FLAG_WRONG = 13;

        private Theme theme;
        private NinePatch fieldPatch;
        private Sprite digitsPanelSprite;
        private SpriteSheet digitsSheet;
        private SpriteSheet buttonSheet;
        private SpriteSheet cellsSheet;

        private JPopupMenu difficultyMenu;
        private DifficultySetting difficulty;
        private MineField field;

        private Dimension dimension;
        private Point buttonPosition;
        private Point leftCounterPosition;
        private Point rightCounterPosition;

        private Timer timer;
        private int timeCounter;

        private boolean mouseIsOverButton;
        private boolean mouseIsOverField;
        private int gridX;
        private int gridY;

        private boolean buttonIsActive;
        private boolean fieldIsActive;
        private boolean mouseRightButtonDown;

        public Panel() {
            try {
                theme = new Theme("theme.properties");
                fieldPatch = new NinePatch("field.png", theme.fieldPatchLeft, theme.fieldPatchTop,
                    theme.fieldPatchRight, theme.fieldPatchBottom);
                digitsPanelSprite = new Sprite("digitsPanel.png");
                digitsSheet = new SpriteSheet("digits.png", 11);
                cellsSheet = new SpriteSheet("cells.png", 14);
                buttonSheet = new SpriteSheet("button.png", 5);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            timer = new Timer(1000, (event) -> {
                timeCounter++;
                repaint();
            });

            setDifficulty(DifficultySetting.EXPERT);

            difficultyMenu = new JPopupMenu();

            JMenuItem beginnerItem = new JMenuItem("Beginner");
            beginnerItem.addActionListener(this);
            difficultyMenu.add(beginnerItem);

            JMenuItem intermediateItem = new JMenuItem("Intermediate");
            intermediateItem.addActionListener(this);
            difficultyMenu.add(intermediateItem);

            JMenuItem expertItem = new JMenuItem("Expert");
            expertItem.addActionListener(this);
            difficultyMenu.add(expertItem);

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void setDifficulty(DifficultySetting difficultySetting) {
            difficulty = difficultySetting;
            newGame();
            updateSize();
        }

        public void newGame() {
            field = new MineField(difficulty);
            timer.stop();
            timeCounter = 0;
        }

        public void updateSize() {
            dimension = new Dimension(
                field.width * cellsSheet.spriteWidth + fieldPatch.left + fieldPatch.right,
                field.height * cellsSheet.spriteHeight + fieldPatch.top + fieldPatch.bottom
            );
            buttonPosition = new Point((dimension.width - buttonSheet.spriteWidth) / 2, theme.buttonMarginTop);
            leftCounterPosition = new Point(theme.counterMarginLeft, theme.counterMarginTop);
            rightCounterPosition = new Point(dimension.width - digitsPanelSprite.width - theme.counterMarginRight, theme.counterMarginTop);

            setPreferredSize(dimension);
        }

        @Override
        public void paintComponent(Graphics g) {
            fieldPatch.draw(g, 0, 0, dimension.width, dimension.height);
            drawDigits(g, leftCounterPosition.x, leftCounterPosition.y, field.mineCount - field.getFlagCount());
            drawDigits(g, rightCounterPosition.x, rightCounterPosition.y, timeCounter);

            if (buttonIsActive && mouseIsOverButton) {
                buttonSheet.draw(g, buttonPosition, BUTTON_DOWN);
            } else {
                if (field.gameIsOver()) {
                    if (field.isDead()) {
                        buttonSheet.draw(g, buttonPosition, BUTTON_DEAD);
                    } else {
                        buttonSheet.draw(g, buttonPosition, BUTTON_WON);
                    }
                } else {
                    if (mouseIsOverField && fieldIsActive) {
                        buttonSheet.draw(g, buttonPosition, BUTTON_SCARED);
                    } else {
                        buttonSheet.draw(g, buttonPosition, BUTTON_NORMAL);
                    }
                }
            }

            int highlightRadius = mouseRightButtonDown ? 2 : 1;

            int y = fieldPatch.top;
            for (int j = 0; j < field.height; j++) {
                int x = fieldPatch.left;
                for (int i = 0; i < field.width; i++) {
                    if (field.isOpen(i, j)) {
                        if (field.isMine(i, j)) {
                            cellsSheet.draw(g, x, y, CELL_MINE_EXPLODED);
                        } else {
                            cellsSheet.draw(g, x, y, field.getNumber(i, j));
                        }
                    } else if (field.isFlagged(i, j)) {
                        if (field.gameIsOver() && !field.isMine(i, j)) {
                            cellsSheet.draw(g, x, y, CELL_FLAG_WRONG);
                        } else {
                            cellsSheet.draw(g, x, y, CELL_FLAG);
                        }
                    } else {
                        if (field.gameIsOver() && field.isMine(i, j)) {
                            cellsSheet.draw(g, x, y, CELL_MINE);
                        } else {
                            if (fieldIsActive && mouseIsOverField
                                && Math.abs(i - gridX) < highlightRadius && Math.abs(j - gridY) < highlightRadius) {

                                cellsSheet.draw(g, x, y, CELL_OPEN);
                            } else {
                                cellsSheet.draw(g, x, y, CELL_CLOSED);
                            }
                        }
                    }
                    x += cellsSheet.spriteWidth;
                }
                y += cellsSheet.spriteHeight;
            }
        }

        void drawDigits(Graphics g, int x, int y, int value) {
            digitsPanelSprite.draw(g, x, y);
            x += theme.counterPaddingLeft;
            y += theme.counterPaddingTop;

            if (value < 0) {
                value = -value;
                digitsSheet.draw(g, x, y, 10);
            } else {
                digitsSheet.draw(g, x, y, (value / 100) % 10);
            }
            x += digitsSheet.spriteWidth + theme.counterDigitSpacing;

            digitsSheet.draw(g, x, y, (value / 10) % 10);
            x += digitsSheet.spriteWidth + theme.counterDigitSpacing;

            digitsSheet.draw(g, x, y, value % 10);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            String cmd = event.getActionCommand();
            if (cmd.equals("Beginner")) {
                setDifficulty(DifficultySetting.BEGINNER);

            } else if (cmd.equals("Intermediate")) {
                setDifficulty(DifficultySetting.INTERMEDIATE);

            } else if (cmd.equals("Expert")) {
                setDifficulty(DifficultySetting.EXPERT);

            } else {
                throw new RuntimeException("unexpected command: " + cmd);
            }
            pack();
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            int x = event.getX();
            int y = event.getY();
            mouseIsOverButton = x >= buttonPosition.x && x < buttonPosition.x + buttonSheet.spriteWidth
                && y >= buttonPosition.y && y < buttonPosition.y + buttonSheet.spriteHeight;
            mouseIsOverField = x >= fieldPatch.left && x < dimension.width - fieldPatch.right
                && y >= fieldPatch.top && y < dimension.height - fieldPatch.bottom;
            if (mouseIsOverField) {
                gridX = (x - fieldPatch.left) / cellsSheet.spriteWidth;
                gridY = (y - fieldPatch.top) / cellsSheet.spriteHeight;
            }
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            mouseMoved(event);
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent event) {
            boolean lmb = event.getButton() == MouseEvent.BUTTON1;
            boolean rmb = event.getButton() == MouseEvent.BUTTON3;
            if (!lmb && !rmb) return;

            if (mouseIsOverButton) {
                if (lmb) {
                    buttonIsActive = true;
                } else {
                    difficultyMenu.show(this, event.getX(), event.getY());
                    fieldIsActive = false;
                    return;
                }
            } else if (mouseIsOverField && !field.gameIsOver()) {
                if (lmb) {
                    fieldIsActive = true;
                } else if (!fieldIsActive) {
                    field.flipFlag(gridX, gridY);
                }
            }
            if (rmb) {
                mouseRightButtonDown = true;
            }
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            boolean lmb = event.getButton() == MouseEvent.BUTTON1;
            boolean rmb = event.getButton() == MouseEvent.BUTTON3;
            if (!lmb && !rmb) return;

            if (mouseIsOverButton && buttonIsActive) {
                newGame();

            } else if (mouseIsOverField && !field.gameIsOver()) {
                if (fieldIsActive) {
                    if (mouseRightButtonDown) {
                        if (field.flagsMatchNumber(gridX, gridY)) {
                            field.openAround(gridX, gridY);
                        }
                    } else if (lmb) {
                        field.open(gridX, gridY);
                    }
                    if (timer.isRunning()) {
                        if (field.gameIsOver()) timer.stop();
                    } else {
                        if (field.gameStarted()) timer.start();
                    }
                }
            }
            if (lmb) {
                buttonIsActive = false;
                fieldIsActive = false;
            }
            if (rmb) {
                mouseRightButtonDown = false;
            }
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent event) {
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        }
    }

    private Main(String title) {
        super(title);
        getContentPane().add(new Panel());
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main("jmine");
    }
}
