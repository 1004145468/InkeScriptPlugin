package utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.terminal.JBTerminalPanel;
import com.jediterm.terminal.TerminalOutputStream;

import javax.swing.*;

public class TerminalTools {

    public static void sendCmd(Project project, String cmd) {
        ToolWindow terminal = ToolWindowManager.getInstance(project).getToolWindow("Terminal");
        terminal.show(() -> {
            JComponent currentComponent = terminal.getComponent();
            while (currentComponent != null) {
                if (currentComponent instanceof JBTerminalPanel) {
                    TerminalOutputStream terminalOutputStream = ((JBTerminalPanel) currentComponent).getTerminalOutputStream();
                    if (terminalOutputStream == null) {
                        ToastUtil.make(project, MessageType.INFO, "首次创建终端对象，请重新执行");
                    } else {
                        terminalOutputStream.sendString(cmd + "\n");
                    }
                    break;
                }
                int componentCount = currentComponent.getComponentCount();
                if (componentCount <= 0) {
                    break;
                }
                currentComponent = (JComponent) currentComponent.getComponent(0);
            }
        });
    }
}
