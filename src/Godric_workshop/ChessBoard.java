package Godric_workshop;
import java.awt.Color;  
import java.awt.Cursor;  
import java.awt.Dimension;  
import java.awt.Graphics;  
import java.awt.Graphics2D;  
import java.awt.Image;  
import java.awt.RadialGradientPaint;  
import java.awt.RenderingHints;  
import java.awt.Toolkit;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseListener;  
import java.awt.event.MouseMotionListener;  
import java.awt.geom.Ellipse2D;  
  
import javax.swing.*;  
/** 
 * ChessBoard
*/
//Test 10
public class ChessBoard extends JPanel implements MouseListener {
   public static final int MARGIN=45;//Margin Size
   public static final int GRID_SPAN=40;//Grid Span
   public static final int ROWS=14;//# of rows
   public static final int COLS=14;//# of cols

   public JLabel label;

   Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//each element initialized null  
   boolean isBlack=true;//is current chess to be set black? 
   boolean gameOver=false;//is game over?
   int chessCount;//how many chess set so far?
   int xIndex,yIndex;//Index of the chess just set

   Image img;
   Image shadows;
   Color colortemp;
   public ChessBoard(){
       setBackground(Color.GRAY);//set Panel's background
       img=Toolkit.getDefaultToolkit().getImage("board1.jpg");
       shadows=Toolkit.getDefaultToolkit().getImage("shadows.jpg");
       addMouseListener(this);
       addMouseMotionListener(new MouseMotionListener(){
           public void mouseDragged(MouseEvent e){

           }

           public void mouseMoved(MouseEvent e){
           //Convert mouse position to index
             int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
             int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
             //Can't set chess when gameover, pos outside of board or position(x1,y1) already occupied.  
             //if not valid, set cursor to default.
             if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1))
                 setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
             //if valid, set cursor to hand
             else setCursor(new Cursor(Cursor.HAND_CURSOR));

           }
       });
   }


//����
   public void paintComponent(Graphics g){

       super.paintComponent(g);//draw Panel

       int imgWidth= img.getWidth(this);  //get picture's width and height
       int imgHeight=img.getHeight(this);

       int FWidth=getWidth();  //get window width and height from preferredSize
       int FHeight=getHeight();
       int x=(FWidth-imgWidth)/2;
       int y=(FHeight-imgHeight)/2;
       g.drawImage(img, x, y, null);

       for(int i=0;i<=ROWS;i++){//draw row lines
           g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);  
       }
       for(int i=0;i<=COLS;i++){//draw vertical lines
           g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN, MARGIN+ROWS*GRID_SPAN);  

       }
       //from the chessList history, draw chess to the # of chessCount
       for(int i=0;i<chessCount;i++){
           //Convert index to grid coordinate.
           int xPos=chessList[i].getX()*GRID_SPAN+MARGIN;
           int yPos=chessList[i].getY()*GRID_SPAN+MARGIN;
          //g.setColor(chessList[i].getColor());//������ɫ
          // g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,
                           //Point.DIAMETER, Point.DIAMETER);
           //g.drawImage(shadows, xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER, null);
           //set drawing color
           colortemp=chessList[i].getColor();
           if(colortemp==Color.black){
               RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 20, new float[]{0f, 1f}
               , new Color[]{Color.WHITE, Color.BLACK});
               ((Graphics2D) g).setPaint(paint);
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);

           }
           else if(colortemp==Color.white){
               RadialGradientPaint paint = new RadialGradientPaint(xPos-Point.DIAMETER/2+25, yPos-Point.DIAMETER/2+10, 70, new float[]{0f, 1f}
               , new Color[]{Color.WHITE, Color.BLACK});
               ((Graphics2D) g).setPaint(paint);
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT);
           }

           //draw the oval
           Ellipse2D e = new Ellipse2D.Float(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, 34, 35);
           ((Graphics2D) g).fill(e);

           //mark the last chess in a red rectangle
           if(i==chessCount-1){
               g.setColor(Color.red);
               g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2,
                           34, 35);
           }
       }
   }

   //Override mousePressed in MouseListener
   public void mousePressed(MouseEvent e){
	   //reset the label
	   if(isBlack) label.setText("		White's Turn");
	   else label.setText("		Black's Turn");
	   
       //Do nothing when game is over
       if(gameOver) return;
         
       String colorName=isBlack?"Black":"White";

       //Convert coordinate to index  
       xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
       yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;

       //Do nothing when outside the board
       if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)
           return;

       //Do nothing when current position already occupied
       if(findChess(xIndex,yIndex))return;

       //if valid, Draw a new chess at current position and add index to the chessList 
       Point ch=new Point(xIndex,yIndex,isBlack?Color.black:Color.white);
       chessList[chessCount++]=ch;
       repaint();//run paintComponent


       //if someone wins, give a message
       if(isWin()){
           String msg=String.format("Congratulations��%s won��", colorName);  
           JOptionPane.showMessageDialog(this, msg);
           gameOver=true;
       }
       isBlack=!isBlack;
     }
   //override mouseListener's other unimplemented methods
   public void mouseClicked(MouseEvent e){
       //��갴��������ϵ���ʱ����
   }

   public void mouseEntered(MouseEvent e){
       //�����뵽�����ʱ����
   }  
   public void mouseExited(MouseEvent e){
       //����뿪���ʱ����
   }  
   public void mouseReleased(MouseEvent e){
       //��갴ť��������ͷ�ʱ����
   }
   //if the chess at index(x,y) exists
   private boolean findChess(int x,int y){
       for(Point c:chessList){
           if(c!=null&&c.getX()==x&&c.getY()==y)
               return true;
       }
       return false;
   }

   private boolean isWin(){
       int continueCount=1;//# of continuous chess
        
       //search to left
       for(int x=xIndex-1;x>=0;x--){
           Color c=isBlack?Color.black:Color.white;  
           if(getChess(x,yIndex,c)!=null){
               continueCount++;
           }else
               break;
       }
      //search to right
       for(int x=xIndex+1;x<=COLS;x++){
          Color c=isBlack?Color.black:Color.white;
          if(getChess(x,yIndex,c)!=null){
             continueCount++;
          }else
             break;
       }  
       if(continueCount>=5){
             return true;
       }else
       continueCount=1;
         
       //search to top
       for(int y=yIndex-1;y>=0;y--){
           Color c=isBlack?Color.black:Color.white;
           if(getChess(xIndex,y,c)!=null){
               continueCount++;
           }else
               break;
       }
       //search to bottom
       for(int y=yIndex+1;y<=ROWS;y++){
           Color c=isBlack?Color.black:Color.white;
           if(getChess(xIndex,y,c)!=null)
               continueCount++;
           else
              break;
       }
       if(continueCount>=5)
           return true;
       else
           continueCount=1;

       //search to right-top
       for(int x=xIndex+1,y=yIndex-1;y>=0&&x<=COLS;x++,y--){
           Color c=isBlack?Color.black:Color.white;
           if(getChess(x,y,c)!=null){
               continueCount++;
           }
           else break;
       }
       //search to left-bottom
       for(int x=xIndex-1,y=yIndex+1;x>=0&&y<=ROWS;x--,y++){
           Color c=isBlack?Color.black:Color.white;
           if(getChess(x,y,c)!=null){
               continueCount++;
           }
           else break;
       }
       if(continueCount>=5)
           return true;
       else continueCount=1;


       //search to left-top
       for(int x=xIndex-1,y=yIndex-1;x>=0&&y>=0;x--,y--){
           Color c=isBlack?Color.black:Color.white;
           if(getChess(x,y,c)!=null)
               continueCount++;
           else break;
       }
       //search to right-bottom
       for(int x=xIndex+1,y=yIndex+1;x<=COLS&&y<=ROWS;x++,y++){
           Color c=isBlack?Color.black:Color.white;
           if(getChess(x,y,c)!=null)
               continueCount++;
           else break;
       }
       if(continueCount>=5)
           return true;
       else continueCount=1;

       return false;
     }

   private Point getChess(int xIndex,int yIndex,Color color){
       for(Point p:chessList){
           if(p!=null&&p.getX()==xIndex&&p.getY()==yIndex
                   &&p.getColor()==color)
               return p;
       }
       return null;
   }

   public void restartGame(){
       //clear the chessList
       for(int i=0;i<chessList.length;i++){
           chessList[i]=null;
       }
       //restore variables
       isBlack=true;
       gameOver=false;
       chessCount =0;
       repaint();
   }

   //goBack  
   public void goback(){
	   if(isBlack) label.setText("White's Turn");
       else label.setText("Black's Turn");
       if(chessCount==0)
           return ;
       chessList[chessCount-1]=null;
       chessCount--;
       if(chessCount>0){
           xIndex=chessList[chessCount-1].getX();
           yIndex=chessList[chessCount-1].getY();
       }
       isBlack=!isBlack;
       repaint();
   }

   //override getPreferredSize
  
   public Dimension getPreferredSize(){
       return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2
                            +GRID_SPAN*ROWS);
   }
}