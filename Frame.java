import javax.swing.JFrame;

public class Frame extends JFrame{

    Frame(){

        //vytvorenie okna
        this.add(new Panel());
        this.setTitle("Had"); //titul
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //moznost zatvorenia
        this.setResizable(false); //zabranenie zmeny velkosti
        this.pack(); //vlozenie komponentov do okna
        this.setVisible(true); //viditelnost
        this.setLocationRelativeTo(null); //nastavenie kde sa okno umiestni
    }
}
