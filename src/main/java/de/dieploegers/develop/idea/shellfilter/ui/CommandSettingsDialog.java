package de.dieploegers.develop.idea.shellfilter.ui;

import com.intellij.openapi.ui.DialogWrapper;
import de.dieploegers.develop.idea.shellfilter.beans.CommandBean;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CommandSettingsDialog extends DialogWrapper {
    private JPanel contentPane;
    private JTextArea command;
    private JTextField commandName;
    private JCheckBox trimLeadingNewlinesFromCheckBox;

    public CommandSettingsDialog() {
        super(false);
        init();
    }

    public void setData(final CommandBean data) {
        command.setText(data.getCommand());
        commandName.setText(data.getName());
        trimLeadingNewlinesFromCheckBox.setSelected(data.isRemoveTrailingNewline());
    }

    public void getData(final CommandBean data) {
        data.setCommand(command.getText());
        data.setName(commandName.getText());
        data.setRemoveTrailingNewline(trimLeadingNewlinesFromCheckBox.isSelected());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
