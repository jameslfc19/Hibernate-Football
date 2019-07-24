package optionmenu.menus;

import frameworks.Season;
import gui.InputField;
import gui.Output;
import optionmenu.options.Option;
import utils.ASCII;
import utils.InputUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public abstract class Menu {

    private InputField inputField;
    private Season season;
    private Menu parentMenu;
    private JFrame frame;
    private JPanel panel;

    public Menu(JFrame frame, Season season){
        this.season = season;
        this.frame = frame;
        //inputField = InputUtil.getInputField();
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    }

    public Menu(JFrame frame, Season season, Menu parentMenu){
        this(frame, season);
        this.parentMenu = parentMenu;
    }

    public abstract ArrayList<Option> getOptions();

    public abstract ArrayList<Menu> getSubMenus();

    public abstract String getMenuName();

    public Menu getParentMenu(){
        return parentMenu;
    }


    /**
     * Displays menu to console and waits for selection.
     */
    public void consoleOpen(){
        Output.clear();
        System.out.println(ASCII.title);

        System.out.println("------------");
        System.out.println(getMenuName());
        System.out.println("------------");

        int options = 0;
        if(getOptions() != null) {
            for (int i = 0; i < getOptions().size(); i++) {
                Option option = getOptions().get(i);
                System.out.println((options + 1) + ". " + option.getTitle() + " - " + option.getDescription());
                options++;
            }
        }
        if(getSubMenus() != null) {
            for (int i = 0; i < getSubMenus().size(); i++) {
                Menu subMenu = getSubMenus().get(i);
                System.out.println((options + 1) + ". " + subMenu.getMenuName());
                options++;
            }
        }
        if(getParentMenu() != null){
            Menu menu = getParentMenu();
            System.out.println((options + 1) + ". Return to " + menu.getMenuName());
        }

        int menuChoice = inputField.nextInt();

        while(!isValidMenuChoice(menuChoice)){
            System.out.println("Invalid menu choice! Try again.");
            menuChoice = inputField.nextInt();
        }

        displayConsoleMenu(menuChoice);

//        Option option = getOptions().get(menuChoice-1);
//        option.display();
    }

    private boolean isValidMenuChoice(int i){
        int size = 0;
        if(getOptions() != null) size += getOptions().size();
        if(getSubMenus() != null) size += getSubMenus().size();
        if(getParentMenu() != null) size++;

        return (i > 0) && (i <= size);
    }

    private void displayConsoleMenu(int menuChoice){
        //Menu sizes
        int optionSize = 0, subMenuSize = 0;
        if(getOptions() != null) optionSize = getOptions().size();
        if(getSubMenus() != null) subMenuSize = getSubMenus().size();

        if(menuChoice <= optionSize){
            getOptions().get(menuChoice-1).display();
        }
        if(menuChoice <= (optionSize+subMenuSize)){
            getSubMenus().get(menuChoice-getOptions().size()-1).consoleOpen();
        }
        else {
            getParentMenu().consoleOpen();
        }
    }

    public void setupGuiElements(){
        if(getOptions() != null)
            for(Option option : getOptions()){
                JButton button = getButton(option.getTitle());
                button.addActionListener(e -> option.guiDisplay());
                panel.add(button);
            }
        if(getSubMenus() != null)
            for(Menu menu : getSubMenus()){
                JButton button = getButton(menu.getMenuName());
                button.addActionListener(e -> {
                    menu.setupGuiElements();
                    menu.display();
                    });
                panel.add(button);
            }

        if(getParentMenu() != null){
            JButton button = getButton("Back");
            button.addActionListener(e -> {
                getParentMenu().display();
            });
            panel.add(button);
        }
    }

    public void display(){
        frame.getContentPane().removeAll();
        frame.repaint();
        frame.add(panel);
        frame.pack();
        frame.setVisible( true );
        frame.setSize(500,500);
    }

    public Season getSeason(){
        return season;
    }

    private JButton getButton(String text){
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }

    public JFrame getFrame() {
        return frame;
    }
}
