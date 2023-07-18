package 扫雷.src.cn.com.mine;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class MineGame extends JFrame implements ActionListener {
	JMenuBar bar;
	JMenu 选择难度, 透视模式,存读档;
	JMenuItem 初级, 中级, 高级, 自定义;
	JMenuItem 透视;
	JMenuItem 存档,读档;
	MineArea mineArea;
	JDialog set ;
	JPanel panel, panel1, panel2, panel3, panel4;
	JLabel label, label1, label2, label3,label4;
	JTextField row , column, mine;
	JButton 确认,取消;
	JDialog visibleMode, play ;


	MineGame() {
//		初始化是中等难度
		mineArea = new MineArea(16, 16, 40);
		add(mineArea, BorderLayout.CENTER); // 边框布局
		bar = new JMenuBar();
		选择难度 = new JMenu("Level");
		初级 = new JMenuItem("Easy");
		中级 = new JMenuItem("Medium");
		高级 = new JMenuItem("Hard");
		自定义 = new JMenuItem("Customize");
		选择难度.add(初级);
		选择难度.add(中级);
		选择难度.add(高级);
		选择难度.add(自定义);
		透视模式 = new JMenu("Visible Mode");
		透视 = new JMenuItem("Visible");
		透视模式.add(透视);
		存读档 = new JMenu("Archiver");
		存档 = new JMenuItem("Save");
		读档 = new JMenuItem("Read");
		bar.add(选择难度);
		bar.add(存读档);
		bar.add(透视模式);

		setJMenuBar(bar); // 设置窗体的菜单栏
		初级.addActionListener(this);
		中级.addActionListener(this);
		高级.addActionListener(this);
		自定义.addActionListener(this);
		透视.addActionListener(this);
		存档.addActionListener(this);
		读档.addActionListener(this);
		setBounds(300, 100, 480, 560); // 移动组件并调整大小
		setVisible(true); // 使Window可见
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭Window的同时关闭资源
		setResizable(false);
		ImageIcon gameIcon = new ImageIcon("src/icon.png");
		gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
		this.setIconImage(gameIcon.getImage());
		validate(); // 再次布置子组件
		setTitle("Monkey VS Mine");
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == 初级) {
			mineArea.initMineArea(9, 9, 10);
			setBounds(300, 100, 270, 350);
		}
		if (e.getSource() == 中级) {
			mineArea.initMineArea(16, 16, 40);
			setBounds(300, 100, 480, 560);
		}
		if (e.getSource() == 高级) {
			mineArea.initMineArea(16, 30, 99);
			setBounds(100, 100, 900, 560);
		}
		if (e.getSource() == 自定义) {
			set = new JDialog();
			set.setTitle("Customize");
			set.setBounds(300,100,300,150);
			set.setResizable(false);//设置大小不可变
			set.setModal(true);//设置为对话框模式
			panel = new JPanel();
			//panel.setLayout(new BorderLayout());
			panel1 = new JPanel();
			panel2 = new JPanel();
			panel3 = new JPanel();
			panel4 = new JPanel();
			label = new JLabel("Enter the row and column", JLabel.CENTER);
			label1 = new JLabel("Row：", JLabel.CENTER);
			label2 = new JLabel("Column：", JLabel.CENTER);
			label3 = new JLabel("Number of mines：", JLabel.CENTER);
			row = new JTextField();
			row.setText("16");
			row.setSize(2, 8);
			column = new JTextField();
			column.setText("16");
			mine = new JTextField();
			mine.setText("40");
			确认 = new JButton("Confirm");
			确认.addActionListener(this);
			取消 = new JButton("Cancel");
			取消.addActionListener(this);
			panel1.add(label1);
			panel1.add(row);
			panel2.add(label2);
			panel2.add(column);
			panel3.add(label3);
			panel3.add(mine);
			panel4.add(确认);
			panel4.add(取消);
			panel.add(panel1);
			panel.add(panel2);
			panel.add(panel3);
			set.add(label, BorderLayout.NORTH);
			set.add(panel, BorderLayout.CENTER);
			set.add(panel4, BorderLayout.SOUTH);
			set.setVisible(true);
		}
		if (e.getSource() == 确认) {
			int rowNum = Integer.parseInt(row.getText());
			int columnNum = Integer.parseInt(column.getText());
			int mineNum = Integer.parseInt(mine.getText());
			if(rowNum > 24){
				rowNum = 24;
			}else if (rowNum <= 9){
				rowNum = 9;
			}
			if(columnNum > 30){
				columnNum = 30;
			}else if (columnNum <= 9){
				columnNum = 9;
			}
			if(mineNum > rowNum * columnNum / 2){
				mineNum = rowNum * columnNum / 2;
			}else if (mineNum <= 0){
				mineNum = 1;
			}

			mineArea.initMineArea(rowNum, columnNum, mineNum);
			setBounds(100, 100, columnNum * 30, rowNum * 30 + 80);
			set.setVisible(false);
		}
		if (e.getSource() == 取消) {
			set.setVisible(false);
		}
//		if (e.getSource() == 透视) {
//			mineArea.initMineArea(rowNum, columnNum, mineNum);
//			setBounds(100, 100, columnNum * 30, rowNum * 30 + 80);
//			set.setVisible(false);
//
//		}

		validate();
	}

	public static void main(String args[]) {
		new MineGame();
	}
}
