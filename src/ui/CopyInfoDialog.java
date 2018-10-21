package ui;

import com.intellij.openapi.ide.CopyPasteManager;
import utils.TextUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class CopyInfoDialog extends JDialog {

    public static final String RESULT_INFO = "执行结果";

    private JPanel contentPane;
    private JTextArea textAreaContent;
    private JButton buttonCopy;
    private JScrollPane scrollPanel;

    public static void showInfoDialog(String content) {
        CopyInfoDialog dialog = new CopyInfoDialog();
        dialog.setContent(content);
        dialog.pack();
        //设置位置
        Dimension dialogSize = dialog.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((int) (screenSize.getWidth() - dialogSize.width) / 2, (int) (screenSize.getHeight() - dialogSize.height) / 2);
        dialog.setVisible(true);
    }

    public CopyInfoDialog() {
        setContentPane(contentPane);
        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setModal(false);
        setTitle(RESULT_INFO);
        scrollPanel.setBorder(null);
        textAreaContent.setFocusable(false);
        textAreaContent.setEditable(false);
        buttonCopy.addActionListener(e -> {
            String message = textAreaContent.getText();
            if (!TextUtil.isEmpty(message)) {
                CopyPasteManager.getInstance().setContents(new StringSelection(message));
            }
            dispose();
        });
    }

    public void setContent(String content) {
        textAreaContent.setText(content);
    }
}
