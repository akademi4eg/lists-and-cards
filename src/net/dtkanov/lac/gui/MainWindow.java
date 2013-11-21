package net.dtkanov.lac.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.dtkanov.lac.core.AppCore;
import net.dtkanov.lac.core.CardsList;

public class MainWindow extends JFrame {
	public MainWindow() {
		setTitle("Lists and Cards");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initComponents();
		setLocationRelativeTo(null);
	}
	
	private void initComponents() {
		CardsList cl = AppCore.getInstance().getCurrentList();
		
		// overview
		pOverview = new JPanel(new BorderLayout());
		lLangs = new JLabel(AppCore.getInstance().getString("List")+": " + AppCore.getInstance().getLangFromCode(cl.getLang1())
									  + "-" + AppCore.getInstance().getLangFromCode(cl.getLang2())
									  + ", " + cl.getName());
		pOverview.add(lLangs, BorderLayout.NORTH);
		
		pCard = new CardPanel(cl.first());
		pOverview.add(pCard, BorderLayout.CENTER);
		
		pCtrl = new ListControls(pCard);
		pOverview.add(pCtrl, BorderLayout.SOUTH);
		
		// general
		pGeneral = new JPanel(new BorderLayout());
		lListInfo = new JLabel(String.format("<html>"+AppCore.getInstance().getString("List")+": %s<br/>"+
											 AppCore.getInstance().getString("SourceLang")+": %s<br/>"+
											 AppCore.getInstance().getString("TargetLang")+": %s<br/>"+
											 AppCore.getInstance().getString("CardsNum")+": %d</html>", cl.getName(),
											 AppCore.getInstance().getLangFromCode(cl.getLang1()),
											 AppCore.getInstance().getLangFromCode(cl.getLang2()), cl.getSize()));
		lListInfo.setHorizontalAlignment(SwingConstants.CENTER);
		pGeneral.add(lListInfo, BorderLayout.CENTER);
		JPanel pGenButtons = new JPanel();
		bSwitchListLangs = new JButton(AppCore.getInstance().getString("SwitchLangs"));
		bSwitchListLangs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AppCore.getInstance().getCurrentList().swap();
				updateComponents();
			}
		});
		pGenButtons.add(bSwitchListLangs);
		bOpenList = new JButton(AppCore.getInstance().getString("OpenList")+"...");
		bOpenList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (fcOpen == null) {
					fcOpen = new JFileChooser(new File("."));
					fcOpen.setFileFilter(new FileNameExtensionFilter(AppCore.getInstance().getString("CardsLists"), "lcl"));
					fcOpen.setAcceptAllFileFilterUsed(false);
				}
				int returnVal = fcOpen.showOpenDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					AppCore.getInstance().setCurrentList(fcOpen.getSelectedFile());
					updateComponents();
				}
			}
		});
		pGenButtons.add(bOpenList);
		bVers = new JButton(AppCore.getInstance().getString("Version")+"...");
		bVers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this,
						"<html>Questions and comments can be sent to Dmytro Tkanov " +
						"&lt;akademi4eg@gmail.com&gt;."+
						"<br/>Lists and Cards. "+AppCore.getInstance().getString("Version")+": "+AppCore.VERSION+"</html>");
			}
		});
		pGenButtons.add(bVers);
		pGeneral.add(pGenButtons, BorderLayout.SOUTH);
		Vector<CardsList> lists = new Vector<CardsList>(AppCore.getInstance().getLists());
		listOfLists = new JList<CardsList>(lists);
		listOfLists.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bOpenFromList = new JButton(AppCore.getInstance().getString("Open"));
		bOpenFromList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppCore.getInstance().setCurrentList(listOfLists.getSelectedIndex());
				updateComponents();
			}
		});
		bUpdate = new JButton(AppCore.getInstance().getString("Refresh"));
		bUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AppCore.getInstance().reloadLists();
				updateComponents();
			}
		});
		pLists = new JPanel();
		spLists = new JScrollPane(listOfLists);
		pLists.setLayout(new BoxLayout(pLists, BoxLayout.Y_AXIS));
		pLists.add(spLists);
		JPanel pListsButtons = new JPanel();
		pListsButtons.add(bOpenFromList);
		pListsButtons.add(bUpdate);
		bCreateList = new JButton(AppCore.getInstance().getString("CreateList")+"...");
		bCreateList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (winCL == null)
					winCL = new CreateListWindow();
				//TODO maybe use invokeLater call here
				winCL.setVisible(true);
			}
		});
		pListsButtons.add(bCreateList);
		pLists.add(pListsButtons);
		pGeneral.add(pLists, BorderLayout.EAST);
		
		// write it
		pWrite = new JPanel(new BorderLayout());
		lLangsW = new JLabel(AppCore.getInstance().getString("List")+": " + AppCore.getInstance().getLangFromCode(cl.getLang1())
				  + "-" + AppCore.getInstance().getLangFromCode(cl.getLang2())
				  + ", " + cl.getName());
		pWrite.add(lLangsW, BorderLayout.NORTH);
		pCardW = new CardPanel(cl.first());
		pCardW.setFlipable(false);
		pWrite.add(pCardW, BorderLayout.CENTER);
		pCtrlW = new WriteControl(pCardW);
		pWrite.add(pCtrlW, BorderLayout.SOUTH);
		
		// tabs
		tabs = new JTabbedPane();
		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateComponents();
			}
		});
		tabs.addTab(AppCore.getInstance().getString("Overview"), pGeneral);
		tabs.addTab(AppCore.getInstance().getString("Freestyle"), pOverview);
		tabs.addTab(AppCore.getInstance().getString("Writing"), pWrite);
		
		add(tabs);
		pack();
	}
	
	public void updateComponents() {
		CardsList cl = AppCore.getInstance().getCurrentList();
		lLangs.setText(AppCore.getInstance().getString("List")+": " + AppCore.getInstance().getLangFromCode(cl.getLang1())
								  + "-" + AppCore.getInstance().getLangFromCode(cl.getLang2())
								  + ", " + cl.getName());
		lLangsW.setText(AppCore.getInstance().getString("List")+": " + AppCore.getInstance().getLangFromCode(cl.getLang1())
				  + "-" + AppCore.getInstance().getLangFromCode(cl.getLang2())
				  + ", " + cl.getName());
		pCardW.setCard(cl.first());
		pCtrlW.clearState();
		lListInfo = new JLabel(String.format("<html>"+AppCore.getInstance().getString("List")+": %s<br/>"+
											 AppCore.getInstance().getString("SourceLang")+": %s<br/>"+
											 AppCore.getInstance().getString("TargetLang")+": %s<br/>"+
											 AppCore.getInstance().getString("CardsNum")+": %d</html>", cl.getName(),
											 AppCore.getInstance().getLangFromCode(cl.getLang1()),
											 AppCore.getInstance().getLangFromCode(cl.getLang2()), cl.getSize()));
		pCard.setCard(cl.first());
		pCtrl.clearState();
		listOfLists.setListData(new Vector<CardsList>(AppCore.getInstance().getLists()));
		spLists.revalidate();
		spLists.repaint();
	}
	
	private JScrollPane spLists;
	private JPanel pLists;
	private JButton bOpenFromList;
	private JButton bUpdate;
	private JList<CardsList> listOfLists;
	private JButton bVers;
	private JFileChooser fcOpen;
	private JButton bOpenList;
	private JButton bCreateList;
	private JButton bSwitchListLangs;
	private JLabel lListInfo;
	private JLabel lLangs;
	private JLabel lLangsW;
	private CardPanel pCard;
	private CardPanel pCardW;
	private WriteControl pCtrlW;
	private ListControls pCtrl;
	private JPanel pOverview;
	private JPanel pGeneral;
	private JPanel pWrite;
	private JTabbedPane tabs;
	private CreateListWindow winCL;
	
	private static final long serialVersionUID = 468204462342927503L;
}
