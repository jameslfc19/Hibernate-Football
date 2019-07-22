package optionmenu.options;

import gui.InputField;
import gui.Output;
import optionmenu.menus.Menu;
import org.hibernate.Session;
import utils.InputUtil;
import utils.SessionStore;

import javax.swing.*;

public abstract class Option {

    private InputField inputField;
    protected Session session;
    private Menu menu;

    public Option(Menu menu){
        this.menu = menu; //Parent menu to this option.
        inputField = InputUtil.getInputField();
        session = SessionStore.getSession();
    }

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract Menu postRunMenu();

    protected abstract void run();

    protected abstract void consoleInfo();

    public void display(){
        Output.clear();
        System.out.println("--------------------------");
        System.out.println(getTitle());
        System.out.println("--------------------------");
        run();
        consoleInfo();
        System.out.println();
        System.out.println("Continue [Enter] ");
        inputField.nextLine();
        postRunMenu().consoleOpen();
    }

    public void guiDisplay(){
        run();
        consoleInfo();

        JFrame frame = getParentMenu().getFrame();
        if(getPanel() != null) {
            frame.getContentPane().removeAll();
            frame.repaint();
            frame.add(getPanel());
            frame.setVisible(true);
        }
    }

    public abstract JComponent getPanel();

    public Menu getParentMenu(){
        return menu;
    }


}
