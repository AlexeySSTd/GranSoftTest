package Main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Main {
	static JFrame frame;
	static JButton[] NumbersButton;
	static int sleep;
	public static void main(String[] argv) {
		 frame = new JIntoFrame();
	}
	static class JIntoFrame extends JFrame {
		JTextField NumbersText;
	JIntoFrame(){
		super();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		NumbersText = new JTextField(50);
		JButton enter = new JButton("Enter");
		enter.setAlignmentX(CENTER_ALIGNMENT);
		JLabel label = new JLabel("How many numbers to display?");
		label.setAlignmentX(CENTER_ALIGNMENT);
		setLayout(new BoxLayout(getContentPane(),BoxLayout.PAGE_AXIS));
		NumbersText.setAlignmentX((float)0.5);
		NumbersText.setMaximumSize(new Dimension(250,150));
		NumbersText.setAlignmentX(CENTER_ALIGNMENT);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		add(Box.createRigidArea(new Dimension(0,(int)height/2)));
		add(label);
		add(NumbersText);
		add(enter);
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				frame = new JSortFrame(Integer.parseInt(NumbersText.getText()));
				frame.validate();
			} 
		});
		add(Box.createVerticalGlue());
		setVisible(true);
	}
	}
	static class JSortFrame extends JFrame {
		boolean sort= false;
		JSortFrame(int numbers){
			super();
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			//Создание правой колонки 
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

			JButton Sort = new JButton("Sort");
			JButton Reset = new JButton("Reset");
			Reset.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
					frame = new JIntoFrame(); 
					frame.validate();
				}
			});
			final JTextField sleepText = new JTextField(10);
			Sort.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (sleepText.getText().equals("")) sleep = 500; else  sleep = Integer.parseInt(sleepText.getText())*1000;
					if (sort) qsortM(0,NumbersButton.length-1); else qsortB(0,NumbersButton.length-1);
					sort = true;
				}
			});
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double height = screenSize.getHeight();
			rightPanel.add(Box.createRigidArea(new Dimension(0,(int)height/2)));
			rightPanel.add(Sort);
			rightPanel.add(Reset);
			rightPanel.add(new JLabel("Enter speed show sort [1,30]"));
			rightPanel.add(new JLabel("int (default 0.5s)"));
			rightPanel.add(sleepText);
			rightPanel.add(Box.createRigidArea(new Dimension(0,(int)height/2)));
			add(rightPanel,BorderLayout.EAST);
			// Создание центрального меню
			NumbersButton = new JButton[numbers];
			JPanel centrPanel = new JPanel();
			centrPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints(); 
			constraints.insets    = new Insets(5, 5, 5, 5); 
			for (int i = 0; i<numbers;i++) {
				NumbersButton[i] = new JButton();
				if (i==0) NumbersButton[i].setText(Integer.toString((int)(Math.random()*30))); 
				else NumbersButton[i].setText(Integer.toString((int)(Math.random()*1000))); 
				constraints.gridy = i%10;
				constraints.gridx = i/10;
				NumbersButton[i].setPreferredSize(new Dimension(60,30));
				
				NumbersButton[i].addActionListener(new ActionListener() {	
					public void actionPerformed(ActionEvent e) {
						JButton button = (JButton) e.getSource();
						if (Integer.parseInt(button.getText())<30) button.setText(Integer.toString((int)(Math.random()*1000))); 
						else new JOptionPane().showMessageDialog(frame,"Please select a value smaller or equal to 30");;
					}
				});
				centrPanel.add(NumbersButton[i],constraints);
			}
			add(centrPanel);
			setVisible(true);
		}
	}
	private static void qsortB(final int left,final int right) {
		SwingWorker<Boolean,Integer> worker = new SwingWorker<Boolean,Integer>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				int l = left;
				int r = right;
				int pivot = Integer.parseInt(NumbersButton[(left+right)/2].getText());
				while (l<=r) {
					while (Integer.parseInt(NumbersButton[l].getText())>pivot)l++;
					while (Integer.parseInt(NumbersButton[r].getText())<pivot)r--;
					if (l<=r) {
						if (Integer.parseInt(NumbersButton[l].getText())<Integer.parseInt(NumbersButton[r].getText())) {
						publish(l);
						publish(r);
						Thread.sleep(sleep);
						getState();
						}
					l++;
					r--;
					}
				}
				if (left<r) qsortB(left,r);
				if (l<right) qsortB(l,right);
				return true;
			}
			protected void process(List<Integer> chunks) {
				String s = NumbersButton[chunks.get(chunks.size()-1)].getText();
				NumbersButton[chunks.get(chunks.size()-1)].setText(NumbersButton[chunks.get(chunks.size()-2)].getText());
				NumbersButton[ chunks.get(chunks.size()-2)].setText(s);
			}
		};
		worker.execute();
	}
	private static void qsortM(final int left,final int right) {
		SwingWorker<Boolean,Integer> worker = new SwingWorker<Boolean,Integer>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				int l = left;
				int r = right;
				int pivot = Integer.parseInt(NumbersButton[(left+right)/2].getText());
				while (l<=r) {
					while (Integer.parseInt(NumbersButton[l].getText())<pivot)l++;
					while (Integer.parseInt(NumbersButton[r].getText())>pivot)r--;
					if (l<=r) {
						if (Integer.parseInt(NumbersButton[l].getText())>Integer.parseInt(NumbersButton[r].getText())) {
						publish(l);
						publish(r);
						Thread.sleep(sleep);
						getState();
						}
					l++;
					r--;
					}
				}
				if (left<r) qsortM(left,r);
				if (l<right) qsortM(l,right);
				return true;
			}
			protected void process(List<Integer> chunks) {
				String s = NumbersButton[chunks.get(chunks.size()-1)].getText();
				NumbersButton[chunks.get(chunks.size()-1)].setText(NumbersButton[chunks.get(chunks.size()-2)].getText());
				NumbersButton[ chunks.get(chunks.size()-2)].setText(s);
			}
		};
		worker.execute();
	}
}
