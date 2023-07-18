package ɨ��.src.cn.com.mine;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;

public class MineArea extends JPanel implements ActionListener, MouseListener, Serializable {
	private static final long serialVersionUID = 715919700682963666L;

	JButton reStart;

	Block[][] block;
	BlockView[][] blockView;
	LayMines lay;
	int row, column, mineCount, markMount;// �����������������Լ����׸������û������ı����
	ImageIcon mark;
	JPanel pCenter, pNorth;
	JTextField showTime, showMarkedMineCount; // ��ʾ��ʱ�Լ������
	Timer time; // ��ʱ��
	int spendTime = 0;
	JDialog lose = new JDialog();//�ж���Ӯ�ķ�������������������������
	JPanel panel;
	JLabel str;	
	JButton ���¿�ʼ = new JButton("Restart");
	JButton	cancel = new JButton("Cancel");

	public MineArea(int row, int column, int mineCount) {
		ImageIcon reStartIcon = new ImageIcon("src/houzi.png");
		reStart = new JButton(reStartIcon);
		mark = new ImageIcon("src/mark.png"); // �ױ��
		time = new Timer(1000, this);//ÿ��һ���һ��ʱ��
		showTime = new JTextField(5);
		showMarkedMineCount = new JTextField(5);
		showTime.setHorizontalAlignment(JTextField.CENTER);
		showTime.setFont(new Font("Arial", Font.BOLD, 16));
		showTime.setDisabledTextColor(java.awt.Color.black); // ����������ɫ
		showTime.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray));// ���ñ߿���ɫ
		showTime.setEnabled(false);// ���ò�����Ӧ�û�����
		showMarkedMineCount.setHorizontalAlignment(JTextField.CENTER);
		showMarkedMineCount.setFont(new Font("Arial", Font.BOLD, 16));
		showMarkedMineCount.setDisabledTextColor(java.awt.Color.black);
		showMarkedMineCount.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray));
		showMarkedMineCount.setEnabled(false);
		pCenter = new JPanel();
		pNorth = new JPanel();
		lay = new LayMines();

		initMineArea(row, column, mineCount); // ��ʼ������,�������LayMines()
		reStart.addActionListener(this);
		pNorth.add(showMarkedMineCount);
		pNorth.add(reStart);
		pNorth.add(showTime);
		setLayout(new BorderLayout());
		add(pNorth, BorderLayout.NORTH);
		add(pCenter, BorderLayout.CENTER);
	}

	public void initMineArea(int row, int column, int mineCount) {
		pCenter.removeAll();
		spendTime = 0;
		markMount = mineCount;
		this.row = row;
		this.column = column;
		this.mineCount = mineCount;

		block = new Block[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++)
				block[i][j] = new Block();
		}
		blockView = new BlockView[row][column];
		pCenter.setLayout(new GridLayout(row, column));
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				blockView[i][j] = new BlockView();
				pCenter.add(blockView[i][j]);
				blockView[i][j].getBlockCover().addActionListener(this);
				blockView[i][j].getBlockCover().addMouseListener(this);
				blockView[i][j].seeBlockCover();
				blockView[i][j].getBlockCover().setEnabled(true);
				blockView[i][j].getBlockCover().setIcon(null);
			}
		}
		showMarkedMineCount.setText("" + markMount);
		validate();
	}
//	public void initVisibleMineArea(int row, int column, int mineCount) {
//		pCenter.removeAll();
//		spendTime = 0;
//		markMount = mineCount;
//		this.row = row;
//		this.column = column;
//		this.mineCount = mineCount;
//
//		block = new Block[row][column];
//		for (int i = 0; i < row; i++) {
//			for (int j = 0; j < column; j++)
//				block[i][j] = new Block();
//		}
//		//lay.layMinesForBlock(block, mineCount);
//		blockView = new BlockView[row][column];
//		pCenter.setLayout(new GridLayout(row, column));
//		for (int i = 0; i < row; i++) {
//			for (int j = 0; j < column; j++) {
//				blockView[i][j] = new BlockView();
//				//blockView[i][j].giveView(block[i][j]); // ��block[i][j]�ṩ��ͼ
//				pCenter.add(blockView[i][j]);
//				blockView[i][j].getBlockCover().addActionListener(this);
//				blockView[i][j].getBlockCover().addMouseListener(this);
//				blockView[i][j].seeBlockCover();//������������������������������������������������������������ĳɿɿ��������ס�����������������������������������������������������������������������������������������������������������������������������������������������
//				blockView[i][j].getBlockCover().setEnabled(true);
//				blockView[i][j].getBlockCover().setIcon(null);
//			}
//		}
//		showMarkedMineCount.setText("" + markMount);
//		validate();
//	}
	public void initMine(int m, int n){
		lay.layMinesForBlock(block, mineCount, m, n);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				blockView[i][j].giveView(block[i][j]);
			}
		}
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setMineCount(int mineCount) {
		this.mineCount = mineCount;
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ���¿�ʼ) {
			initMineArea(row, column, mineCount);
			lose.dispose();
			return;
		}
		if (e.getSource() == cancel){
			lose.dispose();
			return;
		}
		int temp = 0;
		if (e.getSource() != reStart && e.getSource() != time) {
			time.start();
			int m = -1, n = -1;
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					if(block[i][j].getIsMine())
						temp = -1;
					if (e.getSource() == blockView[i][j].getBlockCover()) {
						m = i;
						n = j;
					}
				}
			}
			
			if(temp == 0)
				initMine(m, n);
			
			if (block[m][n].getIsMine()) {
				for (int i = 0; i < row; i++) {
					for (int j = 0; j < column; j++) {
						blockView[i][j].getBlockCover().setEnabled(false);
						if (block[i][j].getIsMine())
							blockView[i][j].seeBlockNameOrIcon();
					}
				}
				panel = new JPanel();
				panel.setLayout(new FlowLayout());
				str = new JLabel("������Ŷ��", JLabel.CENTER);
				//restart1 = new JButton("���¿�ʼ");
				���¿�ʼ.addActionListener(this);
				//cancel = new JButton("ȡ��");
				cancel.addActionListener(this);
				lose.setTitle("����");
				lose.setBounds(300,100,200,150);
				lose.setResizable(false);
				lose.setVisible(false);
				lose.setModal(true);
				time.stop();
				spendTime = 0;
				markMount = mineCount;
				lose.add(str, BorderLayout.CENTER);
				panel.add(���¿�ʼ);
				panel.add(cancel);
				lose.add(panel, BorderLayout.SOUTH);
				lose.setVisible(true);
			} else {
				show(m, n); // ����������show����
			}
		}
		if (e.getSource() == reStart) {
			initMineArea(row, column, mineCount);
		}
		if (e.getSource() == time) {
			spendTime++;
			showTime.setText("" + spendTime);
		}
		inquireWin();
	}

	public void show(int m, int n) {
		if (block[m][n].getAroundMineNumber() > 0 && !block[m][n].getIsOpen()) {
			blockView[m][n].seeBlockNameOrIcon();
			block[m][n].setIsOpen(true);
		} else if (block[m][n].getAroundMineNumber() == 0 && !block[m][n].getIsOpen()) {
			blockView[m][n].seeBlockNameOrIcon();
			block[m][n].setIsOpen(true);
			for (int k = Math.max(m - 1, 0); k <= Math.min(m + 1, row - 1); k++) {
				for (int t = Math.max(n - 1, 0); t <= Math.min(n + 1, column - 1); t++)
					show(k, t);
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		JButton source = (JButton) e.getSource();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				if (e.getModifiers() == InputEvent.BUTTON3_MASK && source == blockView[i][j].getBlockCover()) {
					if (block[i][j].getIsMark()) {
						source.setIcon(null);
						block[i][j].setIsMark(false);
						markMount = markMount + 1;
					} else {
						source.setIcon(mark);
						block[i][j].setIsMark(true);
						markMount = markMount - 1;
					}
					showMarkedMineCount.setText("" + markMount);
				}
			}
		}
	}

	public void inquireWin() {
		int number = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				if (!block[i][j].getIsOpen())
					number++;
			}
		}
//		if (number == )
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}
}
