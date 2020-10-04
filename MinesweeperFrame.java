import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import javax.swing.border.*;
public class MinesweeperFrame extends JFrame
{
    private JFrame game;
    private JFrame settingsFrame;
    private JPanel gamePanel;
    private JPanel counterPanel;
    private JPanel timePanel;
    private JButton[][] j;  // 2D array of JButtons
    private TimerTask task;
    private boolean isTimed;
    private boolean isFirstClick = true; // boolean variable to determine if game has begun
    private boolean isGameOver = false;
    private boolean isSettingsOpen = false;
    private boolean isTrue0[][]= {{false,false,false,false,false,false,false},{false,false,false,false,false,false,false},{false,false,false,false,false,false,false}};
    private boolean isTrue1[][]= {{false,false,false,false,false,false,false},{false,false,false,false,false,false,false},{false,false,false,false,false,false,false}};
    private boolean isTrue[][][] = {isTrue0, isTrue1};
    private byte NUMBER_OF_MINES; // constant value for amount of mines
    private byte NUM_ROWS;  // constant value representing number of rows
    private byte NUM_COLS;  // constant value representing number of columns
    private byte numFlags;
    private byte[][] mine;  // 2D array of either 0 or 1 representing if mine at location
    private int numLeftExposed; // int value checking how many tiles are not exposed
    private int a = 1;
    private int x0[][] = {{a,6*a,6*a,a}   ,{2*a,7*a,17*a,22*a},{23*a,18*a,18*a,23*a},{23*a,18*a,18*a,23*a}  ,{2*a,7*a,17*a,22*a}   ,{a,6*a,6*a,a},{2*a,6*a,18*a,22*a,18*a,6*a}};
    private int x1[][] = {{26*a,31*a,31*a,26*a}, {27*a,32*a,42*a,47*a}, {48*a,43*a,43*a,48*a}, {48*a,43*a,43*a,48*a}, {27*a,32*a,42*a,47*a}, {26*a,31*a,31*a,26*a}, {27*a,31*a,43*a,47*a,43*a,31*a}};
    private int x2[][] = {{51*a,56*a,56*a,51*a}   ,{52*a,57*a,67*a,72*a},{73*a,68*a,68*a,73*a},{73*a,68*a,68*a,73*a}  ,{52*a,57*a,67*a,72*a}   ,{51*a,56*a,56*a,51*a},{52*a,56*a,68*a,72*a,68*a,56*a}};
    private int y1[][] = {{2*a,7*a,17*a,22*a},{a,6*a,6*a,a}   ,{2*a,7*a,17*a,22*a} ,{24*a,29*a,39*a,44*a},{45*a,40*a,40*a,45*a},{24*a,29*a,39*a,44*a},{23*a,19*a,19*a,23*a,27*a,27*a}};
    private int places[][][] = {x0,x1,x2};
    //
    private MinesweeperFrame()
    {
        System.out.println("call to constructor requires parameters for game to be created");
    }

    private MinesweeperFrame(byte dimensionX, byte dimensionY, byte num)
    {
        NUM_ROWS = dimensionX;   // sets dimensions of minefield, constant values
        NUM_COLS = dimensionY;

        numLeftExposed = NUM_ROWS * NUM_COLS;  // Total number of tiles on game panel
        mine = new byte[NUM_ROWS][NUM_COLS];  // creates new empty 2D array of mines
        j = new JButton[NUM_ROWS][NUM_COLS]; // creates new empty 2D array to store buttons
        NUMBER_OF_MINES = numFlags = num;  // sets total number of mines, constant value

        makeMinesweeperGame();
    }
    private void makeMinesweeperGame()
    {
        game = new JFrame();
        if(NUM_ROWS == NUM_COLS)
            game.setSize(600,650);
        else
            game.setSize(1000, 630);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        game.setLocation(dim.width/2-game.getWidth()/2, dim.height/2 - game.getHeight()/2);
        game.setTitle("Minesweeper");  // set title of JFrame

        JPanel topPanel = new JPanel();  // create a JPanel
        topPanel.setBackground(Color.LIGHT_GRAY); // set color to light gray
        topPanel.setBounds(50,0,game.getWidth() - 100,75); // set dimensions and location of panel to top of frame
        topPanel.setLayout(null);  // no layout

        counter(NUMBER_OF_MINES, 0);
        counterPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.RED);
                for(int x = 0; x < isTrue[0].length; x++)
                {
                    for(int y = 0; y < isTrue[0][x].length; y++)
                    {
                        if(isTrue[0][x][y])
                            g.fillPolygon(places[x][y], y1[y], places[x][y].length);
                    }
                }
            }
        };
        counterPanel.setBounds(25, topPanel.getHeight()/2 - 25, 74*a, 46*a);
        counterPanel.setBackground(Color.BLACK);
        counterPanel.setLayout(null);
        topPanel.add(counterPanel);

        counter(100,1);
        timePanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.RED);
                for(int x = 0; x < isTrue[1].length; x++)
                {
                    for(int y = 0; y < isTrue[1][x].length; y++)
                    {
                        if(isTrue[1][x][y])
                            g.fillPolygon(places[x][y], y1[y], places[x][y].length);
                    }
                }
            }
        };
        timePanel.setSize( 74*a, 46*a);
        timePanel.setLocation(topPanel.getWidth() - (timePanel.getWidth() + 25), topPanel.getHeight()/2 - 25);
        timePanel.setBackground(Color.BLACK);
        timePanel.setLayout(null);
        topPanel.add(timePanel);

        JButton reset = new JButton();
        reset.setText("Reset");
        reset.setMargin(new Insets(0,0,0,0));
        reset.setPreferredSize(new Dimension(50,49));
        reset.setBounds(topPanel.getWidth()/2 - 25,topPanel.getHeight()/2 - 25,50,50);
        reset.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                    reset();
            }
        });
        topPanel.add(reset);

        JButton settings = new JButton(); // Create settings JButton
        settings.setMargin(new Insets(0,0,0,0));
        settings.setText("Opt.");
        settings.setPreferredSize(new Dimension(50,49));
        settings.setBounds(topPanel.getWidth()/2 - 80,topPanel.getHeight()/2 - 25,50,50);
        settings.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if(!isSettingsOpen)
                    {
                        isSettingsOpen = true;
                        settingsMenu();
                    }
                    else
                    {
                        settingsFrame.dispose();
                        isSettingsOpen = true;
                        settingsMenu();
                    }
                }
            }
        });
        topPanel.add(settings);
        game.add(topPanel);  // add top panel to frame

        gamePanel = new JPanel();  // create a JPanel
        gamePanel.setBackground(Color.LIGHT_GRAY);  // set color to gray
        gamePanel.setBounds(50,80,game.getWidth() - 100,game.getHeight() - 150);  //set dimensions and location of panel to below top panel
        GridLayout grid = new GridLayout(NUM_ROWS,NUM_COLS);  // create a grid layout for game panel that buttons will be placed onto
        grid.setHgap(1); // sets gap between tiles to 1
        grid.setVgap(1);
        gamePanel.setLayout(grid);  // set layout of game panel to grid

        JButton b;
        for(int x = 0; x < NUM_ROWS; x++)
        {
            for(int y = 0; y < NUM_COLS; y++)
            {

                b = new JButton();  // create new JButton object
                b.setFont(new Font("Arial", Font.BOLD, 12)); // set font of button
                b.setMargin(new Insets(0, 0, 0, 0)); // remove margin in JButton
                Border raisedBorder = BorderFactory.createRaisedBevelBorder();
                b.setBorder(raisedBorder);
                b.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        JButton temp = (JButton)e.getSource();
                        boolean enabled = temp.isEnabled();
                        int indX = 0;
                        int indY = 0;
                        for(int x = 0; x < NUM_ROWS; x++)
                        {
                            for(int y = 0; y < NUM_COLS; y++)
                            {
                                if(j[x][y] == temp)
                                {
                                    indX = x;
                                    indY = y;
                                }
                            }
                        }
                        if (e.getButton() == MouseEvent.BUTTON1 && enabled && temp.getText() != "S")
                        {
                            if(isFirstClick)
                            {
                                makeMineField(indX, indY, NUMBER_OF_MINES);
                                isFirstClick = false;
                            }
                            //

                            if(mine[indX][indY] == 1)
                                lossSequence();
                            else
                            {
                                int mines = Integer.parseInt(numMines(indX,indY));
                                if(mines == 0)
                                    noMines(indX,indY);
                                else
                                {
                                    temp.setText(Integer.toString(mines));
                                    numLeftExposed--;
                                }
                            }
                            temp.setEnabled(false);
                            //
                        }
                        else if(e.getButton() == MouseEvent.BUTTON3) {
                            if (!isFirstClick) {
                                if (numFlags > 0 && enabled && temp.getText() != "S")
                                {
                                    enabled = false;
                                    temp.setText("S");
                                    numFlags--;
                                    counter(numFlags, 0);
                                    counterPanel.repaint();
                                }
                                else if (temp.getText() == "S")
                                {
                                    enabled = true;
                                    temp.setText(null);
                                    numFlags++;
                                    counter(numFlags, 0);
                                    counterPanel.repaint();
                                }
                                else if(!(temp.isEnabled()) && temp.getText() != "S" && temp.getText() != null)
                                {
                                    chord(temp, indX, indY);
                                }
                            }
                        }
                        if(numLeftExposed == NUMBER_OF_MINES && !isGameOver)
                        {
                            winSequence();
                        }
                    }
                });
                j[x][y] = b;
                gamePanel.add(b);
            }
        }
        game.add(gamePanel);
        game.setLayout(null);

        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame gamemode = new JFrame("Minesweeper");
        gamemode.setSize(200,170);
        gamemode.setLocation(dim.width/2 - gamemode.getWidth()/2, dim.height/2 - gamemode.getHeight()/2);
        gamemode.setVisible(true);
        JLabel gameLabel = new JLabel("Choose game mode");
        gameLabel.setHorizontalAlignment(JLabel.CENTER);
        gameLabel.setSize(150,20);
        gameLabel.setLocation(gamemode.getWidth()/2 - gameLabel.getWidth()/2, 10);
        gamemode.add(gameLabel);
        JButton gameTimed = new JButton("Timed");
        gameTimed.setMargin(new Insets(0,0,0,0));
        gameTimed.setSize(80,30);
        gameTimed.setLocation(gamemode.getWidth()/2 - gameTimed.getWidth()/2, 40);
        gameTimed.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gamemode.dispose();
                game.setVisible(true);
                isTimed = true;
                countdown(100, isTimed);
                //isTimed = false;
            }
        });
        gamemode.add(gameTimed);
        JButton gameFree = new JButton("Free play");
        gameFree.setMargin(new Insets(0,0,0,0));
        gameFree.setSize(80,30);
        gameFree.setLocation(gamemode.getWidth()/2 - gameFree.getWidth()/2, 80);
        gameFree.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gamemode.dispose();
                game.setVisible(true);
                isTimed = false;
                countdown(100, isTimed);
            }
        });
        gamemode.add(gameFree);
        gamemode.setLayout(null);
        gamemode.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    //
    private void counter(int numMines, int whichTrue)
    {
        int nums[] = {numMines/100, (numMines%100)/10, numMines%10};
        for(int x = 0; x < isTrue[0].length; x++)
        {
            switch (nums[x])
            {
                case 0:
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][1] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = true;
                    isTrue[whichTrue][x][6] = false;
                    break;
                case 1:
                    isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = true;
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][1] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = isTrue[whichTrue][x][6] = false;
                    break;
                case 2:
                    isTrue[whichTrue][x][1] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = isTrue[whichTrue][x][6] = true;
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][3] = false;
                    break;
                case 3:
                    isTrue[whichTrue][x][1] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][6] = true;
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][5] = false;
                    break;
                case 4:
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][6] = true;
                    isTrue[whichTrue][x][1] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = false;
                    break;
                case 5:
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][1] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][6] = true;
                    isTrue[whichTrue][x][2] = isTrue[whichTrue][x][5] = false;
                    break;
                case 6:
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][1] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = isTrue[whichTrue][x][6] = true;
                    isTrue[whichTrue][x][2] = false;
                    break;
                case 7:
                    isTrue[whichTrue][x][1] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = true;
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = isTrue[whichTrue][x][6] = false;
                    break;
                case 8:
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][1] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][5] = isTrue[whichTrue][x][6] = true;
                    break;
                case 9:
                    isTrue[whichTrue][x][0] = isTrue[whichTrue][x][1] = isTrue[whichTrue][x][2] = isTrue[whichTrue][x][3] = isTrue[whichTrue][x][4] = isTrue[whichTrue][x][6] = true;
                    isTrue[whichTrue][x][5] = false;
                    break;
            }
        }
    }

    private void countdown(int numSeconds, boolean timed)
    {
        task = new TimerTask()
        {
            int seconds = numSeconds;
            int t = 0;
            @Override
            public void run()
            {
                if(!isFirstClick)
                {
                    t++;
                }
                if(timed)
                {
                    if (t % seconds == 0 && !isFirstClick)
                    {
                        counter(999, 1);
                        timePanel.repaint();
                        cancel();
                        lossSequence();
                    }
                    else
                    {
                        counter((seconds - (t % seconds)), 1);
                        timePanel.repaint();
                    }
                }
                else
                {
                    if(t < 999)
                    {
                        counter(t, 1);
                        timePanel.repaint();
                    }
                }
            }
        };
        Timer gameTimer = new Timer();
        gameTimer.schedule(task, 0, 1000);
    }

    private void makeMineField(int startX, int startY, int numMines)
    {
        Random rand = new Random();
        int randX;
        int randY;
        int count = 0;
        while(count < numMines)
        {
            boolean isOk = true;
            randX = rand.nextInt(NUM_ROWS);
            randY = rand.nextInt(NUM_COLS);
            for(int x = randX - 1; x <= randX + 1; x++ )
            {
                for(int y = randY - 1; y <= randY + 1; y++)
                {
                    if(x == startX && y == startY)
                        isOk = false;
                }
            }
            if(isOk && mine[randX][randY] != 1)
            {
                mine[randX][randY] = 1;
                count++;
            }
        }
    }
    //
    private String numMines(int x, int y)
    {
        int count = 0;
        for(int a = x - 1; a <= x + 1; a++)
        {
            for(int b = y - 1; b <= y + 1; b++)
            {
                if(!(a == x && b == y) && (a >= 0 && a < NUM_ROWS && b >= 0 && b < NUM_COLS))
                {
                    if(mine[a][b] == 1)
                        count++;
                }
            }
        }
        return Integer.toString(count);
    }
    //
    private void noMines(int x, int y)
    {
        if(j[x][y].isEnabled())
        {
            j[x][y].setEnabled(false);
            numLeftExposed--;
        }
        for(int a = x - 1; a <= x + 1; a++)
        {
            for(int b = y - 1; b <= y + 1; b++)
            {
                if(a >= 0 && a < NUM_ROWS && b >= 0 && b < NUM_COLS)
                {
                    if(mine[a][b] == 0 && j[a][b].isEnabled())
                    {
                        int mines = Integer.parseInt(numMines(a,b));
                        if(mines == 0)
                        {
                            noMines(a,b);
                        }
                        else
                        {
                            j[a][b].setText(numMines(a,b));
                            j[a][b].setEnabled(false);
                            numLeftExposed--;
                        }
                    }
                    else if(mine[a][b] == 1 && j[a][b].getText() != "S")
                    {
                        j[a][b].setText("X");
                        j[a][b].setEnabled(false);
                    }
                }
            }
        }
    }

    private void reset()
    {
        gamePanel.setVisible(false);
        for(int x = 0; x < NUM_ROWS; x++)
        {
            for(int y = 0; y < NUM_COLS; y++)
            {
                j[x][y].setText(null);
                j[x][y].setEnabled(true);
            }
        }
        mine = new byte[NUM_ROWS][NUM_COLS];
        numLeftExposed = NUM_ROWS * NUM_COLS;
        isFirstClick = true;
        isGameOver = false;
        gamePanel.setVisible(true);
        numFlags = NUMBER_OF_MINES;
        counter(NUMBER_OF_MINES, 0);
        counter(100, 1);
        task.cancel();
        countdown(100, isTimed);
        counterPanel.repaint();
        timePanel.repaint();
    }

    private void settingsMenu()
    {
        int locX = game.getLocation().x + game.getSize().width/2;
        int locY = game.getLocation().y + game.getSize().height/2;

        settingsFrame = new JFrame("Settings");
        settingsFrame.setLayout(null);
        settingsFrame.setSize(200,200);
        settingsFrame.setLocation(locX - settingsFrame.getWidth()/2, locY - settingsFrame.getHeight()/2);
        settingsFrame.setVisible(true);

        JLabel difficulty = new JLabel("Choose difficulty");
        difficulty.setBounds(settingsFrame.getSize().width/2 - 50, 10, 100, 20);
        settingsFrame.add(difficulty);

        JButton easy = new JButton("Easy");
        easy.setBounds(settingsFrame.getWidth()/2 - 40,40,80,20);
        easy.setHorizontalAlignment(SwingConstants.CENTER);
        easy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                settingsFrame.dispose();
                if(NUM_ROWS == 9 && NUM_COLS == 9)
                {
                    reset();
                }
                else
                {
                    game.dispose();
                    new MinesweeperFrame((byte)9,(byte)9,(byte)10);
                }
                isSettingsOpen = false;
            }
        });
        JButton intermediate = new JButton("Intermediate");
        intermediate.setBounds(settingsFrame.getWidth()/2 - 60,70,120,20);
        intermediate.setHorizontalAlignment(SwingConstants.CENTER);
        intermediate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                settingsFrame.dispose();
                if(NUM_ROWS == 16 && NUM_COLS == 16)
                {
                    reset();
                }
                else
                {
                    game.dispose();
                    new MinesweeperFrame((byte)16,(byte)16, (byte)40);
                }
                isSettingsOpen = false;
            }
        });
        JButton expert = new JButton("Expert");
        expert.setBounds(settingsFrame.getWidth()/2 - 40,100,80,20);
        expert.setHorizontalAlignment(SwingConstants.CENTER);
        expert.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                settingsFrame.dispose();
                if(NUM_ROWS == 16 && NUM_COLS == 30)
                {
                    reset();
                }
                else
                {
                    game.dispose();
                    new MinesweeperFrame((byte)16,(byte)30, (byte)99);
                }
                isSettingsOpen = false;
            }
        });
        settingsFrame.add(easy);
        settingsFrame.add(intermediate);
        settingsFrame.add(expert);
        settingsFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    private void chord(JButton b, int xIndex, int yIndex)
    {
        int numSealed = 0;
        int numCorrectSealed = 0;
        for(int x = xIndex - 1; x <= xIndex + 1; x++)
        {
            for(int y = yIndex - 1; y <= yIndex + 1; y++)
            {
                if(!(x == xIndex && y == yIndex) && (x >= 0 && x < NUM_ROWS && y >= 0 && y < NUM_COLS))
                {
                    if(j[x][y].getText() == "S")
                    {
                        numSealed++;
                        if (mine[x][y] == 1)
                            numCorrectSealed++;
                    }
                }
            }
        }
        if(numSealed > 0)
        {
            noMines(xIndex, yIndex);
            if (numCorrectSealed != Integer.parseInt(b.getText()))
                lossSequence();
        }
    }
    //
    private void lossSequence()
    {
        gamePanel.setVisible(false);
        for(int x = 0; x < NUM_ROWS; x++)
        {
            for(int y = 0; y < NUM_COLS; y++)
            {
                if(mine[x][y] == 1)
                {
                    j[x][y].setText("X");
                }
                j[x][y].setEnabled(false);
            }
        }
        gamePanel.setVisible(true);
        task.cancel();
        isGameOver = true;
        //
        JFrame lose = new JFrame("Game Done");
        lose.setBounds((this.getSize().width)/2 - 100, (this.getSize().height)/2 - 75,200,140);
        Dimension dimLose = Toolkit.getDefaultToolkit().getScreenSize();
        lose.setLocation(dimLose.width/2-lose.getSize().width/2, dimLose.height/2-lose.getSize().height/2);
        //
        JTextField loseMessage = new JTextField("Boom you lost");
        loseMessage.setBounds(50,20,100,40);
        loseMessage.setHorizontalAlignment(JTextField.CENTER);        loseMessage.setEditable(false);
        lose.add(loseMessage);
        //
        JButton loseButton = new JButton();
        loseButton.setText("Ok");
        loseButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                lose.dispose();
            }
        });
        loseButton.setBounds(75,70,50,20);
        loseButton.setHorizontalAlignment(SwingConstants.CENTER);
        lose.add(loseButton);
        lose.setLayout(null);
        lose.setVisible(true);
        lose.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //
    private void winSequence()
    {
        for(int x = 0; x < NUM_ROWS; x++)
        {
            for(int y = 0; y < NUM_COLS; y++)
            {
                if(mine[x][y] == 1)
                {
                    j[x][y].setText("S");
                }
                j[x][y].setEnabled(false);
            }
        }
        task.cancel();
        isGameOver = true;
        JFrame win = new JFrame("Game Done");
        win.setBounds((this.getSize().width)/2 - 150, (this.getSize().height)/2 - 75,300,140);
        Dimension dimWin = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(dimWin.width/2-win.getSize().width/2, dimWin.height/2-win.getSize().height/2);
        //
        JTextField winMessage = new JTextField("Congratulations! You won the game!");
        winMessage.setBounds(50,20,200,40);
        winMessage.setHorizontalAlignment(JTextField.CENTER);
        winMessage.setEditable(false);
        win.add(winMessage);
        //
        JButton winButton = new JButton();
        winButton.setText("Ok");
        winButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                win.dispose();
            }
        });
        winButton.setBounds(125,70,50,20);
        winButton.setHorizontalAlignment(SwingConstants.CENTER);
        win.add(winButton);
        //
        win.setLayout(null);
        win.setVisible(true);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //
    public static void main(String[] args)
    {
        Dimension dimFrame = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame difficulty = new JFrame("Minesweeper"); // make JFrame
        JLabel lbl = new JLabel("Choose difficulty"); // create instance of JLabel
        lbl.setBounds(100,10,100,30); // set x-axis, y-axis, width, and height of label
        difficulty.setSize(300,300);
        difficulty.setLocation(dimFrame.width/2 - difficulty.getWidth()/2, dimFrame.height/2 - difficulty.getHeight()/2);
        lbl.setHorizontalAlignment(JLabel.CENTER);
        //
        JButton easy = new JButton("Easy");
        easy.setBounds(110,50,80,20);
        easy.setHorizontalAlignment(SwingConstants.CENTER);
        easy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                difficulty.dispose();
                MinesweeperFrame game = new MinesweeperFrame((byte)9,(byte)9, (byte)10);
            }
        });
        JButton intermediate = new JButton("Intermediate");
        intermediate.setBounds(90,80,120,20);
        intermediate.setHorizontalAlignment(SwingConstants.CENTER);
        intermediate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                difficulty.dispose();
                MinesweeperFrame game = new MinesweeperFrame((byte)16,(byte)16,(byte)40);
            }
        });
        JButton hard = new JButton("Expert");
        hard.setBounds(110,110,80,20);
        hard.setHorizontalAlignment(SwingConstants.CENTER);
        hard.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                difficulty.dispose();
                MinesweeperFrame game = new MinesweeperFrame((byte)16, (byte)30,(byte)99);
            }
        });
        difficulty.add(lbl);
        difficulty.add(easy);
        difficulty.add(intermediate);
        difficulty.add(hard);
        difficulty.setSize(300,200);
        difficulty.setLayout(null);
        difficulty.setVisible(true);
        difficulty.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}