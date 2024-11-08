package de.dieploegers.develop.idea.shellfilter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.util.Pair;
import de.dieploegers.develop.idea.shellfilter.beans.CommandBean;
import de.dieploegers.develop.idea.shellfilter.error.ShellCommandErrorException;
import de.dieploegers.develop.idea.shellfilter.error.ShellCommandNoOutputException;
import icons.ShellFilterIcons;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class FilterAction extends AnAction {
    @NonNls
    private static final String CUSTOM_ID = "_custom";

    private final Logger LOG = Logger.getInstance(FilterAction.class);

    @NonNls
    private final ResourceBundle resourceBundle =
        ResourceBundle.getBundle("messages.shellfilter");

    private String cat(final InputStream is) throws IOException {
        LOG.debug("Converting stdout stream into a string"); // NON-NLS
        byte[] buf = new byte[65536];
        int offset = 0;
        int bytesRead;
        while ((bytesRead = is.read(buf, offset, 8192)) != -1) {
            offset += bytesRead;
            if (offset + 8192 >= buf.length) {
                final byte[] newBuf = new byte[buf.length * 3];
                System.arraycopy(buf, 0, newBuf, 0, offset);
                buf = newBuf;
            }
        }
        return new String(buf, 0, offset);
    }

    @Override
    public void actionPerformed(final AnActionEvent anActionEvent) {

        LOG.debug("Shell Filter was invoked. Fetching data from event.");

        final Editor editor =
            anActionEvent.getData(CommonDataKeys.EDITOR);

        if (editor == null) {
            LOG.error("Internal error. Editor not found");
            return;
        }

        LOG.debug("Building up command list from settings and custom command");

        final Settings settings = Settings.getInstance();
        final List<CommandBean> commands = new ArrayList<>(
            settings.getCommands()
        );

        commands.add(0,
            new CommandBean(resourceBundle.getString("custom"),
                CUSTOM_ID,
                true));

        LOG.debug("Building up command list popup");

        final String lastSelectedCommand =
            Settings.getInstance().getLastSelectedCommand();

        final IPopupChooserBuilder<CommandBean> popupChooserBuilder = JBPopupFactory.getInstance()
            .createPopupChooserBuilder(commands);

        if (lastSelectedCommand != null) {

            for (final CommandBean command : commands) {
                if (command.getName().equals(lastSelectedCommand)) {
                    popupChooserBuilder.setSelectedValue(command, true);
                    break;
                }
            }
        }

        final JBPopup popup = popupChooserBuilder
            .setItemChosenCallback(
                selectedCommand -> {
                    Settings.getInstance().setLastSelectedCommand(
                        selectedCommand.getName()
                    );

                    final CommandBean command;
                    if (selectedCommand.getCommand().equals(CUSTOM_ID)) {
                        LOG.debug("Custom command selected. Show input dialog");

                        final CommandBean lastCustomCommand =
                            Settings.getInstance().getLastCustomCommand();

                        final Pair<String, Boolean> customCommand = getCustomCommand(lastCustomCommand);
                        if (customCommand == null) return;

                        command = new CommandBean("Custom",
                            customCommand.getFirst(),
                            customCommand.getSecond()
                        );

                        LOG.debug("Storing last custom command");

                        lastCustomCommand.setCommand(
                            customCommand.getFirst()
                        );
                        lastCustomCommand.setRemoveTrailingNewline(
                            customCommand.getSecond()
                        );
                    } else {
                        LOG.debug(
                            String.format(
                                "Command %s selected.", //NON-NLS
                                selectedCommand.getName()
                            )
                        );

                        if (StringUtils.isEmpty(selectedCommand.getCommand())) {
                            LOG.debug("Command is empty. Canceling");
                            return;
                        }

                        command = selectedCommand;
                    }

                    runCommand(command, editor);
                }
            )
            .createPopup();

        LOG.debug("Showing popup");
        popup.showInBestPositionFor(editor);
    }

    @Nullable
    protected Pair<String, Boolean> getCustomCommand(final CommandBean lastCustomCommand) {
        final Pair<String, Boolean> customCommand =
            Messages.showInputDialogWithCheckBox(
                resourceBundle.getString("dialog.custom.message"),
                resourceBundle.getString("dialog.custom.title"),
                resourceBundle.getString(
                    "configuration.command.trim"),
                lastCustomCommand.isRemoveTrailingNewline(),
                true,
                ShellFilterIcons.TOOLBAR_ICON,
                lastCustomCommand.getCommand(),
                null
            );

        if (StringUtils.isEmpty(customCommand.getFirst())) {
            LOG.debug("No custom command specified. Canceling");
            return null;
        }
        return customCommand;
    }

    protected void runCommand(final CommandBean command, final Editor editor) {

        LOG.debug("Running command");

        final String selectedText;

        if (editor.getSelectionModel().hasSelection()) {
            selectedText =
                editor.getSelectionModel().getSelectedText();
            LOG.debug("Providing this text as input: ", selectedText); //NON-NLS
        } else {
            LOG.debug(
                "No text was selected. Providing the whole file to the command."
            );

            selectedText =
                editor.getDocument().getText();
        }

        String replacement = null;
        try {
            replacement = process(command.getCommand(), selectedText);
        } catch (final ShellCommandErrorException e) {
            LOG.warn(e);
            Messages.showErrorDialog(
                e.getMessage(),
                resourceBundle.getString("dialog.error.commanderror.title")
            );
            return;
        } catch (final ShellCommandNoOutputException e) {
            LOG.warn(e);
            Messages.showErrorDialog(
                e.getMessage(),
                resourceBundle.getString("dialog.error.nooutput.title")
            );
            return;
        } catch (final IOException e) {
            final String error = String.format(
                resourceBundle.getString("dialog.error.executing.message"),
                e.getMessage()
            );
            LOG.error(error, e);
            Messages.showErrorDialog(
                error,
                resourceBundle.getString("dialog.error.executing.title")
            );
        } catch (final InterruptedException e) {
            final String error = String.format(
                resourceBundle.getString("dialog.error.interrupted.message"),
                e.getMessage()
            );
            LOG.error(error, e);
            Messages.showErrorDialog(
                error,
                resourceBundle.getString("dialog.error.interrupted.title")
            );
        }

        if (replacement != null) {
            if (command.isRemoveTrailingNewline()) {
                replacement = replacement.trim();
            }

            final String replacementText = replacement;

            final Runnable filterRunnable;
            if (editor.getSelectionModel().hasSelection()) {
                LOG.debug(
                    "Replacing selection with this text: ", //NON-NLS
                    replacement
                );
                filterRunnable =
                    () -> editor.getDocument().replaceString(
                        editor.getSelectionModel()
                            .getSelectionStart(),
                        editor.getSelectionModel()
                            .getSelectionEnd(),
                        replacementText
                    );
            } else {
                LOG.debug(
                    "Inserting this text into the editor: ", //NON-NLS
                    replacement
                );
                filterRunnable =
                    () -> editor.getDocument().insertString(
                        editor.getCaretModel()
                            .getCurrentCaret()
                            .getOffset(),
                        replacementText
                    );
            }

            WriteCommandAction.runWriteCommandAction(
                editor.getProject(),
                filterRunnable
            );
        } else {
            LOG.debug("Did not get anything back from shell. Canceling");
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(final AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            LOG.debug("No editor selected, disabling action.");
        } else {
            LOG.debug("Editor selected, enabling action");
        }
        e.getPresentation().setVisible(editor != null);
    }

    private String process(final String command,
                           final String filterContent)
        throws
        ShellCommandErrorException,
        ShellCommandNoOutputException,
        IOException, InterruptedException
    {
        LOG.debug("Writing command into a temporary file");

        final File commandFile;
        final FileWriter commandFileWriter;
        try {
            commandFile = File.createTempFile("shellfilter", "command");
            commandFileWriter = new FileWriter(commandFile);
            commandFileWriter.write(command);
            commandFileWriter.close();
        } catch (final IOException e) {
            final String error = String.format(
                resourceBundle.getString(
                    "dialog.error.writingcommandfile.message"),
                e.getMessage()
            );
            LOG.error(error, e);
            Messages.showErrorDialog(
                error,
                resourceBundle.getString("dialog.error.writingcommandfile.title")
            );
            return null;
        }

        LOG.debug("Building up command");

        final String shellCommand = Settings.getInstance().getShellCommand();
        final List<String> cmd = new ArrayList<>();

        if (shellCommand.contains("%s")) { //NON-NLS
            cmd.addAll(Arrays.asList(String.format(shellCommand, commandFile.getPath()).split(" ")));
        } else {
            cmd.add(shellCommand);
            cmd.add(commandFile.getPath());
        }

        LOG.debug("Running command ", shellCommand); //NON-NLS

        final Process p;
        final DataInputStream pStdout;
        final DataInputStream pStdErr;
        final DataOutputStream pStdin;
        final String output;

        try {
            p = Runtime.getRuntime().exec(cmd.toArray(new String[0]));

            pStdout = new DataInputStream(p.getInputStream());
            if (!filterContent.isEmpty()) {
                LOG.debug("Piping selected text into stdin of the shell");
                pStdin = new DataOutputStream(p.getOutputStream());
                pStdin.write(filterContent.getBytes());
                pStdin.close();
            }
            output = cat(pStdout);

            p.waitFor();

            final int exitValue = p.exitValue();
            if (exitValue != 0) {
                pStdErr = new DataInputStream(p.getErrorStream());
                final String commandError = cat(pStdErr);
                final String error = String.format(
                    resourceBundle.getString("error.commanderror"),
                    cmd,
                    exitValue,
                    output,
                    commandError
                );
                throw new ShellCommandErrorException(error);
            }

            if (StringUtils.isEmpty(output)) {
                final String error = String.format(
                    resourceBundle.getString("error.nooutput"),
                    cmd
                );
                throw new ShellCommandNoOutputException(error);
            }
        } finally {
            if (!commandFile.delete()) {
                LOG.warn(
                    String.format(
                        "Temporary file %s can not be deleted.", //NON-NLS
                        commandFile.getPath()
                    )
                );
            }
        }

        return output;
    }
}
