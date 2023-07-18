package ɨ��.src.cn.com.mine;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class MineGame extends JFrame implements ActionListener {
	JMenuBar bar;
	JMenu ѡ���Ѷ�, ͸��ģʽ,�����;
	JMenuItem ����, �м�, �߼�, �Զ���;
	JMenuItem ͸��;
	JMenuItem �浵,����;
	MineArea mineArea;
	JDialog set ;
	JPanel panel, panel1, panel2, panel3, panel4;
	JLabel label, label1, label2, label3,label4;
	JTextField row , column, mine;
	JButton ȷ��,ȡ��;
	JDialog visibleMode, play ;


	MineGame() {
//		��ʼ�����е��Ѷ�
		mineArea = new MineArea(16, 16, 40);
		add(mineArea, BorderLayout.CENTER); // �߿򲼾�
		bar = new JMenuBar();
		ѡ���Ѷ� = new JMenu("Level");
		���� = new JMenuItem("Easy");
		�м� = new JMenuItem("Medium");
		�߼� = new JMenuItem("Hard");
		�Զ��� = new JMenuItem("Customize");
		ѡ���Ѷ�.add(����);
		ѡ���Ѷ�.add(�м�);
		ѡ���Ѷ�.add(�߼�);
		ѡ���Ѷ�.add(�Զ���);
		͸��ģʽ = new JMenu("Visible Mode");
		͸�� = new JMenuItem("Visible");
		͸��ģʽ.add(͸��);
		����� = new JMenu("Archiver");
		�浵 = new JMenuItem("Save");
		���� = new JMenuItem("Read");
		bar.add(ѡ���Ѷ�);
		bar.add(�����);
		bar.add(͸��ģʽ);

		setJMenuBar(bar); // ���ô���Ĳ˵���
		����.addActionListener(this);
		�м�.addActionListener(this);
		�߼�.addActionListener(this);
		�Զ���.addActionListener(this);
		͸��.addActionListener(this);
		�浵.addActionListener(this);
		����.addActionListener(this);
		setBounds(300, 100, 480, 560); // �ƶ������������С
		setVisible(true); // ʹWindow�ɼ�
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �ر�Window��ͬʱ�ر���Դ
		setResizable(false);
		ImageIcon gameIcon = new ImageIcon("src/icon.png");
		gameIcon.setImage(gameIcon.getImage().getScaledInstance(150, 150,Image.SCALE_SMOOTH));
		this.setIconImage(gameIcon.getImage());
		validate(); // �ٴβ��������
		setTitle("Monkey VS Mine");
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ����) {
			mineArea.initMineArea(9, 9, 10);
			setBounds(300, 100, 270, 350);
		}
		if (e.getSource() == �м�) {
			mineArea.initMineArea(16, 16, 40);
			setBounds(300, 100, 480, 560);
		}
		if (e.getSource() == �߼�) {
			mineArea.initMineArea(16, 30, 99);
			setBounds(100, 100, 900, 560);
		}
		if (e.getSource() == �Զ���) {
			set = new JDialog();
			set.setTitle("Customize");
			set.setBounds(300,100,300,150);
			set.setResizable(false);//���ô�С���ɱ�
			set.setModal(true);//����Ϊ�Ի���ģʽ
			panel = new JPanel();
			//panel.setLayout(new BorderLayout());
			panel1 = new JPanel();
			panel2 = new JPanel();
			panel3 = new JPanel();
			panel4 = new JPanel();
			label = new JLabel("Enter the row and column", JLabel.CENTER);
			label1 = new JLabel("Row��", JLabel.CENTER);
			label2 = new JLabel("Column��", JLabel.CENTER);
			label3 = new JLabel("Number of mines��", JLabel.CENTER);
			row = new JTextField();
			row.setText("16");
			row.setSize(2, 8);
			column = new JTextField();
			column.setText("16");
			mine = new JTextField();
			mine.setText("40");
			ȷ�� = new JButton("Confirm");
			ȷ��.addActionListener(this);
			ȡ�� = new JButton("Cancel");
			ȡ��.addActionListener(this);
			panel1.add(label1);
			panel1.add(row);
			panel2.add(label2);
			panel2.add(column);
			panel3.add(label3);
			panel3.add(mine);
			panel4.add(ȷ��);
			panel4.add(ȡ��);
			panel.add(panel1);
			panel.add(panel2);
			panel.add(panel3);
			set.add(label, BorderLayout.NORTH);
			set.add(panel, BorderLayout.CENTER);
			set.add(panel4, BorderLayout.SOUTH);
			set.setVisible(true);
		}
		if (e.getSource() == ȷ��) {
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
		if (e.getSource() == ȡ��) {
			set.setVisible(false);
		}
//		if (e.getSource() == ͸��) {
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
