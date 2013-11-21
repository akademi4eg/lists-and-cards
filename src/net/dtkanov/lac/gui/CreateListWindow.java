package net.dtkanov.lac.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.dtkanov.lac.core.AppCore;
import net.dtkanov.lac.core.Card;
import net.dtkanov.lac.core.CardsList;

public class CreateListWindow extends JFrame {
	public CreateListWindow() {
		super();
		cl = new CardsList();
		
		setResizable(false);
		setTitle(AppCore.getInstance().getString("ListCreation"));
		JPanel form = new JPanel();
		GroupLayout layout = new GroupLayout(form);
		form.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		lName = new JLabel(AppCore.getInstance().getString("Name"));
		tName = new JTextField(TEXT_LEN);
		lName.setLabelFor(tName);
		
		Vector<String> langs = AppCore.getInstance().getLangs();
		lang1 = new JComboBox<String>(langs);
		lang2 = new JComboBox<String>(langs);
		lLang1 = new JLabel(AppCore.getInstance().getString("Lang")+" 1:");
		lLang1.setLabelFor(lang1);
		lLang2 = new JLabel(AppCore.getInstance().getString("Lang")+" 2:");
		lLang2.setLabelFor(lang2);
		
		lSide1 = new JLabel(AppCore.getInstance().getString("Side")+" 1:");
		tSide1 = new JTextField(TEXT_LEN);
		lSide1.setLabelFor(tSide1);
		lSide2 = new JLabel(AppCore.getInstance().getString("Side")+" 2:");
		tSide2 = new JTextField(TEXT_LEN);
		lSide2.setLabelFor(tSide2);
		
		lCards = new JList<Card>();
		spCards = new JScrollPane(lCards);
		
		JPanel pButtons = new JPanel();
		bAddCard = new JButton(AppCore.getInstance().getString("AddCard"));
		bAddCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s1 = tSide1.getText().trim();
				String s2 = tSide2.getText().trim();
				if (!s1.isEmpty() && !s2.isEmpty()) {
					cl.addCard(s1, s2);
					tSide1.setText("");
					tSide2.setText("");
					updateComponents();
				} else {
					JOptionPane.showMessageDialog(CreateListWindow.this, AppCore.getInstance().getString("ErrBothSides"));
				}
			}
		});
		pButtons.add(bAddCard);
		bDelCard = new JButton(AppCore.getInstance().getString("DelCard"));
		bDelCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int idxs[] = lCards.getSelectedIndices();
				if (idxs.length>0) {
					Arrays.sort(idxs);
					for (int i = idxs.length-1; i >= 0; i--) {
						if (cl.getCards().size() > idxs[i])
							cl.getCards().remove(idxs[i]);
					}
					updateComponents();
				}
			}
		});
		pButtons.add(bDelCard);
		bSave = new JButton("Сохранить...");
		bSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = tName.getText().trim();
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(CreateListWindow.this, AppCore.getInstance().getString("ErrAddName"));
					return;
				}
				String l1 = AppCore.getInstance().getCodeForLang(lang1.getSelectedItem().toString());
				String l2 = AppCore.getInstance().getCodeForLang(lang2.getSelectedItem().toString());
				cl.setName(name);
				cl.setLang1(l1);
				cl.setLang2(l2);
				cl.generateID();
				
				if (fcSave == null) {
					fcSave = new JFileChooser(new File("."));
					fcSave.setFileFilter(new FileNameExtensionFilter(AppCore.getInstance().getString("CardsLists"), "lcl"));
					fcSave.setAcceptAllFileFilterUsed(false);
				}
				int returnVal = fcSave.showSaveDialog(CreateListWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dst = fcSave.getSelectedFile();
					if (!dst.getName().endsWith(".lcl")) {
						dst = new File(dst.getPath()+dst.getName()+".lcl");
					}
					CardsList.save(cl, dst);
					JOptionPane.showMessageDialog(CreateListWindow.this, AppCore.getInstance().getString("Saved"));
				}
			}
		});
		pButtons.add(bSave);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
					layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(lName)
								.addComponent(lLang1)
								.addComponent(lLang2)
								.addComponent(lSide1)
								.addComponent(lSide2)
							)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(tName)
								.addComponent(lang1)
								.addComponent(lang2)
								.addComponent(tSide1)
								.addComponent(tSide2)
							)
					)
				.addComponent(spCards)
				.addComponent(pButtons)
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lName)
							.addComponent(tName)
						)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lLang1)
							.addComponent(lang1)
						)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lLang2)
							.addComponent(lang2)
						)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lSide1)
							.addComponent(tSide1)
						)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(lSide2)
							.addComponent(tSide2)
						)
					.addComponent(spCards)
					.addComponent(pButtons)
			);
		
		add(form);
		pack();
	}
	
	public void updateComponents() {
		lCards.setListData(new Vector<Card>(cl.getCards()));
		spCards.revalidate();
		spCards.repaint();
	}
	
	private CardsList cl;
	private JFileChooser fcSave;
	private JButton bSave;
	private JButton bAddCard;
	private JButton bDelCard;
	private JList<Card> lCards;
	private JScrollPane spCards;
	private JLabel lSide1;
	private JLabel lSide2;
	private JTextField tSide1;
	private JTextField tSide2;
	private JLabel lLang1;
	private JLabel lLang2;
	private JComboBox<String> lang1;
	private JComboBox<String> lang2;
	private JLabel lName;
	private JTextField tName;
	private static final long serialVersionUID = -8469451750514849221L;
	public static final int TEXT_LEN = 30;
}
