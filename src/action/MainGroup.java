package action;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import entity.ScriptEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.ConfigParser;

import java.util.ArrayList;

public class MainGroup extends ActionGroup {

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        Presentation presentation = e.getPresentation();
        presentation.setVisible(project != null);
    }

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        ArrayList<ScriptEntity> mScriptEntities = ConfigParser.loadConfigFile(anActionEvent.getProject());
        if (mScriptEntities == null || mScriptEntities.size() < 1) {  // 不予许返回空
            AnAction[] defaultActions = new AnAction[1];
            defaultActions[0] = new EmptyAction();
            return defaultActions;
        }
        AnAction[] actions = new AnAction[mScriptEntities.size()];
        for (int i = 0, j = mScriptEntities.size(); i < j; i++) {
            actions[i] = new ScriptAction(mScriptEntities.get(i));
        }
        return actions;
    }
}
