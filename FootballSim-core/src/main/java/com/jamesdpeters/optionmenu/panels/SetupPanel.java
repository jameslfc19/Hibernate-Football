package com.jamesdpeters.optionmenu.panels;

import com.jamesdpeters.entities.LeaguesEntity;
import com.jamesdpeters.entities.RegionsEntity;
import com.jamesdpeters.entities.TeamsEntity;
import com.jamesdpeters.gui.FrameStore;
import com.jamesdpeters.gui.combobox.ComboBox;
import com.jamesdpeters.gui.combobox.ComboBoxUtils;
import com.jamesdpeters.gui.combobox.ComboItem;
import com.jamesdpeters.utils.SessionStore;
import com.jamesdpeters.workers.SetupGame;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class SetupPanel extends SwingWorker{

    private JPanel panel;
    private ComboBox<ComboItem<RegionsEntity>> regions;
    private ComboBox<ComboItem<LeaguesEntity>> leagues;
    private ComboBox<ComboItem<TeamsEntity>> teams;
    private JButton setupButton;
    private Session session;

    private SetupPanel setupPanel;

    private boolean SETUP = false;

    private int leagueId = -1;
    private int teamId = -1;

    public SetupPanel(){
        System.out.println("new Setup Panel!");
        setupPanel = this;
        session = SessionStore.getSession();

        //Create panel.
        panel = new JPanel();
        panel.setLayout(new MigLayout(
                "al center center",
                "[center]",
                "[center]"
        ));

        //Add regions to panel.
        regions = getComboBox("Regions");
        regions.comboBox.addItemListener(regionsListener);
        addRow(regions);

        //Add leagues to panel.
        leagues = getComboBox("Leagues");
        leagues.comboBox.addItemListener(leagueListener);
        addRow(leagues);

        //Add leagues to panel.
        teams = getComboBox("Teams");
        addRow(teams);

        //Setup button
        setupButton = new JButton("Setup Game");
        setupButton.addActionListener(setupListener);
        panel.add(setupButton, "wrap, span 2");

        execute();
    }

    //SETUP BUTTON LISTENER.
    private ActionListener setupListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Setting up game.");
            showLoadingScreen();
            TeamsEntity teamsEntity = ComboBoxUtils.getSelectedItem(teams.comboBox);
            if(teamsEntity != null) teamId = teamsEntity.getId();

            SetupGame.setup(session,teamId,leagueId);
            session.close();
            synchronized (setupPanel) {
                SETUP = true;
                setupPanel.notifyAll();
            }
            System.out.println("Setup! "+SETUP);
        }
    };

    //REGION COMBOBOX LISTENER.
    private ItemListener regionsListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                RegionsEntity regionsEntity = ComboBoxUtils.getSelectedItem(regions.comboBox);
                int regionId = 0;
                if (regionsEntity != null) regionId = regionsEntity.getId();

                List<LeaguesEntity> leaguesEntities = session.createQuery("from LeaguesEntity where country.regionId="+regionId, LeaguesEntity.class).list();
                List<ComboItem<LeaguesEntity>> leagueItems = new ArrayList<>();
                leaguesEntities.forEach(leaguesEntity -> leagueItems.add(new ComboItem<>(leaguesEntity,leaguesEntity.getLeaguename())));
                ComboBoxUtils.updateFromList(leagueItems,leagues.comboBox);
            }
        }
    };

    //LEAGUES COMBOBOX LISTENER.
    @SuppressWarnings(value = "unchecked")
    private ItemListener leagueListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getStateChange() == ItemEvent.SELECTED){
                //Get currently selected league from Combo Box and then find the ID from it.int leagueId = 0;
                LeaguesEntity leaguesEntity = ComboBoxUtils.getSelectedItem(leagues.comboBox);
                if(leaguesEntity != null) leagueId = leaguesEntity.getId();

                List<TeamsEntity> teamsEntities = session.createQuery("from TeamsEntity where leagueid="+leagueId, TeamsEntity.class).list();
                List<ComboItem<TeamsEntity>> teamItems = new ArrayList<>();
                teamsEntities.forEach(teamsEntity -> teamItems.add(new ComboItem<>(teamsEntity,teamsEntity.getName())));
                ComboBoxUtils.updateFromList(teamItems,teams.comboBox);
            }
        }
    };

    private <T> ComboBox<ComboItem<T>> getComboBox(String title){
        ComboBox<ComboItem<T>> comboBox = new ComboBox<>();
        comboBox.title = new JLabel(title);
        comboBox.comboBox = new JComboBox<>();
        return comboBox;
    }

    private void addRow(ComboBox comboBox){
        panel.add(comboBox.title);
        panel.add(comboBox.comboBox, "wrap, growx");
    }

    private void showLoadingScreen(){
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.add(new JLabel("Loading..."));
        FrameStore.getFrame().validate();
    }

    @Override
    protected Object doInBackground() {
        System.out.println("Do in background!");

        //Update regions combobox.
        List<RegionsEntity> regionsEntityList = session.createQuery("from RegionsEntity ", RegionsEntity.class).list();
        List<ComboItem<RegionsEntity>> regionItems = new ArrayList<>();
        regionsEntityList.forEach(regionsEntity -> regionItems.add(new ComboItem<>(regionsEntity,regionsEntity.getName())));
        ComboBoxUtils.updateFromList(regionItems,regions.comboBox);
        return null;
    }

    @Override
    protected void done() {
        super.done();
    }

    public JComponent getPanel() {
        return panel;
    }

    public boolean isSETUP() {
        return SETUP;
    }
}
