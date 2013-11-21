package net.dtkanov.lac.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.dtkanov.lac.core.AppCore;
import net.dtkanov.lac.core.NoCardException;

public class ListControls extends JPanel {
	public ListControls(CardPanel cardPanel) {
		super();
		this.pCard = cardPanel;
		visitedCards = new boolean[AppCore.getInstance().getCurrentList().getSize()];
		for (int i = 0; i < visitedCards.length; ++i)
			visitedCards[i] = false;
			
		
		setLayout(new BorderLayout());
		
		bFirst = new JButton("<<");
		bFirst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pCard.setCard(AppCore.getInstance().getCurrentList().first());
				checkState();
			}
		});
		bLast = new JButton(">>");
		bLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pCard.setCard(AppCore.getInstance().getCurrentList().last());
				checkState();
			}
		});
		bPrev = new JButton("<");
		bPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					pCard.setCard(AppCore.getInstance().getCurrentList().prev());
				} catch (NoCardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkState();
			}
		});
		bNext = new JButton(">");
		bNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					pCard.setCard(AppCore.getInstance().getCurrentList().next());
				} catch (NoCardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkState();
			}
		});
		
		JPanel buttons = new JPanel();
		
		buttons.add(bFirst);
		buttons.add(bPrev);
		buttons.add(bNext);
		buttons.add(bLast);
		add(buttons, BorderLayout.CENTER);
		
		pbPerc = new JProgressBar(0, AppCore.getInstance().getCurrentList().getSize());
		pbPerc.setValue(0);
		pbPerc.setStringPainted(true);
		add(pbPerc, BorderLayout.SOUTH);
		
		checkState();
	}
	
	public void clearState() {
		visitedCards = new boolean[AppCore.getInstance().getCurrentList().getSize()];
		for (int i = 0; i < visitedCards.length; ++i)
			visitedCards[i] = false;
		pbPerc.setMaximum(AppCore.getInstance().getCurrentList().getSize());
		checkState();
	}
	
	public void checkState() {
		int idx = AppCore.getInstance().getCurrentList().getCurrCardIdx();
		if (idx == 0) {
			bFirst.setEnabled(false);
			bPrev.setEnabled(false);
		} else {
			bFirst.setEnabled(true);
			bPrev.setEnabled(true);
		}
		if (idx == AppCore.getInstance().getCurrentList().getSize()-1) {
			bLast.setEnabled(false);
			bNext.setEnabled(false);
		} else {
			bLast.setEnabled(true);
			bNext.setEnabled(true);
		}
		visitedCards[idx] = true;
		int numVisited = 0;
		for (boolean vc : visitedCards)
			if (vc)
				numVisited++;
		pbPerc.setValue(numVisited);
	}

	private JProgressBar pbPerc;
	private JButton bFirst;
	private JButton bLast;
	private JButton bNext;
	private JButton bPrev;
	private CardPanel pCard;
	private boolean visitedCards[];
	
	private static final long serialVersionUID = -4512466650810264484L;
}
