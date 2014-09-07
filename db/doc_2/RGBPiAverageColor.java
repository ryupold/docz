import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.net.Socket;

	
public class RGBPiAverageColor {
	
	public static void main(String args[]) {
		final RGBPiAverageColor r = new RGBPiAverageColor();
		try {
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							r.setAverageColor();
							Thread.sleep(10);
						} catch (Exception e) {
							
							e.printStackTrace();
						}						
					}
					
				}
			});
			
			t.start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setAverageColor() throws Exception {
	 
	   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	   Rectangle screenRectangle = new Rectangle(screenSize);
	   Robot robot = new Robot();
	   BufferedImage image = robot.createScreenCapture(screenRectangle);


	   
	   Color c = getAverageColor(image);
	   
	    try
	    {
	      //Verbindung zu Port 13000 auf localhost aufbauen:
	      Socket socket = new Socket ("192.168.178.26", 4321);

	      PrintWriter pw = new PrintWriter(socket.getOutputStream());
	      float[] hsv =new float[3];
	      hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
	      //hsv[1] = 0.0f;//(float) Math.min(hsv[1] * 1.5, 1);
	      c = new Color(Color.HSBtoRGB(hsv[0], 1.0f, (float) (hsv[2])));
	      
	      String cmd = "{    \"commands\":[        {            \"type\":\"fade\",  \"time\":\"0.1\",          \"end\":\"{b:"+c.getRed()+","+c.getGreen()+","+c.getBlue()+"}\"         }    ]}";
//	      System.out.println(cmd);
	      pw.write(cmd);
	      pw.flush();
	  
	      //Socket dichtmachen:
	      socket.close();
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }
	 
	}
	
	private Color getAverageColor(BufferedImage image) {
	    int height = image.getHeight();
	    int width = image.getWidth();
	    int[] red = new int[height*width];
	    int[] green = new int[height*width];
	    int[] blue = new int[height*width];
	
	    int sumRed = 0, sumGreen=0, sumBlue=0;
	    
	    
	    
	    int color = 0;
	    for(int i=0; i < width; i++) {
	        for(int j=0; j < height ; j++) {
				try{
		            color = image.getRGB(i, j);
//		            sumRed += colorModel.getRed(j + i*width); 
//		            sumGreen +=colorModel.getGreen(j +i*width);
//		            sumBlue += colorModel.getBlue(j +i*width);

		            sumRed += (color & 0x00ff0000) >> 16;
	        		sumGreen += (color & 0x0000ff00) >> 8;
	        		sumBlue += color & 0xff;
			
		         
//		           System.out.println(colorModel.getRGB(i*j)); 		            
		    	   
				}catch(Exception e){
					
				}
				
            }               
        }
	    
//	    System.out.println(sumRed + " "+sumGreen + " "+sumBlue); 	
	    
	    
	    int averageRed = sumRed/red.length;
	    int averageGreen = sumGreen/green.length;
	    int averageBlue = sumBlue/blue.length;
	    
//	    System.out.println("R: "+averageRed+". G: "+averageGreen);
	    
	    Color averageColor = new Color(averageRed, averageGreen, averageBlue);
	    
	    return averageColor;
    } 

}
