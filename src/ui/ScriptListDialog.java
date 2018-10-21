package ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import entity.ScriptEntity;
import manager.ScriptExecutor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.CloseableUtils;
import utils.ConfigParser;
import utils.TextUtil;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class ScriptListDialog extends JDialog {

    private static final String NAME = "脚本列表";
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;

    private JPanel contentPane;
    private JPanel mMainPanel;
    private JScrollPane mScrollPanel;

    private Project mProject;

    public ScriptListDialog(Project project) {
        this.mProject = project;
        initAttrs();
        setContentView();
        loadConfigFile();
    }

    private void initAttrs() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setModal(false);
        setTitle(NAME);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void setContentView() {
        setContentPane(contentPane);
        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        mScrollPanel.setBorder(null);
    }

    //读取配置文件
    private void loadConfigFile() {
        new Thread(() -> {
            ArrayList<ScriptEntity> scriptEntityList = ConfigParser.loadConfigFile(mProject);
            if (scriptEntityList == null || scriptEntityList.size() < 1) {
                return;
            }
            SwingUtilities.invokeLater(() -> setupScriptListView(scriptEntityList));
        }).start();
    }

    private void setupScriptListView(ArrayList<ScriptEntity> scriptEntityList) {
        mMainPanel.setLayout(new GridLayout(scriptEntityList.size(), 1, 1, 1));
        for (int i = 0; i < scriptEntityList.size(); i++) {
            ScriptEntity scriptEntity = scriptEntityList.get(i);
            mMainPanel.add(generaScriptItemView(i, scriptEntity));
        }
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalScrollBar = mScrollPanel.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMinimum());
        });
    }

    private JComponent generaScriptItemView(int i, ScriptEntity scriptEntity) {
        //创建item条目
        JPanel itemCell = new JPanel(new GridBagLayout());

        //添加脚本序号
        JLabel indexView = new JLabel((i + 1) + ".");
        indexView.setFont(new Font("Dialog", Font.BOLD, 14));
        GridBagConstraints indexConstraint = new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 0, 0), 0, 0);
        itemCell.add(indexView, indexConstraint);

        //添加标题
        JLabel scriptNameView = new JLabel(scriptEntity.name);
        GridBagConstraints scriptNameConstraint = new GridBagConstraints(1, 0, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 0, 0), 0, 0);
        itemCell.add(scriptNameView, scriptNameConstraint);

        //添加描述
        String description = scriptEntity.description;
        JTextArea descView = new JTextArea(description);
        descView.setEditable(false);
        descView.setFocusable(false);
        descView.setLineWrap(true);
        descView.setWrapStyleWord(true);
        descView.setRows(3);
        descView.setBackground(null);
        TitledBorder tb = BorderFactory.createTitledBorder("脚本描述");
        tb.setTitleJustification(TitledBorder.RIGHT);
        descView.setBorder(tb);
        GridBagConstraints descConstraints = new GridBagConstraints(1, 1, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0);
        itemCell.add(descView, descConstraints);

        //添加运行指令
        JTextField codeField = new JTextField(scriptEntity.code);
        codeField.setPreferredSize(new Dimension(200, 30));
        GridBagConstraints codeConstraint = new GridBagConstraints(1, 2, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0);
        itemCell.add(codeField, codeConstraint);

        //添加执行按钮
        URL executePath = ScriptListDialog.class.getResource("/images/execute.png");
        ImageIcon executeIcon = new ImageIcon(executePath);
        executeIcon.setImage(executeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        JButton executeView = new JButton(executeIcon);
        executeView.setPreferredSize(new Dimension(30, 30));
        GridBagConstraints executeConstraints = new GridBagConstraints(2, 1, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 12), 0, 0);
        executeView.addActionListener(e -> {
            String codeScript = codeField.getText();
            if (TextUtil.isEmpty(codeScript)) {
                return;
            }
            ScriptExecutor.executeScript(mProject, codeScript, scriptEntity.result);
        });
        itemCell.add(executeView, executeConstraints);

        return itemCell;
    }
}
