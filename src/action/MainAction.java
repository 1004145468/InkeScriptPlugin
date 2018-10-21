package action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import ui.ScriptListDialog;

public class MainAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        Presentation presentation = e.getPresentation();
        presentation.setVisible(project != null);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        //打开脚本列表窗口
        ScriptListDialog dialog = new ScriptListDialog(project);
        //dialog.pack();
        dialog.setVisible(true);
    }
}
