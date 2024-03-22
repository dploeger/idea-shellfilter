package de.dieploegers.develop.idea.shellfilter.ui;

import de.dieploegers.develop.idea.shellfilter.Settings;
import de.dieploegers.develop.idea.shellfilter.beans.ConfigurationBean;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Configurable implements com.intellij.openapi.options.Configurable {
    private ConfigurationUI configurationComponent;
    private ConfigurationBean originalConfiguration;

    @Nls
    @Override
    public String getDisplayName() {
        return "Shell Filter";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        this.configurationComponent =
            new ConfigurationUI();
        final Settings settings =
            Settings.getInstance();
        this.originalConfiguration =
            settings.getConfigurationBean();

        this.configurationComponent.setData(this.originalConfiguration);
        return configurationComponent.getConfigurationPanel();
    }

    @Override
    public boolean isModified() {
        return this.configurationComponent.isModified(originalConfiguration);
    }

    @Override
    public void apply() {

        final ConfigurationBean configurationBean = new ConfigurationBean();
        configurationComponent.getData(configurationBean);

        final Settings settings =
            Settings.getInstance();

        settings.setFromConfigurationBean(configurationBean);
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

}
