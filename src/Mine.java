import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Mine extends JPanel implements ActionListener, MouseListener {
    int x;//横坐标
    int y;//纵坐标

    public static int num = 0;//用于存档
    public static int maxTime;//最多每回合允许点击次数
    int type;
    Type model;
    public static int sum = 0;//首发不触雷
    int mineCount;
    int spend = 31;
    int rest;//剩余雷的数量
    int spend2 = -1;
    JOptionPane loser;
    ImageIcon markIcon = new ImageIcon("src//mark.png");
    Lay lay;
    Player player1;
    Player player2;
    Player player = player1;
    JPanel area;
    JPanel upArea;
    JButton reset;
    JButton start;
    ShowGrid[][] showGrid;
    Grid[][] grids;

    public int getMineCount() {
        return mineCount;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    JFrame text;
    TextArea textArea;
    Timer timer;
    JTextField showTime, showMark;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Mine(int x, int y, int mineCounts) {
        setX(x);
        setY(y);
        setMineCount(mineCounts);
        area = new JPanel();
        upArea = new JPanel();
        setMine(x, y, mineCounts);
    }

    public void setMine(int x, int y, int mineCounts) {
        sum = 0;
        setX(x);
        setY(y);
        setMineCount(mineCounts);
        rest = mineCounts;
        area.removeAll();
        area.setLayout(new GridLayout(x, y));
        this.grids = new Grid[x][y];
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                this.grids[i][i1] = new Grid();
            }
        }
        lay = new Lay();
        lay.LaYMine(x, y, mineCounts, this.grids);
        lay.around(this.grids);
        this.showGrid = new ShowGrid[x][y];
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                this.showGrid[i][i1] = new ShowGrid();
                this.showGrid[i][i1].show_button();
//                this.showGrid[i][i1].show_label();

                this.showGrid[i][i1].getButton().addActionListener(this);
                this.showGrid[i][i1].getButton().addMouseListener(this);
                this.showGrid[i][i1].getButton().setEnabled(true);
                this.showGrid[i][i1].getButton().setIcon(null);
                this.showGrid[i][i1].IconView(this.grids[i][i1]);
                this.showGrid[i][i1].show_blank();
                area.add(showGrid[i][i1]);
            }
        }

        setLayout(new BorderLayout());
//        upArea =new JPanel();
        upArea.removeAll();
        reset = new JButton(new AbstractAction("再来！") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea == null) {
                    JOptionPane.showMessageDialog(area, "请先点击 \'开始\' 按钮！", "提示", JOptionPane.WARNING_MESSAGE, null);
                } else {
                    setMine(x, y, mineCounts);
                    getView();
                    showButton();
                    setText();
                    timer.stop();
                    spend = 31;
                    spend2 = -1;
//                textArea.setText("游戏开始\n"+"玩家1: "+player1.name+"\n"+"玩家2: "+player2.name+"\n");
                }
            }

        });
        start = new JButton(new AbstractAction("开始") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMine(x, y, mineCounts);
                type = JOptionPane.showOptionDialog(area, "请选择游戏模式", "选择模式", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"单人", "双人"}, "单人");
                if (type == 1) {
                    spend = 31;
                    int result = JOptionPane.showOptionDialog(area, "请选择每回合允许单击次数", "选择模式", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"1", "2", "3", "4", "5"}, "1");
                    model = Type.双人;
                    maxTime = result + 1;
                    String result1 = JOptionPane.showInputDialog(area, "请输入玩家1的昵称：", "Player1", JOptionPane.INFORMATION_MESSAGE);
                    if (result1 != null) {
                        player1 = new Player(result1.toString());
                    } else {
                        player1 = new Player("默认角色1");
                    }
                    String result2 = JOptionPane.showInputDialog(area, "请输入玩家2的昵称：", "Player2", JOptionPane.INFORMATION_MESSAGE);
//                    while (result2 == null) {
//                        result2 = JOptionPane.showInputDialog(area, "请输入玩家2的昵称：", "Player2", JOptionPane.INFORMATION_MESSAGE);
//
//                    }
                    if (result2 != null) {
                        player2 = new Player(result2.toString());
                    } else {
                        player2 = new Player("默认角色2");
                    }

                    player = player2;
                    showButton();
                    if (text != null) {
                        text.dispose();
                    }
                    text = new JFrame("游戏进程信息");
                    textArea = new TextArea(20, 30);
                    Font font = new Font("hh", Font.BOLD, 20);
                    textArea.setFont(font);
                    text.add(textArea);
//                    JScrollPane ScrollPane = new JScrollPane(textArea);
//                    ScrollPane scrollPane=new ScrollPane();
//                    textArea.add(scrollPane);
                    text.pack();
                    text.setVisible(true);
                    text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    text.setResizable(false);
                    textArea.setEnabled(false);
//                textArea.setText("游戏开始\n" + "玩家1: " + player1.name + "\n" + "玩家2: " + player2.name + "\n");
                    setText();
                }
                if (type == 0) {
                    spend2 = -1;
                    model = Type.单人;
                    showButton();
                    if (text != null) {
                        text.dispose();
                    }
                    text = new JFrame("游戏进程信息");
                    textArea = new TextArea(20, 30);
                    Font font = new Font("hh", Font.BOLD, 20);
                    textArea.setFont(font);
                    text.add(textArea);
                    text.pack();
                    text.setVisible(true);
                    text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    text.setResizable(false);
                    textArea.setEnabled(false);
//                textArea.setText("游戏开始\n" + "玩家1: " + player1.name + "\n" + "玩家2: " + player2.name + "\n");
                    textArea.setText("单人模式启动\n");
                    player = new Player("player");
                }
            }
        });
        timer = new Timer(1000, this);
        showTime = new JTextField(2);
        showTime.setFont(new Font("h", Font.BOLD, 16));
        showTime.setDisabledTextColor(Color.BLACK);
        showTime.setBorder(BorderFactory.createLineBorder(Color.gray));
        showTime.setEnabled(false);
        showMark = new JTextField(2);
        showTime.setFont(new Font("h", Font.BOLD, 16));
        showMark.setDisabledTextColor(Color.RED);
        showMark.setBorder(BorderFactory.createLineBorder(Color.gray));
        showMark.setEnabled(false);
        showMark.setText("" + rest);
        upArea.add(showMark);
        upArea.add(start);
        upArea.add(reset);
        upArea.add(showTime);
        add(upArea, BorderLayout.NORTH);
        add(area, BorderLayout.CENTER);
        validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() != reset && e.getSource() != start && e.getSource() != timer) {
//            timer.start();
//        }
        for (int i = 0; i < this.x; i++) {
            for (int i1 = 0; i1 < this.y; i1++) {
//                timer.start();
                if (e.getSource() == showGrid[i][i1].getButton()) {
                    if (sum >= 1 || !grids[i][i1].isMine) {
                        timer.start();
                    }
                    getBlank(i, i1);
                    sum++;
                    showGrid[i][i1].show_label();
                    grids[i][i1].isClicked = true;
                    if (grids[i][i1].isMine && sum != 1) {
                        loseScore();
                        rest--;
                        showMark.setText("" + rest);
                        showGrid[i][i1].show_label();
                        grids[i][i1].isClicked = true;
                        isEnd();
                    }

                    if ((sum > 1 || grids[i][i1].isMine == false) && model == Type.双人) {
                        player.times++;

//                        textArea.append(Integer.toString(player.times)+"\n");
                        if (player.times == maxTime) {
                            player.times = 0;
                            spend = 31;
                        }
                    }
                    if (grids[i][i1].isMine && sum == 1) {
                        setMine(x, y, mineCount);
                        getView();
                        showButton();
                        setText();
                        timer.stop();
                        spend = 32;
                    }
                }
            }
        }
        if (e.getSource() == timer) {
            if (model == Type.双人) {
                turn();
                spend--;
                showTime.setText("" + spend);
                timer.setDelay(1000);
                if (spend == 0) {
                    spend = 31;
                }
            }
            if (model == Type.单人) {
                spend2++;
                timer.setDelay(1000);
                showTime.setText("" + spend2);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {//右键单击，用于标记雷区

    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                if (source == this.showGrid[i][i1].getButton() && e.getModifiers() == InputEvent.BUTTON3_MASK) {
                    sum++;
                    timer.start();
                    player.times++;
                    if (grids[i][i1].isMarked && model == Type.单人) {
                        showGrid[i][i1].getButton().setIcon(null);
                        grids[i][i1].isMarked = false;
                    } else if (!grids[i][i1].isMarked && model == Type.单人) {
                        showGrid[i][i1].getButton().setIcon(markIcon);
                        grids[i][i1].isMarked = true;
                        if (grids[i][i1].isMine) {
                            rest--;
                            showMark.setText("" + rest);
                            getScore();
                            isEnd();
                        } else {
                            wrongMark(grids[i][i1], showGrid[i][i1]);

                        }
                    } else if (!grids[i][i1].isMarked && model == Type.双人) {
                        if (grids[i][i1].isMine) {
                            getScore();
                            grids[i][i1].isMarked = true;
                            rest--;
                            showMark.setText("" + rest);
                            showGrid[i][i1].getButton().setIcon(markIcon);
                            isEnd();
//                            showGrid[i][i1].getButton().setEnabled(false);
                        } else {
                            wrongMark(grids[i][i1], showGrid[i][i1]);
                        }
                    }
                    if (player.times == maxTime) {
                        spend = 31;
                        player.times = 0;
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void showButton() {
        for (ShowGrid[] showGrids : showGrid) {
            for (ShowGrid grid : showGrids) {
                grid.show_button();
            }
        }
    }

    public void showLabel() {
        for (ShowGrid[] showGrids : showGrid) {
            for (ShowGrid grid : showGrids) {
                grid.show_label();
            }
        }
    }

    public void setText() {
        if (model == Type.双人) {
            textArea.setText("游戏开始\n" + "玩家1: " + player1.name + "\n" + "玩家2: " + player2.name + "\n");
        } else {
            textArea.setText("单人模式启动\n");
        }
    }

    public void youLose() {
        if (model == Type.双人) {
            loser = new JOptionPane();
            Booms.play();
            JOptionPane.showMessageDialog(this, player.name + ",you lose", "游戏结束", JOptionPane.WARNING_MESSAGE, new ImageIcon("mine.GIF"));
        }
        if (model == Type.单人) {
            loser = new JOptionPane();
            Booms.play();
            JOptionPane.showMessageDialog(this, "Oh no", "游戏结束", JOptionPane.WARNING_MESSAGE, new ImageIcon("mine.GIF"));
            textArea.append("You have got " + player.record + " scores\n");
            timer.stop();
            showLabel();
        }
    }

    public void getView() {
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                this.showGrid[i][i1].IconView(this.grids[i][i1]);
            }
        }
    }

    public void turn() {
        if (model == Type.双人) {
            if (player.times == maxTime && player == player1) {
                player = player2;
                player.times = 0;
                Beep.play();
                textArea.append("Now is " + player.name + "'s turn\n");
            } else if (player.times == maxTime && player == player2) {
                player = player1;
                player.times = 0;
                Beep.play();
                textArea.append("Now is " + player.name + "'s turn\n");
            } else if (spend == 31 && player == player1) {
                player = player2;
                player.times = 0;
                Beep.play();
                textArea.append("Now is " + player.name + "'s turn\n");
            } else if (spend == 31 && player == player2) {
                player = player1;
                Beep.play();
                player.times = 0;
                if (sum == 1) {
                    player.times++;
                }
                textArea.append("Now is " + player.name + "'s turn\n");
                Beep.play();
            }
        }
    }

    public void getScore() {
        if (model == Type.双人) {
            player.record++;
            textArea.append(player.name + " has got 1 score\n");
            Scored.play();
        } else {
            player.record++;
            textArea.append("You got 1 score\n");
            Scored.play();
        }
    }

    public void loseScore() {
        if (model == Type.双人) {
            player.record--;
            player.mistake++;
            textArea.append(player.name + " has lost 1 score\n");
            WrongFlag.play();
        } else {
            youLose();

//            player.record--;
//            textArea.append("You lost 1 score\n");
        }
    }

    public void getBlank(int i, int j) {
        if (i >= 0 && i < x && j >= 0 && j < y && grids[i][j].mineAround == 0 && grids[i][j].isClicked == false && !grids[i][j].isMine) {
            showGrid[i][j].show_label();
            grids[i][j].isClicked = true;
            if (i == 0 && j == 0) {
                getBlank(i + 1, j);
                getBlank(i, j + 1);
            } else if (i == 0 && j == y - 1) {
                getBlank(i + 1, j);
                getBlank(i, j - 1);
            } else if (i == x - 1 && j == 0) {
                getBlank(i - 1, j);
                getBlank(i, j + 1);
            } else if (i == x - 1 && j == y - 1) {
                getBlank(i - 1, j);
                getBlank(i, j - 1);
            } else if (i == 0) {
                getBlank(i + 1, j);
                getBlank(i, j + 1);
                getBlank(i, j - 1);
            } else if (i == x - 1) {
                getBlank(i - 1, j);
                getBlank(i, j + 1);
                getBlank(i, j - 1);
            } else if (j == 0) {
                getBlank(i, j + 1);
                getBlank(i + 1, j);
                getBlank(i - 1, j);
            } else if (j == y - 1) {
                getBlank(i, j - 1);
                getBlank(i + 1, j);
                getBlank(i - 1, j);
            } else {
                getBlank(i - 1, j);
                getBlank(i, j - 1);
                getBlank(i, j + 1);
                getBlank(i + 1, j);
            }
        } else if (i >= 0 && i < x && j >= 0 && j < y && grids[i][j].mineAround > 0 && grids[i][j].isClicked == false) {
            showGrid[i][j].show_label();
            grids[i][j].isClicked = true;
        }
    }

    public void wrongMark(Grid grid, ShowGrid showGrid) {
        if (model == Type.双人) {
            player.mistake++;
            grid.isClicked = true;
            showGrid.show_label();
            textArea.append(player.name + " gets a wrong mark\n");
        } else {
            grid.isClicked = false;
            grid.isMarked = true;
            showGrid.getButton().setIcon(markIcon);
        }
    }

    public void save() {
        if (textArea == null) {
            JOptionPane.showMessageDialog(area, "请先点击 \'开始\' 按钮！", "提示", JOptionPane.WARNING_MESSAGE, null);
        } else {
            File file = new File("src//save" + Integer.toString(num) + ".txt");
            while (file.exists()) {
                num++;
                file = new File("src//save" + Integer.toString(num) + ".txt");
            }
            String s1 = new String();
            if (model == Type.双人) {
                s1 += 2 + "\n";
            } else {
                s1 += 1 + "\n";
            }//单人还是双人
            s1 += Integer.toString(x);
            s1 += "\n";
            s1 += Integer.toString(y);
            s1 += "\n";
            s1 += Integer.toString(mineCount) + "\n";
            s1 += rest + "\n";
            if(model==Type.单人){
                s1+= spend2+"\n";
            }
            if (model == Type.双人) {
                s1 += player1.name + "\n";
                s1 += player2.name + "\n";
                s1 += Integer.toString(player1.record) + "\n";
                s1 += Integer.toString(player2.record) + "\n";
                s1 += player1.mistake + "\n";
                s1 += player2.mistake + "\n";
                if (player == player1) {
                    s1 += Integer.toString(1) + "\n";
                } else {
                    s1 += Integer.toString(2) + "\n";
                }//轮到玩家一还是玩家二
                s1 += Integer.toString(player.times) + "\n";//该玩家已经走了多少次
                s1 += maxTime + "\n";//每回合行动数
                s1 += spend + "\n";
            }
            for (int i = 0; i < x; i++) {
                for (int i1 = 0; i1 < y; i1++) {
                    if (grids[i][i1].isMine) {
                        s1 += "M";
                    } else {
                        s1 += "m";
                    }
                    if (grids[i][i1].isClicked) {
                        s1 += "C";
                    } else {
                        s1 += "c";
                    }
                    if (grids[i][i1].isMarked) {
                        s1 += "K";
                    } else {
                        s1 += "k";
                    }
                    s1 += Integer.toString(grids[i][i1].mineAround);
                    s1 += "\n";
                }
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter writer = new FileWriter("src//save" + num + ".txt");
                writer.write(s1);
                writer.close();
            } catch (IOException e) {
                System.out.println("wrong");
            }
        }
    }

    public void read() {
        JFileChooser chooser = new JFileChooser(".");
        chooser.showOpenDialog(area);
        File file = chooser.getSelectedFile();
        if(file==null){
            JOptionPane.showMessageDialog(area, "未读取到存档文件", "提示", JOptionPane.WARNING_MESSAGE, null);
            spend=100;
        }else{
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = null;
        try {
            s = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        int row = 0;
        int col = 0;
        model=Type.无;
        while (true) {
            try {
                if (count == 0 && s.equals("2")) {
                    model = Type.双人;
                } else if (count == 0 && s.equals("1")) {
                    model = Type.单人;
                    player = new Player("默认角色");
                } else if (model == Type.无) {
                    spend = 100;
                    JOptionPane.showMessageDialog(area, "这不是一个存档文件！", "警告", JOptionPane.WARNING_MESSAGE, null);
                    break;
                } else if (count == 1) {
                    this.x = Integer.parseInt(s);
                } else if (count == 2) {
                    this.y = Integer.parseInt(s);
                    grids = new Grid[x][y];
                    showGrid = new ShowGrid[x][y];
                    for (int i = 0; i < x; i++) {
                        for (int i1 = 0; i1 < y; i1++) {
                            grids[i][i1] = new Grid();
                            showGrid[i][i1] = new ShowGrid();
                        }
                    }
                } else if (count == 3) {
                    this.mineCount = Integer.valueOf(s);
                } else if (count == 4) {
                    this.rest = Integer.valueOf(s);
                }else if (count==5&&model==Type.单人){
                    this.spend2=Integer.valueOf(s);
                } else if (count == 5 && model == Type.双人) {
                    player1 = new Player(s);
                } else if (count == 6 && model == Type.双人) {
                    player2 = new Player(s);
                } else if (count == 7 && model == Type.双人) {
                    player1.record = Integer.valueOf(s);
                } else if (count == 8 && model == Type.双人) {
                    player2.record = Integer.valueOf(s);
                } else if (count == 9 && model == Type.双人) {
                    player1.mistake = Integer.valueOf(s);
                } else if (count == 10 && model == Type.双人) {
                    player2.mistake = Integer.valueOf(s);
                } else if (count == 11 && model == Type.双人) {
                    if (s.equals("1")) {
                        player = player1;
                    }
                    if (s.equals("2")) {
                        player = player2;
                    }
                } else if (count == 12 && model == Type.双人) {
                    player.times = Integer.valueOf(s);
                } else if (count == 13 && model == Type.双人) {
                    maxTime = Integer.valueOf(s);
                } else if (count == 14 && model == Type.双人) {
                    spend = Integer.valueOf(s);
                } else {
                    if (s.charAt(0) == 'M') {
                        grids[row][col].isMine = true;
                    } else {
                        grids[row][col].isMine = false;
                    }
                    if (s.charAt(1) == 'C') {
                        grids[row][col].isClicked = true;
                    } else {
                        grids[row][col].isClicked = false;
                    }
                    if (s.charAt(2) == 'K') {
                        grids[row][col].isMarked = true;
                    } else {
                        grids[row][col].isMarked = false;
                    }
                    grids[row][col].mineAround = Integer.parseInt(String.valueOf(s.charAt(3)));

                    col++;
                    if (col == y) {
                        col = 0;
                        row++;
                    }
                    if (row == x) {
                        break;
                    }
                }
                count++;
                if (!((s = bufferedReader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
//        area = new JPanel();
//        area.removeAll();
//        area.setLayout(new GridLayout(x, y));
//        for (int i = 0; i < x; i++) {
//            for (int i1 = 0; i1 < y; i1++) {
//                showGrid[i][i1].removeAll();
//                showGrid[i][i1] = new ShowGrid();
//                showGrid[i][i1].getButton().addActionListener(this);
//                showGrid[i][i1].getButton().addMouseListener(this);
//                showGrid[i][i1].getButton().setEnabled(true);
//                showGrid[i][i1].IconView(grids[i][i1]);
//                showGrid[i][i1].show_label();
//                area.add(showGrid[i][i1]);
//            }
//        }
    }

    public void setTextArea() {
        if (text != null) {
            text.dispose();
        }
        text = new JFrame("游戏进程信息");
        textArea = new TextArea(20, 30);
        Font font = new Font("hh", Font.BOLD, 20);
        textArea.setFont(font);
        text.add(textArea);
        text.pack();
        text.setVisible(true);
        text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        text.setResizable(false);
        textArea.setEnabled(false);
//                textArea.setText("游戏开始\n" + "玩家1: " + player1.name + "\n" + "玩家2: " + player2.name + "\n");
        setText();
        if (model == Type.双人) {
            textArea.append(player1.name + " has got " + player1.record + " scores\n");
            textArea.append(player2.name + " has got " + player2.record + " scores\n");
            textArea.append(player1.name + " has mistaken " + player1.mistake + " times\n");
            textArea.append(player2.name + " has mistaken " + player2.mistake + " times\n");
            if (player == player1) {
                textArea.append("Now is " + player1.name + "'s turn\nyou have used " + player.times + " steps\n");
            } else {
                textArea.append("Now is " + player2.name + "'s turn\nyou have used " + player.times + " steps\n");
            }
        }
    }

    public void setMinex(int x, int y, int mineCounts) {
        sum = 10;
        setX(x);
        setY(y);
        setMineCount(mineCounts);
        area.removeAll();
        area.setLayout(new GridLayout(x, y));
        this.showGrid = new ShowGrid[x][y];
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                this.showGrid[i][i1] = new ShowGrid();
//                this.showGrid[i][i1].show_button();
//                this.showGrid[i][i1].show_label();
//                this.showGrid[i][i1].show_blank();
                this.showGrid[i][i1].getButton().addActionListener(this);
                this.showGrid[i][i1].getButton().addMouseListener(this);
                this.showGrid[i][i1].getButton().setEnabled(true);
                this.showGrid[i][i1].getButton().setIcon(null);
                this.showGrid[i][i1].IconView(this.grids[i][i1]);
                area.add(showGrid[i][i1]);
            }
        }

        setLayout(new BorderLayout());
//        upArea =new JPanel();
        upArea.removeAll();
        reset = new JButton(new AbstractAction("再来！") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMine(x, y, mineCounts);
                getView();
                showButton();
                setText();
                timer.stop();
                spend = 32;
//                textArea.setText("游戏开始\n"+"玩家1: "+player1.name+"\n"+"玩家2: "+player2.name+"\n");
            }

        });
        start = new JButton(new AbstractAction("开始") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMine(x, y, mineCounts);
                type = JOptionPane.showOptionDialog(area, "请选择游戏模式", "选择模式", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"单人", "双人"}, "单人");
                if (type == 1) {
                    int result = JOptionPane.showOptionDialog(area, "请选择每回合允许单击次数", "选择模式", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"1", "2", "3", "4", "5"}, "1");
                    model = Type.双人;
                    maxTime = result + 1;
                    String result1 = JOptionPane.showInputDialog(area, "请输入玩家1的昵称：", "Player1", JOptionPane.INFORMATION_MESSAGE);
                    if (result1 != null) {
                        player1 = new Player(result1.toString());
                    }
                    String result2 = JOptionPane.showInputDialog(area, "请输入玩家2的昵称：", "Player2", JOptionPane.INFORMATION_MESSAGE);
                    if (result2 != null) {
                        player2 = new Player(result2.toString());
                    }

                    player = player2;
                    showButton();
                    if (text != null) {
                        text.dispose();
                    }
                    text = new JFrame("游戏进程信息");
                    textArea = new TextArea(20, 30);
                    Font font = new Font("hh", Font.BOLD, 20);
                    textArea.setFont(font);
                    text.add(textArea);
                    text.pack();
                    text.setVisible(true);
                    text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    text.setResizable(false);
                    textArea.setEnabled(false);
//                textArea.setText("游戏开始\n" + "玩家1: " + player1.name + "\n" + "玩家2: " + player2.name + "\n");
                    setText();
                }
                if (type == 0) {
                    model = Type.单人;
                    showButton();
                    if (text != null) {
                        text.dispose();
                    }
                    text = new JFrame("游戏进程信息");
                    textArea = new TextArea(20, 30);
                    Font font = new Font("hh", Font.BOLD, 20);
                    textArea.setFont(font);
                    text.add(textArea);
                    text.pack();
                    text.setVisible(true);
                    text.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    text.setResizable(false);
                    textArea.setEnabled(false);
//                textArea.setText("游戏开始\n" + "玩家1: " + player1.name + "\n" + "玩家2: " + player2.name + "\n");
                    textArea.setText("单人模式启动\n");
                    player = new Player("player");
                }
            }
        });
        timer = new Timer(30, this);
        showTime = new JTextField(2);
        showTime.setFont(new Font("h", Font.BOLD, 16));
        showTime.setDisabledTextColor(Color.BLACK);
        showTime.setBorder(BorderFactory.createLineBorder(Color.gray));
        showTime.setEnabled(false);
        showMark = new JTextField(2);
        showTime.setFont(new Font("h", Font.BOLD, 16));
        showMark.setDisabledTextColor(Color.RED);
        showMark.setBorder(BorderFactory.createLineBorder(Color.gray));
        showMark.setEnabled(false);
        showMark.setText("" + rest);
        upArea.add(showMark);
        upArea.add(start);
        upArea.add(reset);
        upArea.add(showTime);
        add(upArea, BorderLayout.NORTH);
        add(area, BorderLayout.CENTER);
        validate();
    }

    public void cheat() {
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                if (grids[i][i1].isMine) {
                    showGrid[i][i1].show_label();
                }
            }
        }
    }

    public void noCheat() {
        for (int i = 0; i < x; i++) {
            for (int i1 = 0; i1 < y; i1++) {
                if (grids[i][i1].isMine && !grids[i][i1].isClicked) {
                    showGrid[i][i1].show_button();
                }
            }
        }
    }

    public void isEnd() {
//        textArea.append(String.valueOf(rest)+"\n");
        if (rest == 0 && model == Type.双人) {
            if (player1.record > player2.record) {
                textArea.append(player1.name + " has gotten " + player1.record + " scores.\n" + "mistaken " + player1.mistake + " times.\n");
                textArea.append(player2.name + " has gotten " + player2.record + " scores.\n" + "mistaken " + player2.mistake + " times.\n");
                JOptionPane.showMessageDialog(area, player1.name + " has won the game!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE, null);
                Applause.play();
            }
            if (player1.record < player2.record) {
                textArea.append(player1.name + " has gotten " + player1.record + " scores.\n" + "mistaken " + player1.mistake + " times.\n");
                textArea.append(player2.name + " has gotten " + player2.record + " scores.\n" + "mistaken " + player2.mistake + " times.\n");
                JOptionPane.showMessageDialog(area, player2.name + " has won the game!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE, null);
                Applause.play();
            }
            if (player1.record == player2.record) {
                textArea.append(player1.name + " has gotten " + player1.record + " scores.\n" + "mistaken " + player1.mistake + " times.\n");
                textArea.append(player2.name + " has gotten " + player2.record + " scores.\n" + "mistaken " + player2.mistake + " times.\n");
                if (player1.mistake < player2.mistake)
                    JOptionPane.showMessageDialog(area, player1.name + " has won the game!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE, null);
                Applause.play();
            }
            if (player1.mistake > player2.mistake)
                JOptionPane.showMessageDialog(area, player2.name + " has won the game!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE, null);
            Applause.play();
            if (player1.mistake == player2.mistake) {
                JOptionPane.showMessageDialog(area, "请再战一局！", "棋逢对手！", JOptionPane.INFORMATION_MESSAGE, null);
                Applause.play();
            }
            showLabel();
            timer.stop();
        }
        if (rest == 0 && model == Type.单人) {
            timer.stop();
            JOptionPane.showMessageDialog(area, "高手！请留下姓名！", "You win！", JOptionPane.INFORMATION_MESSAGE, null);
            Applause.play();
            String result3 = JOptionPane.showInputDialog(area, "请留下姓名", "英雄榜", JOptionPane.INFORMATION_MESSAGE);
            String string = new String();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("英雄榜.txt"));
                try {
                    String s = bufferedReader.readLine();
                    while (s != null) {
                        string += s + "\n";
                        s = bufferedReader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            string += result3 + "  用时  " + spend2 + "s";
            switch (y) {
                case 9:
                    string += "   难度：初级";
                    break;
                case 16:
                    string += "   难度：中级";
                    break;
                case 30:
                    string += "   难度：高级";
                    break;
                default:
                    string += "    自定义";
            }
            string += "\n";
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("英雄榜.txt"));

                bufferedWriter.write(string);
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void hero() {
        String string = new String();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src//英雄榜.txt"));
            try {
                String s = bufferedReader.readLine();
                while (s != null) {
                    string += s + "\n";
                    s = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(area, string, "英雄榜！", JOptionPane.INFORMATION_MESSAGE, null);
    }
}
