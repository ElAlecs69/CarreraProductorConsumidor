package comercio;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Backend implements Runnable{

	private String n;
    private JProgressBar Barra;
    private volatile int p = 0;
    private JLabel E;
    private InterfazGráfica IG;
    private static final Object lock = new Object(); 
    private boolean QuéHilo;
    
    public Backend(String n, JLabel E, JProgressBar Barra, InterfazGráfica IG, boolean QuéHilo) {
        this.n = n;
        this.E = E;
        this.Barra = Barra;
        this.IG = IG;
        this.QuéHilo = QuéHilo;
    }

	@Override
	public void run() {
		
        while (IG.EstaLaCarreraActiva() && !Thread.currentThread().isInterrupted()) {
        		
            synchronized (lock) {
                while (IG.EsTurnoDelHilo1() != QuéHilo) {
                    try {
                        lock.wait(); 
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                ProductorConsumidor();

                IG.cambiarTurno();
                lock.notifyAll(); 
            }
        }
    }


    private void ProductorConsumidor() {
        	
    	Random a = new Random();
    
    		int avance = a.nextInt(9)+1;
    		
            p += avance;
            
            if (p >= 100) {
            	Barra.setValue(p);
            	E.setText("       " + n + "        posición: " + p);
                IG.EstadoDeLaCarrera(false);
                IG.DialogoFinal(n);
            }
            
            Barra.setValue(p);
            E.setText("       " + n + "        posición: " + p);

            try {
                Thread.sleep(250); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        
    }
    
    
}
