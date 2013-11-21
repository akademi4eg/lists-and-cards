package net.dtkanov.lac.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.dtkanov.lac.core.AppCore;
import net.dtkanov.lac.core.CardsList;
import net.dtkanov.lac.core.NoCardException;

public class WriteControl extends JPanel {
	public WriteControl(CardPanel cardPanel) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		pCard = cardPanel;
		tInput = new JTextField(INPUT_FIELD_LENGTH);
		tInput.setFont(tInput.getFont().deriveFont(INPUT_TEXT_SIZE));
		add(tInput);
		
		JPanel pCont = new JPanel();
		bCheck = new JButton(AppCore.getInstance().getString("Try"));
		bCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CardsList cl = AppCore.getInstance().getCurrentList();
				int idx = cl.getCurrCardIdx();
				String text = tInput.getText().trim();
				if (!text.isEmpty()) {
					if (text.equals(cl.getCard(idx).getSide2())) {
						if (idx == cl.getSize()-1) {
							JOptionPane.showMessageDialog(pCard, AppCore.getInstance().getString("ListComplete"));
						} else {
							try {
								pCard.setCard(AppCore.getInstance().getCurrentList().next());
								attemptNum = 0;
								tInput.setText("");
							} catch (NoCardException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						JOptionPane.showMessageDialog(pCard, AppCore.getInstance().getString("Wrong"));
						attemptNum++;
						errCnt++;
						if (attemptNum >= MAX_ATTEMPTS)
							pCard.setSide2();
					}
					updateStats();
				}
			}
		});
		pCont.add(bCheck);
		bNext = new JButton(AppCore.getInstance().getString("GiveUp"));
		bNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				errCnt++;
				pCard.setSide2();
				updateStats();
			}
		});
		pCont.add(bNext);
		lStats = new JLabel();
		updateStats();
		pCont.add(lStats);
		add(pCont);
	}
	public void updateStats() {
		CardsList cl = AppCore.getInstance().getCurrentList();
		int idx = cl.getCurrCardIdx();
		lStats.setText((idx+1)+" "+AppCore.getInstance().getString("From")
					 +" "+cl.getSize()+", "+AppCore.getInstance().getString("NumErrCurCard")
					 +": "+attemptNum+", "+AppCore.getInstance().getString("TotalErr")+": "+errCnt);
	}
	public void clearState() { 
		errCnt = 0;
		attemptNum = 0;
		tInput.setText("");
		updateStats();
	}
	
	private JLabel lStats;
	private JTextField tInput;
	private JButton bCheck;
	private JButton bNext;
	private CardPanel pCard;
	private int attemptNum = 0;
	private int errCnt = 0;
	private static final long serialVersionUID = 1897198859447998126L;
	
	public static final int INPUT_FIELD_LENGTH = 40;
	public static final int MAX_ATTEMPTS = 3;
	public static final float INPUT_TEXT_SIZE = 30.0f;
}
