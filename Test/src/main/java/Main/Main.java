package Main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
  static JFrame Frame;
  static JButton[] NumbersButtons;
  static int sleep; 
  static double height;
  public static void main(String[] argv) {
    Frame = new JIntoFrame();
  }
  static class JIntoFrame extends JFrame {
    JTextField NumbersText;
    JIntoFrame() {
      super();
      setExtendedState(JFrame.MAXIMIZED_BOTH);
      NumbersText = new JTextField(50);
      NumbersText.setMaximumSize(new Dimension(250, 150));
      NumbersText.setAlignmentX(CENTER_ALIGNMENT);
      JButton Enter = new JButton("Enter");
      Enter.setAlignmentX(CENTER_ALIGNMENT);
      JLabel Label = new JLabel("How many numbers to display?");
      Label.setAlignmentX(CENTER_ALIGNMENT);
      setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
      Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
      height = ScreenSize.getHeight();
      add(Box.createRigidArea(new Dimension(0, (int) height / 2)));
      add(Label);
      add(NumbersText);
      add(Enter);
      Enter.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Frame.dispose();
          Frame = new JSortFrame(Integer.parseInt(NumbersText.getText()));
          Frame.validate();
        }
      });
      add(Box.createVerticalGlue());
      setVisible(true);
    }
  }
  static class JSortFrame extends JFrame {
    boolean sort = false; 
    JSortFrame(int numbers) {
      super();
      setExtendedState(JFrame.MAXIMIZED_BOTH);
      //Создание правой колонки 
      JPanel RightPanel = new JPanel();
      RightPanel.setLayout(new BoxLayout(RightPanel, BoxLayout.Y_AXIS));
      JButton Sort = new JButton("Sort");
      JButton Reset = new JButton("Reset");
      Reset.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Frame.dispose();
          Frame = new JIntoFrame();
          Frame.validate();
        }
      });
      final JTextField SleepText = new JTextField(10);
      Sort.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (SleepText.getText().equals("")) sleep = 500;
          else sleep = Integer.parseInt(SleepText.getText()) * 1000;
          if (sort) qsortM(0, NumbersButtons.length - 1);
          else qsortB(0, NumbersButtons.length - 1);
          sort = true;
        }
      });
      RightPanel.add(Box.createRigidArea(new Dimension(0, (int) height / 2)));
      RightPanel.add(Sort);
      RightPanel.add(Reset);
      RightPanel.add(new JLabel("Enter speed show sort [1,30]"));
      RightPanel.add(new JLabel("int (default 0.5s)"));
      RightPanel.add(SleepText);
      RightPanel.add(Box.createRigidArea(new Dimension(0, (int) height / 2)));
      add(RightPanel, BorderLayout.EAST);
      // Создание центрального меню
      NumbersButtons = new JButton[numbers];
      JPanel CentrPanel = new JPanel();
      CentrPanel.setLayout(new GridBagLayout());
      GridBagConstraints Constraints = new GridBagConstraints();
      Constraints.insets = new Insets(5, 5, 5, 5);
      for (int i = 0; i < numbers; i++) {
        NumbersButtons[i] = new JButton();
        if (i == 0) NumbersButtons[i].setText(Integer.toString((int)(Math.random() * 30)));
        else NumbersButtons[i].setText(Integer.toString((int)(Math.random() * 1000)));
        Constraints.gridy = i % 10;
        Constraints.gridx = i / 10;
        NumbersButtons[i].setPreferredSize(new Dimension(60, 30));
        NumbersButtons[i].addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            JButton Button = (JButton) e.getSource();
            if (Integer.parseInt(Button.getText()) < 30) Button.setText(Integer.toString((int)(Math.random() * 1000)));
            else new JOptionPane().showMessageDialog(Frame, "Please select a value smaller or equal to 30");;
          }
        });
        CentrPanel.add(NumbersButtons[i], Constraints);
      }
      add(CentrPanel);
      setVisible(true);
    }
  }
  private static void qsortB(final int left, final int right) {
    SwingWorker < Boolean, Integer > Worker = new SwingWorker < Boolean, Integer > () {
      @Override
      protected Boolean doInBackground() throws Exception {
        int l = left;
        int r = right;
        int pivot = Integer.parseInt(NumbersButtons[(left + right) / 2].getText());
        while (l <= r) {
          while (Integer.parseInt(NumbersButtons[l].getText()) > pivot) l++;
          while (Integer.parseInt(NumbersButtons[r].getText()) < pivot) r--;
          if (l <= r) {
            if (Integer.parseInt(NumbersButtons[l].getText()) < Integer.parseInt(NumbersButtons[r].getText())) {
              publish(l);
              publish(r);
              Thread.sleep(sleep);
              getState();
            }
            l++;
            r--;
          }
        }
        if (left < r) qsortB(left, r);
        if (l < right) qsortB(l, right);
        return true;
      }
      protected void process(List < Integer > chunks) {
        String s = NumbersButtons[chunks.get(chunks.size() - 1)].getText();
        NumbersButtons[chunks.get(chunks.size() - 1)].setText(NumbersButtons[chunks.get(chunks.size() - 2)].getText());
        NumbersButtons[chunks.get(chunks.size() - 2)].setText(s);
      }
    };
    Worker.execute();
  }
  private static void qsortM(final int left, final int right) {
    SwingWorker < Boolean, Integer > Worker = new SwingWorker < Boolean, Integer > () {
      @Override
      protected Boolean doInBackground() throws Exception {
        int l = left;
        int r = right;
        int pivot = Integer.parseInt(NumbersButtons[(left + right) / 2].getText());
        while (l <= r) {
          while (Integer.parseInt(NumbersButtons[l].getText()) < pivot) l++;
          while (Integer.parseInt(NumbersButtons[r].getText()) > pivot) r--;
          if (l <= r) {
            if (Integer.parseInt(NumbersButtons[l].getText()) > Integer.parseInt(NumbersButtons[r].getText())) {
              publish(l);
              publish(r);
              Thread.sleep(sleep);
              getState();
            }
            l++;
            r--;
          }
        }
        if (left < r) qsortM(left, r);
        if (l < right) qsortM(l, right);
        return true;
      }
      protected void process(List < Integer > chunks) {
        String s = NumbersButtons[chunks.get(chunks.size() - 1)].getText();
        NumbersButtons[chunks.get(chunks.size() - 1)].setText(NumbersButtons[chunks.get(chunks.size() - 2)].getText());
        NumbersButtons[chunks.get(chunks.size() - 2)].setText(s);
      }
    };
    Worker.execute();
  }
}