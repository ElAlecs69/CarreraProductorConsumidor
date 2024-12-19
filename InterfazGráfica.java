package comercio;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class InterfazGráfica extends JFrame{

	private static final long serialVersionUID = 1L;
    private JLabel EHilo1, EHilo2;
    private JProgressBar BarraHilo1, BarraHilo2;
    private JButton Botón1;
    private Thread Productor, Consumidor;
    private volatile boolean ca = false;
    private Backend backend1, backend2;
    private boolean turnoHilo1 = true;
    
    public InterfazGráfica() {
    	
    	setTitle("Comercio entre Productores y Consumidores");
        setSize(600, 200);
        setLayout(new GridLayout(3, 2));
        setLocationRelativeTo(null);

        EHilo1 = new JLabel("       Productor");
        EHilo1.setFont(new Font("Arial", Font.BOLD, 16));
        EHilo1.setForeground(Color.orange);
        
        EHilo2 = new JLabel("       Consumidor");
        EHilo2.setFont(new Font("Verdana", Font.BOLD, 15));
        EHilo2.setForeground(Color.orange);
        
        BarraHilo1 = new JProgressBar(0, 100);
        BarraHilo1.setForeground(Color.GREEN);
        BarraHilo1.setBackground(Color.darkGray);
        
        BarraHilo2 = new JProgressBar(0, 100);
        BarraHilo2.setForeground(Color.red);
        BarraHilo2.setBackground(Color.DARK_GRAY);
        
        Botón1 = new JButton("Iniciar Carrera");
        Botón1.setBackground(Color.LIGHT_GRAY);
        Botón1.setForeground(Color.BLACK);
        Botón1.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        Botón1.setBorderPainted(false);
        
        EHilo1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        EHilo2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        getContentPane().setBackground(Color.DARK_GRAY);

        add(EHilo1);
        add(BarraHilo1);
        add(EHilo2);
        add(BarraHilo2);
        add(new JLabel());
        add(Botón1);
        
        Botón1.addActionListener(e -> {
            if (!ca) {
                Comercio();
            }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    
    public synchronized void EstadoDeLaCarrera(boolean ca) {
    	this.ca = ca;
    }
    
    public synchronized boolean EstaLaCarreraActiva() {
    	return ca;
    }
    
    public synchronized boolean EsTurnoDelHilo1() {
        return turnoHilo1;
    }

    public synchronized void cambiarTurno() {
        turnoHilo1 = !turnoHilo1; 
    }
    
    public  void Comercio() {
        if (Productor != null && Productor.isAlive()) {
            Productor.interrupt();
        }
        if (Consumidor != null && Consumidor.isAlive()) {
            Consumidor.interrupt();
        }

        ca = true;

        BarraHilo1.setValue(0);
        BarraHilo2.setValue(0);
        
        EHilo1.setText("       " + "Productor" + "          posición: " + 0);
        EHilo2.setText("       " + "Consumidor" + "       posición: " + 0);

        backend1 = new Backend("Productor", EHilo1, BarraHilo1, this, true);
        backend2 = new Backend("Consumidor", EHilo2, BarraHilo2, this, false);

        Productor = new Thread(backend1);
        Consumidor = new Thread(backend2);

        Productor.start();
        Consumidor.start(); 

    }
    
    public void DialogoFinal(String nombre) {
    	
    	UIManager.put("OptionPane.messageFont", new Font("Arial", Font.HANGING_BASELINE, 18));
        UIManager.put("OptionPane.buttonFont", new Font("Comic Sans MS", Font.BOLD, 14));
        UIManager.put("OptionPane.background", Color.LIGHT_GRAY);
        UIManager.put("Panel.background", Color.LIGHT_GRAY);
        UIManager.put("OptionPane.messageForeground", Color.BLUE);
        UIManager.put("Button.background", Color.CYAN);
        UIManager.put("Button.foreground", Color.BLACK);
        
        int respuesta = JOptionPane.showOptionDialog(
            this,
            " el trueque ha llegado a su fin ¿Intentar de nuevo?",
            "Comercio finalizado",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"Sí", "Salir"},
            "Reiniciar"
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            Comercio();
        } else {
            System.exit(0);
        }
    }
    
    
	public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfazGráfica::new);
    }
	
}


