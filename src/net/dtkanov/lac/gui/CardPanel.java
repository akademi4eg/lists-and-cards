package net.dtkanov.lac.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.dtkanov.lac.core.Card;

public class CardPanel extends JPanel {
	public CardPanel(Card c) {
		super();
		isFlipable = true;
		setPreferredSize(new Dimension(600, 300));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		text = new JLabel();
		text.setFont(text.getFont().deriveFont(CARD_TEXT_SIZE));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		add(text, BorderLayout.CENTER);
		setCard(c);
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isFlipable)
					flip();
			}
		});
	}
	public void setFlipable(boolean isF) {
		isFlipable = isF;
	}
	public void setCard(Card c) {
		card = c;
		setSide1();
	}
	public void setSide1() {
		text.setText(card.getSide1());
		isSide1 = true;
		setBackground(Color.yellow);
		repaint();
	}
	public void setSide2() {
		text.setText(card.getSide2());
		isSide1 = false;
		setBackground(Color.white);
		repaint();
	}
	public void flip() {
		if (isSide1)
			setSide2();
		else
			setSide1();
	}
	
	private boolean isFlipable;
	private boolean isSide1;
	private Card card;
	private JLabel text;
	private static final long serialVersionUID = -5362125150033909667L;
	public static final float CARD_TEXT_SIZE = 60.0f;
}
