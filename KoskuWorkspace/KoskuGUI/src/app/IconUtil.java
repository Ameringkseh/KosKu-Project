package app;

import javax.swing.*;
import java.awt.*;
import utils.RoundedButton;

public class IconUtil {
    public static ImageIcon getIcon(String name, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(IconUtil.class.getResource("/Icons/" + name));
            Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (Exception e) {
            System.err.println("Icon not found: " + name);
            return null;
        }
    }

    public static JButton createButton(String text, String iconName, int width, int height) {
        Icon icon = getIcon(iconName, width, height);
        return new RoundedButton(text, icon);
    }
}
