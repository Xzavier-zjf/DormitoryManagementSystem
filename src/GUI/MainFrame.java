package GUI;

import javax.swing.*;
import java.awt.*;
import System.DormitoryManager;
import System.StudentManager;
import System.EntryExitLogManager;
//import System.VisitorManager;
import System.VisitorEntryExitLogManager;


public class MainFrame extends JFrame {
    private DormitoryManager dormitoryManager;
    private StudentManager studentManager;
    private EntryExitLogManager entryExitLogManager;
    //private VisitorManager visitorManager;
    private VisitorEntryExitLogManager visitorEntryExitLogManager;


    public MainFrame() {
        dormitoryManager = new DormitoryManager();
        studentManager = new StudentManager();
        entryExitLogManager = new EntryExitLogManager();
        //visitorManager = new VisitorManager();
        visitorEntryExitLogManager = new VisitorEntryExitLogManager();

        StudentPanel studentPanel = new StudentPanel(studentManager, dormitoryManager); // 传递两个参数

        setTitle("学生宿舍管理系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("宿舍管理", new DormitoryPanel(dormitoryManager));
        tabbedPane.addTab("学生管理", studentPanel);
        tabbedPane.addTab("出入登记管理", new EntryExitLogPanel(entryExitLogManager));
        //tabbedPane.addTab("访客管理", new VisitorPanel(visitorManager));
        tabbedPane.addTab("访客出入登记管理", new VisitorEntryExitLogPanel(visitorEntryExitLogManager));

        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
