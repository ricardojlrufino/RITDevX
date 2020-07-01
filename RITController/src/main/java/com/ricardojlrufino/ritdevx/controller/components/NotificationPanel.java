package com.ricardojlrufino.ritdevx.controller.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;

public class NotificationPanel extends JPanel{
  
  private JLabel label = new JLabel();
  private Timer timer;
  
  public NotificationPanel() {
    super(new BorderLayout());
    
//    setPreferredSize(new Dimension(300, 80));
    setSize(new Dimension(150, 30));
    setBackground(new Color(0, 0, 0, 150));
    this.add(label, BorderLayout.CENTER);
  }
  
  public void showNotification(String text, int delay) {
    label.setText(text);
    
    
    this.setVisible(true);
    UIHelper.repaintParent(this);
    
    if(timer != null && timer.isRunning()) timer.stop();
    
    timer = new Timer(delay, new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        NotificationPanel.this.setVisible(false);
      }
    });
    
    timer.start();
    
  }
  


}
