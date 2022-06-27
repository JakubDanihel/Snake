import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class Panel extends JPanel implements ActionListener{

    //velkost okna
    static final int SCREEN_WITH = 600;
    static final int SCREEN_HEIGHT = 600;

    //rozdelenie pola na elementy
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WITH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100; //nastavenie rychlosti hada

    //nastavenie koordinatov 
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    //nastavenie zaciatku hry 
    int telo = 6;   //velkost hada na zaciatku hry
    int jedlo_ham;  //pocet zjedeneho

    //kordynaty jedla
    int jedloX;
    int jedloY;

    //nastavenie casovaca
    Timer timer;
    Random random;

    //udanie pociatocneho smeru ako sa had hybe
    char smer = 'R'; //zacne v smere doprava
    boolean running = false;

    Panel()
    {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WITH, SCREEN_HEIGHT)); //nastavenie velksti herneho pola
        this.setBackground(Color.black);    //nastavenie farby pozadia
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter()); //snimanie stlacenej klavesy

        zaciatokHry();

    }

    //zaciatok hry
    public void zaciatokHry()
    {
        noveJedlo(); //vlozenie noveho jedla
        running = true; //spustenie hry lebo predtym bolo nastavene na false a hra stala
        timer = new Timer(DELAY, this); //spustenie casovaca
        timer.start(); //spustenie

    }

    // vyfarbenie komponentov hada
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    //kreslenie objektov
    public void draw(Graphics g)
    {
        if(running){
            //nakreslenie mriezky
            for(int i = 0; i<(SCREEN_HEIGHT/UNIT_SIZE); i++)
            {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); //ciary pre x
                g.drawLine(0, i*UNIT_SIZE , SCREEN_WITH, i*UNIT_SIZE);  //ciary pre y
            }

            //vykreslenie jedla
            g.setColor(Color.RED);
            g.fillOval(jedloX,jedloY, UNIT_SIZE, UNIT_SIZE);

            //vykreslenie tela a hlavy hada
            for(int i = 0; i<telo; i++)
            {
                if(i == 0)
                {
                    //ak sa i=0 je to hlava
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
                }
                else
                {
                    //ak i != 0 je to telo
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //skore
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Skore: "+jedlo_ham, (SCREEN_WITH - metrics.stringWidth("Skore: "+jedlo_ham))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }

    //nove jedlo jedlo
    public void noveJedlo()
    {
        jedloX = random.nextInt((int)(SCREEN_WITH/UNIT_SIZE))*UNIT_SIZE;
        jedloY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    //pohyb
    public void pohyb()
    {
        //pohyb hada
        for(int i = telo; i>0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        //zmena smeru a udrziavanie smeru
        switch(smer){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

                case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            
                case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

                case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    //jedlo
    public void jedlo(){

        for(int i =telo; i>0; i--)
        {
            if((x[0] == jedloX) && (y[0] == jedloY))
            {
                telo++;
                jedlo_ham++;
                noveJedlo();
            }
        }
    }

    //kolizie
    public void kolizie(){

        //ci si had je telo
        for(int i = telo; i>0; i--)
        {
            if((x[0]==x[i]) && (y[0]==y[i]))
            {
                running = false;
            }
        }

        //ci hlava narazila na hranice:

        //lava
        if(x[0]<0)
        {
            running = false;
        }

        //prava
        if(x[0]>SCREEN_WITH)
        {
            running = false;
        }
        //hornej
        if(y[0]<0)
        {
            running = false;
        }
        //dolnej
        if(y[0]>SCREEN_HEIGHT)
        {
            running = false;
        }

        if(!running)
        {
            timer.stop();
        }

    }

    //Game over
    public void gameOver(Graphics g){
        //game over text
        g.setColor(Color.RED); //nastavenie farby textu
        g.setFont(new Font("Serif Italic", Font.ITALIC, 75)); //nastavenie fontu a vlastnoti textu
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WITH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT/2); //umiestnenie textu

        //skore po ukonceni hry
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Skore: "+jedlo_ham, (SCREEN_WITH - metrics2.stringWidth("Skore: "+jedlo_ham))/2, g.getFont().getSize());


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(running){
            pohyb();
            jedlo();
            kolizie();
        }
        repaint();
        
    }

    //zaznamenanie klaves
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode())
            {
                //dolava
                case KeyEvent.VK_LEFT:
                if(smer != 'R')
                {
                    smer = 'L';
                }
                break;

                //doprava
                case KeyEvent.VK_RIGHT:
                if(smer != 'L')
                {
                    smer = 'R';
                }
                break;

                //hore
                case KeyEvent.VK_UP:
                if(smer != 'D')
                {
                    smer = 'U';
                }
                break;

                //dole
                case KeyEvent.VK_DOWN:
                if(smer != 'U')
                {
                    smer = 'D';
                }
                break;

            }
        }
    }
    
}
