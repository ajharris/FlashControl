/**
 *  <i>Flash Control</i>. Provides fader control and text entry to control an array of LED sources with Arduino
 *  <p>
 *  
 *  @author Andrew Harris
 */

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.TextField;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.JScrollBar;
import javax.swing.JTable;

public class FlashControl implements ActionListener{

	private JFrame frame;
	static ArduinoController lightController;
	LEDArray fLight = new LEDArray(12);
	private JTextField txtPresetName;
	private JTextField txtPinNumber;
	private JTextField txtFrequency;
	private dataPlot plot = new dataPlot();

	/**
	 * Launch the application.
	 * @throws InvocationTargetException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException, InvocationTargetException {

		EventQueue.invokeAndWait(new Runnable() {
			public void run() {
				try {
					FlashControl window = new FlashControl();
					window.frame.setVisible(true);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
		Thread lc = new Thread(new Runnable(){

			@Override
			public void run() {
				lightController = new ArduinoController();
				lightController.initialize();
			}
		
		});
		lc.start();
		
	}

	/**
	 * Create the application.
	 */
	public FlashControl() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String[] commPorts = new String[lister.list().size()];
		commPorts = lister.list().toArray(commPorts);
		
		
		frame = new JFrame();
		frame.setBounds(0, 0, 1200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel lblLedIntensityChannels = new JLabel("LED Intensity Channels 2 - 13");
        lblLedIntensityChannels.setBounds(71, 32, 186, 16);
        frame.getContentPane().add(lblLedIntensityChannels);
        
        txtPresetName = new JTextField();
        txtPresetName.setBounds(381, 53, 134, 28);
        txtPresetName.setText("Preset Name");
        frame.getContentPane().add(txtPresetName);
        txtPresetName.setColumns(10);
        
        
        JButton btnSaveLedPreset = new JButton("Save LED Preset");
        btnSaveLedPreset.setBounds(373, 87, 142, 29);
        frame.getContentPane().add(btnSaveLedPreset);
        btnSaveLedPreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fLight.savePreset(txtPresetName.getText() + ".pre");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
        	
        });
        
        JButton btnLoadLedPreset = new JButton("Load LED Preset");
        btnLoadLedPreset.setBounds(371, 122, 144, 29);
        frame.getContentPane().add(btnLoadLedPreset);
        
        JPanel panel = new JPanel();
        panel.setBounds(31, 59, 226, 111);
        frame.getContentPane().add(panel);
        
        Dimension d = new Dimension(10, 100);
        
        final JScrollBar scrollBar = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar);
        scrollBar.setPreferredSize(d);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
				faderSet(2, adjustmentEvent);
            }
          });  
       
        final JScrollBar scrollBar_1 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_1);
        scrollBar_1.setPreferredSize(d);
        scrollBar_1.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(3, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_2 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_2);
        scrollBar_2.setPreferredSize(d);
        scrollBar_2.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(4, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_3 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_3);
        scrollBar_3.setPreferredSize(d);
        scrollBar_3.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(5, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_4 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_4);
        scrollBar_4.setPreferredSize(d);
        scrollBar_4.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(6, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_5 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_5);
        scrollBar_5.setPreferredSize(d);
        scrollBar_5.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(7, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_6 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_6);
        scrollBar_6.setPreferredSize(d);
        scrollBar_6.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(8, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_7 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_7);
        scrollBar_7.setPreferredSize(d);
        scrollBar_7.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(9, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_8 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_8);
        scrollBar_8.setPreferredSize(d);
        scrollBar_8.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
            	faderSet(10, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_9 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_9);
        scrollBar_9.setPreferredSize(d);
        scrollBar_9.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(11, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_10 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_10);
        scrollBar_10.setPreferredSize(d);
        scrollBar_10.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(12, adjustmentEvent);
            }
          });
        
        final JScrollBar scrollBar_11 = new JScrollBar(1, 230, 25, 0, 255);
        panel.add(scrollBar_11);
        scrollBar_11.setPreferredSize(d);
        
        
        txtPinNumber = new JTextField();
        txtPinNumber.setText("Pin Number");
        txtPinNumber.setBounds(31, 216, 86, 20);
        frame.getContentPane().add(txtPinNumber);
        txtPinNumber.setColumns(10);
        
        txtFrequency = new JTextField();
        txtFrequency.setText("Frequency");
        txtFrequency.setBounds(31, 247, 86, 20);
        frame.getContentPane().add(txtFrequency);
        txtFrequency.setColumns(10);
        
        JButton btnSetFrequency = new JButton("Set Frequency");
        btnSetFrequency.setBounds(31, 278, 134, 23);
        frame.getContentPane().add(btnSetFrequency);
        
        JPanel panel_1 = new JPanel(new GridLayout(1,1));
        panel_1.setBounds(31, 335, 1100, 220);
        frame.getContentPane().add(panel_1);
        plot.init();
        panel_1.add(plot);
        
        JComboBox comboBox = new JComboBox(commPorts);
        comboBox.setBounds(1002, 55, 192, 27);
        frame.getContentPane().add(comboBox);
        
        JLabel lblLightPort = new JLabel("Light Port");
        lblLightPort.setBounds(917, 59, 73, 16);
        frame.getContentPane().add(lblLightPort);
        
        JLabel lblSpectPort = new JLabel("Spect Port");
        lblSpectPort.setBounds(917, 92, 73, 16);
        frame.getContentPane().add(lblSpectPort);
        
        JComboBox comboBox_1 = new JComboBox(commPorts);
        comboBox_1.setBounds(1002, 88, 192, 27);
        frame.getContentPane().add(comboBox_1);
        
        btnSetFrequency.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				lightController.setFrequency(txtPinNumber.getText(), txtFrequency.getText());
				fLight.setFreq(txtPinNumber.getText(), txtFrequency.getText());
			}
        	
        });

        scrollBar_11.addAdjustmentListener(new AdjustmentListener() {
        	public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        		faderSet(13, adjustmentEvent);
            }
          });
        
        
        btnLoadLedPreset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					fLight = fLight.loadPreset();
					for(int i = 0; i < fLight.length(); i++){
						try {
							lightController.faderMove(i+2, fLight.getElement(i));
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					if(fLight == null) return;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				scrollBar.setValue(255-fLight.getElement(0));
				scrollBar_1.setValue(255-fLight.getElement(1));
				scrollBar_2.setValue(255-fLight.getElement(2));
				scrollBar_3.setValue(255-fLight.getElement(3));
				scrollBar_4.setValue(255-fLight.getElement(4));
				scrollBar_5.setValue(255-fLight.getElement(5));
				scrollBar_6.setValue(255-fLight.getElement(6));
				scrollBar_7.setValue(255-fLight.getElement(7));
				scrollBar_8.setValue(255-fLight.getElement(8));
				scrollBar_9.setValue(255-fLight.getElement(9));
				scrollBar_10.setValue(255-fLight.getElement(10));
				scrollBar_11.setValue(255-fLight.getElement(11));
				
				lightController.setFrequency(0, fLight.getFreq(0));
				lightController.setFrequency(1, fLight.getFreq(1));
				lightController.setFrequency(2, fLight.getFreq(2));
				lightController.setFrequency(3, fLight.getFreq(3));
				lightController.setFrequency(4, fLight.getFreq(4));
				lightController.setFrequency(5, fLight.getFreq(5));
				lightController.setFrequency(6, fLight.getFreq(6));
				lightController.setFrequency(7, fLight.getFreq(7));
				lightController.setFrequency(8, fLight.getFreq(8));
				lightController.setFrequency(9, fLight.getFreq(9));
				lightController.setFrequency(10, fLight.getFreq(10));
				lightController.setFrequency(11, fLight.getFreq(11));				
			}
        	
        });
        
        
        
        
        
        
	}
	public void faderSet(final int fader, AdjustmentEvent adjustmentEvent){
    	final int value = 255-adjustmentEvent.getValue();
    		while(fLight.getElement(fader-2)!=value){
 
   					try {
   						lightController.faderMove(fader, fLight.getElement(fader-2));

						}
				
   					catch (InterruptedException e) {
   						e.printStackTrace();
    					}
    					if(fLight.getElement(fader-2)<value){
    						fLight.setElement(fader-2, fLight.getElement(fader-2)+1);
    					}
    					else{
    						fLight.setElement(fader-2, fLight.getElement(fader-2)-1);
    					}
    		}
    	
    	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	private CommPortLister lister = new CommPortLister();
	
//	

}



