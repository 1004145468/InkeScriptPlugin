package utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.JBTerminalPanel;

import javax.swing.*;

public class TerminalTools {

    public static void sendCmd(Project project, String cmd) {
        ToolWindow terminal = ToolWindowManager.getInstance(project).getToolWindow("Terminal");
        JComponent root = terminal.getComponent();
        terminal.show(() -> {
            JBTerminalPanel terminalPanel = ((JBTerminalPanel) ((JLayeredPane) ((JPanel) ((JPanel) ((JPanel) ((JPanel) ((JPanel) ((JPanel) root.getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0)).getComponent(0));
            //执行history命令
            terminalPanel.getTerminalOutputStream().sendString(cmd + "\n");
        });
    }
}
