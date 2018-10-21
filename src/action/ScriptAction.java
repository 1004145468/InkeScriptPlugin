package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import entity.ScriptEntity;
import manager.ScriptExecutor;
import utils.TextUtil;

public class ScriptAction extends AnAction {

    private ScriptEntity mScriptEntity;

    public ScriptAction(ScriptEntity scriptEntity) {
        super(scriptEntity.name, scriptEntity.description, null);
        this.mScriptEntity = scriptEntity;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        String inputResult = Messages.showInputDialog(project, mScriptEntity.description, mScriptEntity.name, null,
                mScriptEntity.code, null);
        if (TextUtil.isEmpty(inputResult)) {
            return;
        }
        ScriptExecutor.executeScript(project, inputResult, mScriptEntity.result);
    }
}
