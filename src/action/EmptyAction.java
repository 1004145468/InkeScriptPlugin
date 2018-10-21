package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class EmptyAction extends AnAction {

    public EmptyAction() {
        super("无");
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
    }
}
