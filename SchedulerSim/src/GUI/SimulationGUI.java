/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import EDD.Queue;
import EDD.SimpleNode;
import OS.CPU;
import OS.Clock;
import OS.ClockListener;
import ProcessCreation.MemoryManager;
import ProcessCreation.PCB;
import ProcessCreation.ProcessLoader;
import Scheduler.SchedulerManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Daniel
 */
public class SimulationGUI extends javax.swing.JFrame implements ClockListener {

    private SchedulerManager schedulerManager;
    private Clock clock;
    private CPU cpu;
    private Queue<PCB> readyQueue = new Queue<>();
    private Queue<PCB> blockedQueue = new Queue<>();
    private Queue<PCB> terminatedQueue = new Queue<>();
    private Queue<PCB> newQueue;
    private MemoryManager memoryManager;
    private JLabel lblCicloGlobal;
    private JPanel readyPanel = new JPanel();
    private JPanel blockedPanel = new JPanel();
    private JPanel endedPanel = new JPanel();

    private JTextArea logTextArea;

    private void initLogArea() {
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        LogArea.setLayout(new BorderLayout());
        LogArea.add(scrollPane, BorderLayout.CENTER);
    }

    public SimulationGUI(Clock clock, CPU cpu, Queue<PCB> readyQueue,
            Queue<PCB> blockedQueue, Queue<PCB> terminatedQueue,
            MemoryManager memoryManager, Queue<PCB> newQueue) {
        initComponents();
        initLogArea();
        setLocationRelativeTo(null);
        PCB.setLogger(this::logEvent);
        this.clock = clock;
        this.cpu = cpu;
        this.readyQueue = readyQueue;
        this.blockedQueue = blockedQueue;
        this.terminatedQueue = terminatedQueue;
        this.memoryManager = memoryManager;
        this.newQueue = newQueue;

        readyPanel.setLayout(new BoxLayout(readyPanel, BoxLayout.Y_AXIS));
        blockedPanel.setLayout(new BoxLayout(blockedPanel, BoxLayout.Y_AXIS));
        endedPanel.setLayout(new BoxLayout(endedPanel, BoxLayout.Y_AXIS));

        ReadyQueue.setLayout(new BorderLayout());
        ReadyQueue.add(new JScrollPane(readyPanel), BorderLayout.CENTER);

        BlockedQueue.setLayout(new BorderLayout());
        BlockedQueue.add(new JScrollPane(blockedPanel), BorderLayout.CENTER);

        EndedQueue.setLayout(new BorderLayout());
        EndedQueue.add(new JScrollPane(endedPanel), BorderLayout.CENTER);

        schedulerManager = new SchedulerManager(clock, cpu, readyQueue);
        schedulerManager = new SchedulerManager(clock, cpu, readyQueue);
        schedulerManager.setLogger(this::logEvent);

        cpu.setLogger(this::logEvent);
    }

    private JPanel createProcessCard(PCB pcb) {
        JPanel card = new JPanel();
        card.setLayout(new GridLayout(0, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        card.setBackground(Color.WHITE);

        card.add(new JLabel("ID: " + pcb.getPid()));
        card.add(new JLabel("PC: " + pcb.getPc()));
        card.add(new JLabel("MAR: " + pcb.getMar()));
        card.add(new JLabel("Estado: " + pcb.getStatus()));
        card.add(new JLabel("Tipo: " + (pcb.isCpuBound() ? "CPU-bound" : "I/O-bound")));

        return card;
    }

    private void actualizarCPU() {
        PCB running = cpu.getRunningProcess();

        if (running != null) {
            ProcessExecution.setText("Ejecutando: " + running.getName());
            ID.setText("ID: " + running.getPid());
            PC.setText("PC: " + running.getPc());
            MAR.setText("MAR: " + running.getMar());
            Status.setText("Status: " + running.getStatus());
            Tipo.setText(running.isCpuBound() ? "Tipo: CPU-bound" : "Tipo: I/O-bound");
        } else {
            ProcessExecution.setText("CPU libre");
            ID.setText("ID: -");
            PC.setText("PC: -");
            MAR.setText("MAR: -");
            Status.setText("Status: -");
            Tipo.setText("Tipo: -");
        }
    }

    @Override
    public void onTick(long cycle) {
        SwingUtilities.invokeLater(() -> {
            CicloGlobalReloj.setText("CICLO GLOBAL DEL RELOJ: " + cycle);
            actualizarColasEnGUI();
            actualizarCPU();
        });
    }

    private void actualizarColasEnGUI() {

        cleanTerminated(readyQueue);
        cleanTerminated(blockedQueue);

        readyPanel.removeAll();
        SimpleNode<PCB> node = readyQueue.getpFirst();
        while (node != null) {
            readyPanel.add(createProcessCard(node.getData()));
            node = node.getpNext();
        }

        blockedPanel.removeAll();
        node = blockedQueue.getpFirst();
        while (node != null) {
            blockedPanel.add(createProcessCard(node.getData()));
            node = node.getpNext();
        }

        endedPanel.removeAll();
        node = terminatedQueue.getpFirst();
        while (node != null) {
            endedPanel.add(createProcessCard(node.getData()));
            node = node.getpNext();
        }

        readyPanel.revalidate();
        readyPanel.repaint();
        blockedPanel.revalidate();
        blockedPanel.repaint();
        endedPanel.revalidate();
        endedPanel.repaint();
    }

    private void cleanTerminated(Queue<PCB> queue) {
        SimpleNode<PCB> node = queue.getpFirst();
        while (node != null) {
            PCB pcb = node.getData();
            if (pcb.getStatus() == PCB.Status.TERMINATED) {
                queue.delete(pcb);
            }
            node = node.getpNext();
        }
    }

    public void logEvent(String message) {
        SwingUtilities.invokeLater(() -> {
            logTextArea.append(message + "\n");
            logTextArea.setCaretPosition(logTextArea.getDocument().getLength()); // auto-scroll
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        EndedQueue = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ReadyQueue = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        BlockedQueue = new javax.swing.JPanel();
        CPUArea = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        ProcessExecution = new javax.swing.JLabel();
        ID = new javax.swing.JLabel();
        PC = new javax.swing.JLabel();
        MAR = new javax.swing.JLabel();
        Status = new javax.swing.JLabel();
        Tipo = new javax.swing.JLabel();
        CicloGlobalReloj = new javax.swing.JLabel();
        PolicyChooser = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        LogArea = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        LoadFile = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EndedQueue.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.black));
        EndedQueue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(EndedQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 330, 400, 120));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Cola de Terminados:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Cola de Listos:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, -1, -1));

        ReadyQueue.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.black));
        ReadyQueue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(ReadyQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, 400, 120));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cola de Bloqueados:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, -1, -1));

        BlockedQueue.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.black));
        BlockedQueue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(BlockedQueue, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 180, 400, 120));

        CPUArea.setBackground(new java.awt.Color(255, 153, 153));
        CPUArea.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.black));
        CPUArea.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("CPU");
        CPUArea.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 40, -1));

        ProcessExecution.setText("Proceso en ejecucion");
        CPUArea.add(ProcessExecution, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        ID.setText("ID");
        CPUArea.add(ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, -1, -1));

        PC.setText("PC");
        CPUArea.add(PC, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, -1, -1));

        MAR.setText("MAR");
        CPUArea.add(MAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, -1, -1));

        Status.setText("Status");
        CPUArea.add(Status, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, -1, -1));

        Tipo.setText("Tipo");
        CPUArea.add(Tipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, -1, -1));

        jPanel1.add(CPUArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 220, 220));

        CicloGlobalReloj.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        CicloGlobalReloj.setForeground(new java.awt.Color(0, 0, 0));
        CicloGlobalReloj.setText("CICLO GLOBAL DEL RELOJ");
        jPanel1.add(CicloGlobalReloj, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        PolicyChooser.setBackground(new java.awt.Color(255, 153, 153));
        PolicyChooser.setForeground(new java.awt.Color(0, 0, 0));
        PolicyChooser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "HRRN", "Round Robin", "SJF", "SRTF", "Feedback" }));
        PolicyChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PolicyChooserActionPerformed(evt);
            }
        });
        jPanel1.add(PolicyChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Algoritmo de planificación");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        LogArea.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        jPanel1.add(LogArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 270, 120));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 880, 480));

        jMenu1.setText("Archivo");

        LoadFile.setText("Cargar...");
        LoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadFileActionPerformed(evt);
            }
        });
        jMenu1.add(LoadFile);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadFileActionPerformed
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ProcessLoader loader = new ProcessLoader(readyQueue, memoryManager, this);
            loader.loadFromCSV(file.getAbsolutePath(), clock.getGlobalCycle());
            actualizarColasEnGUI(); // refrescar la vista
        }
    }//GEN-LAST:event_LoadFileActionPerformed

    private void PolicyChooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PolicyChooserActionPerformed
        String selected = (String) PolicyChooser.getSelectedItem();

        switch (selected) {
            case "FCFS":
                schedulerManager.useFCFS();
                break;
            case "Round Robin":
                schedulerManager.useRoundRobin(3);
                break;
            case "SJF":
                schedulerManager.useSJF();
                break;
            case "SRTF":
                schedulerManager.useSRTF();
                break;
            case "HRRN":
                schedulerManager.useHRRN();
                break;
            case "Feedback":
                int[] quantums = {2, 4, 8};
                schedulerManager.useFeedback(quantums);
                break;
        }

        JOptionPane.showMessageDialog(this,
                "Algoritmo cambiado a: " + selected,
                "Planificación",
                JOptionPane.INFORMATION_MESSAGE);

        logEvent("→ Algoritmo cambiado a: " + selected);
    }//GEN-LAST:event_PolicyChooserActionPerformed

    public void setSimulationComponents(Clock clock, CPU cpu, Queue<PCB> readyQueue) {
        schedulerManager = new SchedulerManager(clock, cpu, readyQueue);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BlockedQueue;
    private javax.swing.JPanel CPUArea;
    private javax.swing.JLabel CicloGlobalReloj;
    private javax.swing.JPanel EndedQueue;
    private javax.swing.JLabel ID;
    private javax.swing.JMenuItem LoadFile;
    private javax.swing.JPanel LogArea;
    private javax.swing.JLabel MAR;
    private javax.swing.JLabel PC;
    private javax.swing.JComboBox<String> PolicyChooser;
    private javax.swing.JLabel ProcessExecution;
    private javax.swing.JPanel ReadyQueue;
    private javax.swing.JLabel Status;
    private javax.swing.JLabel Tipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
