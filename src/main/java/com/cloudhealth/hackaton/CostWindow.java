package com.cloudhealth.hackaton;

import static java.lang.String.format;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class CostWindow {

  private JButton calculateButton;
  private JLabel calculatedCost;
  private JPanel windowContent;
  private JTextField apiKey;
  private JTextField cpu;
  private JTextField replicas;
  private JRadioButton radioButtonHourly;
  private JRadioButton radioButtonDaily;
  private JRadioButton radioButtonMonthly;
  private JTextField clusterId;
  private JLabel clusterIdLabel;
  private Granularity selectedGranularity = Granularity.HOURLY;

  public CostWindow() {
    calculateButton.addActionListener(e -> calculateCost());
    radioButtonHourly.addActionListener(e -> selectedGranularity = Granularity.HOURLY);
    radioButtonDaily.addActionListener(e -> selectedGranularity = Granularity.DAILY);
    radioButtonMonthly.addActionListener(e -> selectedGranularity = Granularity.MONTHLY);
  }

  public void calculateCost() {
    if (validateInputs()) {
      PodCostCalculator calculator = new PodCostCalculator(apiKey.getText());
      Double costPerPod = calculator.calculateHowMuchPerPod(clusterId.getText(),
              Integer.parseInt(replicas.getText()),
              Double.parseDouble(cpu.getText()),
              selectedGranularity);

      calculatedCost.setText(format("$%.2f", costPerPod));
    } else {
      JOptionPane.showMessageDialog(windowContent,
          "ERROR: Validation failed.",
          "ERROR",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public JPanel getContent() {
    return windowContent;
  }

  private boolean validateInputs() {
    boolean result = true;
    if (apiKey.getText() == null || apiKey.getText().trim().equalsIgnoreCase("") ) {
      result = false;
    }
    if (cpu.getText() == null || cpu.getText().trim().equalsIgnoreCase("") ) {
      result = false;
    }
    if (replicas.getText() == null || replicas.getText().trim().equalsIgnoreCase("") ) {
      result = false;
    }
    if (clusterId.getText() == null || clusterId.getText().trim().equalsIgnoreCase("") ) {
      result = false;
    }
    try {
      Double.parseDouble(cpu.getText());
      Double.parseDouble(replicas.getText());
    } catch (NumberFormatException nfe) {
      result = false;
    }

    return result;
  }
}
