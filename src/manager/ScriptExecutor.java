package manager;

import com.intellij.openapi.project.Project;
import ui.CopyInfoDialog;
import utils.CloseableUtils;
import utils.TextUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScriptExecutor {

    public static void executeScript(Project project, String cmd, boolean showResult) {
        new Thread(() -> executeScriptInternal(project, cmd, showResult)).start();
    }

    private static void executeScriptInternal(Project project, String cmd, boolean showResult) {
        InputStream errorStream = null;
        InputStream resultStream = null;
        try {
            String projectBasePath = project.getBasePath();
            String newCmd = Env.appendEnvParams(cmd);
            Process resultProcess = Runtime.getRuntime().exec(newCmd, null, new File(projectBasePath));
            resultProcess.waitFor();
            //优先获取错误流
            String line;
            errorStream = resultProcess.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            StringBuilder errorResultBuilder = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorResultBuilder.append(line).append("\n");
            }
            if (!TextUtil.isEmpty(errorResultBuilder.toString())) {  //执行脚本错误
                System.out.println("---------------------Script ERROR LOG START---------------------");
                System.out.println(errorResultBuilder.toString());
                System.out.println("----------------------Script ERROR LOG END---------------------");
                showInfoDialog(errorResultBuilder.toString());
                return;
            }

            //成功展示信息
            resultStream = resultProcess.getInputStream();
            BufferedReader resultReader = new BufferedReader(new InputStreamReader(resultStream));
            StringBuilder resultBuilder = new StringBuilder();
            while ((line = resultReader.readLine()) != null) {
                resultBuilder.append(line).append("\n");
            }
            if (TextUtil.isEmpty(resultBuilder.toString())) {
                return;
            }
            System.out.println("---------------------Script LOG START---------------------");
            System.out.println(resultBuilder.toString());
            System.out.println("----------------------Script LOG END---------------------");
            if (showResult) {
                showInfoDialog(resultBuilder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showInfoDialog(e.toString());
        } finally {
            CloseableUtils.close(errorStream);
            CloseableUtils.close(resultStream);
        }
    }

    private static void showInfoDialog(String info) {
        SwingUtilities.invokeLater(() -> CopyInfoDialog.showInfoDialog(info));
    }
}
