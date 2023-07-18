package ɨ��.src.cn.com.mine;

import javax.swing.*;
import java.awt.*;

public class BlockView extends JPanel {
	JLabel blockNameOrIcon; // ������ʾBlock�����name��number��mineIcon����
	JButton blockCover; // �����ڵ�blockNameOrIcon.
	CardLayout card; // ��Ƭʽ���֣���ʾ��һ����ӵ����
	ImageIcon icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8;
	
	public BlockView() {
		card = new CardLayout();
		setLayout(card);
		blockNameOrIcon = new JLabel("", JLabel.CENTER);
		blockNameOrIcon.setHorizontalTextPosition(AbstractButton.CENTER);
		blockNameOrIcon.setVerticalTextPosition(AbstractButton.CENTER);
		blockCover = new JButton();
		add("cover", blockCover);
		add("view", blockNameOrIcon);
	}

	public void giveView(Block block) {
		if (block.isMine) {
			blockNameOrIcon.setIcon(block.getMineIcon());
		} else {
			int n = block.getAroundMineNumber();
			if (n == 1) {
				icon1 = new ImageIcon("src/1.jpg");
				blockNameOrIcon.setIcon(icon1);
			}
			if (n == 2) {
				icon2 = new ImageIcon("src/2.jpg");
				blockNameOrIcon.setIcon(icon2);
			}
			if (n == 3) {
				icon3 = new ImageIcon("src/3.jpg");
				blockNameOrIcon.setIcon(icon3);
			}
			if (n == 4) {
				icon4 = new ImageIcon("src/4.jpg");
				blockNameOrIcon.setIcon(icon4);
			}
			if (n == 5) {
				icon5 = new ImageIcon("src/5.jpg");
				blockNameOrIcon.setIcon(icon5);
			}
			if (n == 6) {
				icon6 = new ImageIcon("src/6.jpg");
				blockNameOrIcon.setIcon(icon6);
			}
			if (n == 7) {
				icon7 = new ImageIcon("src/7.jpg");
				blockNameOrIcon.setIcon(icon7);
			}
			if (n == 8) {
				icon8 = new ImageIcon("src/8.jpg");
				blockNameOrIcon.setIcon(icon8);
			}
			else
				blockNameOrIcon.setText(" ");
		}
	}

	public void seeBlockNameOrIcon() {
		card.show(this, "view"); // ��ת��ָ�������
		validate();
	}
	public void seeVisibleBlockNameOrIcon(){

	}
	public void seeBlockCover() {
		card.show(this, "cover");
		validate();
	}

	public JButton getBlockCover() {
		return blockCover;
	}
}
