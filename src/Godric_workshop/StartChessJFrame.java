package Godric_workshop;  
import java.awt.event.*;  
import java.awt.*;  
  
import javax.swing.*;  
/* 
StartChessFrame
 */  
public class StartChessJFrame extends JFrame {  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ChessBoard chessBoard;  
	private JPanel toolbar;  
	private JButton startButton,backButton,exitButton;  
    
	//MenuBar, Menu and MenuItem
	private JMenuBar menuBar;  
	private JMenu sysMenu;  
	private JMenuItem startMenuItem,exitMenuItem,backMenuItem;  
  
	public StartChessJFrame(){  
      setTitle("Gomoku");  
      chessBoard=new ChessBoard();
      this.setResizable(false);
      this.setBounds(200, 200, 900, 900);
        
//      Container contentPane=getContentPane();  
//      contentPane.add(chessBoard);  
//      chessBoard.setOpaque(true);  
      
        
      //menuBar, menu and menuItem
      menuBar =new JMenuBar();
      sysMenu=new JMenu("System");
      
      //menu item
      startMenuItem=new JMenuItem("Restart");  
      exitMenuItem =new JMenuItem("Exit");  
      backMenuItem =new JMenuItem("UnDo");  
      //add menuItems
      sysMenu.add(startMenuItem);  
      sysMenu.add(exitMenuItem);  
      sysMenu.add(backMenuItem);  
      //ActionListener implementation subClass object
      MyItemListener lis=new MyItemListener();  
      //menuItems addActionListeners
      startMenuItem.addActionListener(lis);  
      backMenuItem.addActionListener(lis);  
      exitMenuItem.addActionListener(lis);
      
      //add menuItems to menu and add menu to menuBar
      menuBar.add(sysMenu);  
      setJMenuBar(menuBar); 
        
      //initialize toolBar(JPanel)
      toolbar=new JPanel();
      //initialize buttons 
      startButton=new JButton("Restart");  
      exitButton=new JButton("Exit");  
      backButton=new JButton("UnDo");  
      chessBoard.label = new JLabel("		Black's Turn!");
      chessBoard.label.setFont(new Font("Batang",Font.BOLD ,15));
      //toolBar use FlowLayout  
      toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));  
      //add buttons and label to toolBar(JPanel) 
      toolbar.add(startButton);  
      toolbar.add(exitButton);  
      toolbar.add(backButton);   
      toolbar.add(chessBoard.label);
      //Buttons add ActionListener
      startButton.addActionListener(lis);  
      exitButton.addActionListener(lis);  
      backButton.addActionListener(lis);  
      //add toolBar to Frame
      add(toolbar,BorderLayout.SOUTH);
      //add chessBoard to Frame
      add(chessBoard);  
      //set Frame close Operation 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
      pack();
        
  }  
    
  private class MyItemListener implements ActionListener{  
      public void actionPerformed(ActionEvent e){  
          //Since different buttons add the same ActionListener, Source is needed.
    	  Object obj=e.getSource();
          if(obj==StartChessJFrame.this.startMenuItem||obj==startButton){  
              //restart            
              System.out.println("Restart");  
              chessBoard.restartGame();  
          }  
          else if (obj==exitMenuItem||obj==exitButton)  
              System.exit(0);  
          else if (obj==backMenuItem||obj==backButton){  
              System.out.println("UnDo...");  
              chessBoard.goback();  
          }  
      }  
  }  
    
    
    
  public static void main(String[] args){  
      StartChessJFrame f=new StartChessJFrame();//initialize Frame  
      f.setVisible(true);//show Frame
        
  }  
}  