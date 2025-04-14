package presentation; 
 
import domain.Costume;
import domain.HalloweenShop;
import domain.HalloweenShopException;
import domain.Log;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/**
 * @version ECI 2020
 */
public class HalloweenShopGUI extends JFrame{


    private static final Dimension PREFERRED_DIMENSION =
                         new Dimension(700,700);

    private HalloweenShop shop;

    /*List*/
    private JButton buttonList;
    private JButton buttonRestartList;
    private JTextArea textDetails;
    
    /*Add*/
    private JTextField name;   
    private JTextField prices;
    private JTextField discount;
    private JTextArea  basics;
    private JButton buttonAdd;
    private JButton buttonRestartAdd;
    
    /*Search*/
    private JTextField textSearch;
    private JTextArea textResults;
    

    
    private HalloweenShopGUI(){
        shop=new HalloweenShop();
        prepareElements();
        prepareActions();
    }


    private void prepareElements(){
        setTitle("Tienda de disfraces");
        name = new JTextField(50);
        prices = new JTextField(50);
        discount = new JTextField(50);
        basics = new JTextArea(10, 50);
        basics.setLineWrap(true);
        basics.setWrapStyleWord(true);
        
        JTabbedPane etiquetas = new JTabbedPane();
        etiquetas.add("Listar",   prepareAreaList());
        etiquetas.add("Adicionar",  prepareAreaAdd());
        etiquetas.add("Buscar", prepareSearchArea());
        getContentPane().add(etiquetas);
        setSize(PREFERRED_DIMENSION);
        
    }


    /**
     * Prepare list area
     * @return 
     */
    private JPanel prepareAreaList(){

        textDetails = new JTextArea(10, 50);
        textDetails.setEditable(false);
        textDetails.setLineWrap(true);
        textDetails.setWrapStyleWord(true);
        JScrollPane scrollArea =
                new JScrollPane(textDetails,
                                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                                
        JPanel  botones = new JPanel();
        buttonList = new JButton("Listar");
        buttonRestartList = new JButton("Limpiar");
        botones.add(buttonList);
        botones.add(buttonRestartList);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollArea, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
     }
     
    /**
     * Prepare adiction area
     * @return adiction area
     */
    private JPanel prepareAreaAdd(){
        
        Box nameH = Box.createHorizontalBox();
        nameH.add(new JLabel("Nombre", JLabel.LEFT));
        nameH.add(Box.createGlue());
        Box nameV = Box.createVerticalBox();
        nameV.add(nameH);
        nameV.add(name);
        
        Box pricesH = Box.createHorizontalBox();
        pricesH.add(new JLabel("Precio basico o del maquillaje", JLabel.LEFT));
        pricesH.add(Box.createGlue());
        Box pricesV = Box.createVerticalBox();
        pricesV.add(pricesH);
        pricesV.add(prices);
        
        Box discountH = Box.createHorizontalBox();
        discountH.add(new JLabel("Descuento", JLabel.LEFT));
        discountH.add(Box.createGlue());
        Box discountV = Box.createVerticalBox();
        discountV.add(discountH);
        discountV.add(discount);        
        
        Box basicsH = Box.createHorizontalBox();
        basicsH.add(new JLabel("Piezas", JLabel.LEFT));
        basicsH.add(Box.createGlue());
        Box basicsV = Box.createVerticalBox();
        basicsV.add(basicsH);
        basicsV.add(basics);
 
        Box singleLineFields = Box.createVerticalBox();
        singleLineFields.add(nameV);
        singleLineFields.add(pricesV);       
        singleLineFields.add(discountV); 
        
        JPanel textDetailsPanel = new JPanel();
        textDetailsPanel.setLayout(new BorderLayout());
        textDetailsPanel.add(singleLineFields, BorderLayout.NORTH);
        textDetailsPanel.add(basicsV, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        buttonAdd = new JButton("Adicionar");
        buttonRestartAdd = new JButton("Limpiar");

        botones.add(buttonAdd);
        botones.add(buttonRestartAdd);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(textDetailsPanel, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    



   /**
     * 
     * @return 
     */
    private JPanel prepareSearchArea(){

        Box busquedaEtiquetaArea = Box.createHorizontalBox();
        busquedaEtiquetaArea.add(new JLabel("Buscar", JLabel.LEFT));
        busquedaEtiquetaArea.add(Box.createGlue());
        textSearch = new JTextField(50);
        Box busquedaArea = Box.createHorizontalBox();
        busquedaArea.add(busquedaEtiquetaArea);
        busquedaArea.add(textSearch);
        
        textResults = new JTextArea(10,50);
        textResults.setEditable(false);
        textResults.setLineWrap(true);
        textResults.setWrapStyleWord(true);
        JScrollPane scrollArea = new JScrollPane(textResults,
                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel buttonListea = new JPanel();
        buttonListea.setLayout(new BorderLayout());
        buttonListea.add(busquedaArea, BorderLayout.NORTH);
        buttonListea.add(scrollArea, BorderLayout.CENTER);

        return buttonListea;
    }


    private void prepareActions(){
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev){
                setVisible(false);
                System.exit(0);
            }
        });
        
        /*List*/
        buttonList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                actionList();
            }
        });

        buttonRestartList.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                textDetails.setText("");
            }
        });
        
        /*Add*/
        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev){
                actionAdd();                    
            }
        });
        
        buttonRestartAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev){
                name.setText("");
                prices.setText("");
                discount.setText("");
                basics.setText("");
            }
        });
        
        /*Search*/
        textSearch.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent ev){
                actionSearch();
            }
           
            public void insertUpdate(DocumentEvent ev){
                actionSearch();
            }
            
            public void removeUpdate(DocumentEvent ev){
                actionSearch();
            }
        });
    }    

    
    private void actionList(){
        textDetails.setText(shop.toString());
    }
    
    private void actionAdd() {
        try {
            if (basics.getText().trim().equals("")) {
                // Intentar agregar un disfraz básico
                shop.addBasic(name.getText(), prices.getText(), discount.getText());
            } else {
                // Intentar agregar un disfraz completo
                shop.addComplete(name.getText(), prices.getText(), discount.getText(), basics.getText());
            }
            
            //Mensaje exitoso
            JOptionPane.showMessageDialog(this, "Disfraz agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {            
            // Mensaje de alerta al usuario
            if(e.getMessage().equals(HalloweenShopException.DUPLICATE_BASIC)){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);    
            } 
            if(e.getMessage().equals(HalloweenShopException.NO_NUMERIC_VALUES)){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);    
            }
            if(e.getMessage().equals(HalloweenShopException.INVALID_DISCOUNT)){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);    
            }
            if(e.getMessage().equals(HalloweenShopException.DUPLICATE_BASIC)){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);    
            } 
            if(e.getMessage().equals(HalloweenShopException.NO_NUMERIC_VALUES)){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);    
            }
            if(e.getMessage().equals(HalloweenShopException.NO_EXIST_BASICS)){
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);    
            }

            //JOptionPane.showMessageDialog(this, "Ocurrió un error al intentar agregar el disfraz. Por favor, revise los datos e intente de nuevo.", "Error", JOptionPane.WARNING_MESSAGE);
            
            // Guardar el error en el registro
            Log.record(e);
            
            // Continuar la ejecución: el bloque `catch` finaliza y el usuario puede seguir usando la aplicación.
        }
    }


    private void actionSearch() {
        String patronBusqueda = textSearch.getText();
        String answer;
        try {
            if (patronBusqueda.trim().isEmpty()) {
                answer = shop.toString();  // Mostrar todos los disfraces si el prefijo está vacío
            } else {
                answer = shop.search(patronBusqueda);
                if (answer.equals("0 disfraces\n")) {
                    answer = "No se encontraron resultados para su búsqueda.";
                }
            }
            textResults.setText(answer);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al realizar la búsqueda. Por favor, intente nuevamente.", "Error", JOptionPane.WARNING_MESSAGE);
            Log.record(e);
            textResults.setText("Error en la búsqueda. Consulte al administrador.");
        }
    }


    
   public static void main(String args[]){
       HalloweenShopGUI gui=new HalloweenShopGUI();
       gui.setVisible(true);
   }    
}
