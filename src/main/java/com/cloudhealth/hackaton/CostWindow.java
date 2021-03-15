package com.cloudhealth.hackaton;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class CostWindow {

  private JTextField filePath;
  private JButton calculateButton;
  private JLabel calculatedCost;
  private JPanel windowContent;
  private JTextField apiKey;
  private JTextField cpu;
  private JTextField replicas;
  private JRadioButton radioButtonHourly;
  private JRadioButton radioButtonDaily;
  private JRadioButton radioButtonMonthly;
  private Project project;

  public CostWindow(Project project, ToolWindow toolWindow) {
    this.project = project;
    calculateButton.addActionListener(e -> calculateCost());
  }

  public void calculateCost() {
    calculatedCost.setText(Double.toString(Math.random()));
  }

  public JPanel getContent() {
    return windowContent;
  }
}
