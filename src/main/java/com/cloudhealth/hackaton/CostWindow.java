package com.cloudhealth.hackaton;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
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
  private Project project;
  private Granularity selectedGranularity = Granularity.HOURLY;

  public CostWindow(Project project, ToolWindow toolWindow) {
    this.project = project;
    calculateButton.addActionListener(e -> calculateCost());
    radioButtonHourly.addActionListener(e -> selectedGranularity = Granularity.HOURLY);
    radioButtonDaily.addActionListener(e -> selectedGranularity = Granularity.DAILY);
    radioButtonMonthly.addActionListener(e -> selectedGranularity = Granularity.MONTHLY);
  }

  public void calculateCost() {
    if (validateInputs()) {
      Double cpu = 1.0;
      int replicas = 100;
      String cluster = "us1-prod";
      Double totalDesiredCpu = getTotalDesiredCpu(cpu, replicas);
      Double currentClusterCost = getCurrentCostOfCluster(cluster);
      Double currentAvailableCpuOnCluster = getCurrentAvailableCpuOfCluster(cluster);
      Double costPerCpu = currentClusterCost/currentAvailableCpuOnCluster;
      Double costOfDesiredCpu = costPerCpu * totalDesiredCpu;

      calculatedCost.setText(Double.toString(costOfDesiredCpu));    } else {
      JOptionPane.showMessageDialog(windowContent,
          "ERROR: Validation failed.",
          "ERROR",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private Double getTotalDesiredCpu(Double cpuPerPod, int totalReplicas) {
    return cpuPerPod * totalReplicas;
  }

  private Double getCurrentCostOfCluster(String cluster) {
    return 170000.0;
  }

  private Double getCurrentAvailableCpuOfCluster(String cluster) {
    return 50000.0;
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
    try {
      Double.parseDouble(cpu.getText());
      Double.parseDouble(replicas.getText());
    } catch (NumberFormatException nfe) {
      result = false;
    }

    return result;
  }
}
