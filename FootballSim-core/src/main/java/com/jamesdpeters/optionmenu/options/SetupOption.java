package com.jamesdpeters.optionmenu.options;

import com.jamesdpeters.optionmenu.menus.Menu;
import com.jamesdpeters.optionmenu.panels.SetupPanel;

import javax.swing.*;

public class SetupOption extends Option {

    private SetupPanel panel;

    public SetupOption(Menu menu) {
        super(menu);
    }

    @Override
    public String getTitle() {
        return "Setup";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Menu postRunMenu() {
        return null;
    }

    @Override
    protected void run() {
        panel = new SetupPanel();
    }

    @Override
    protected void consoleInfo() {

    }

    @Override
    public JComponent getPanel() {
        return panel.getPanel();
    }
}
