package com.disabledmallis;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void Log(Object obj) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS");
        System.out.println(ANSI_WHITE + "[" + ANSI_GREEN  + dateTime.format(formatter) + ANSI_WHITE + "]" + ANSI_WHITE + " [" + ANSI_PURPLE + "Java2Cpp" + ANSI_WHITE + "] " + obj.toString());
        //JOptionPane.showMessageDialog(null, ANSI_WHITE + "[" + ANSI_GREEN  + dateTime.format(formatter) + ANSI_WHITE + "]" + ANSI_WHITE + " [" + ANSI_PURPLE + "Java2Cpp" + ANSI_WHITE + "] " + obj.toString());
        JOptionPane pane= new JOptionPane("Error Code C2022:\n  Enable JIT!\n   Python 3 not found!!", JOptionPane.INFORMATION_MESSAGE);
        final JDialog dialog = pane.createDialog("Windows Error!!!");

        Timer timer = new Timer(2000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        try {
        BufferedImage image = ImageIO.read(new URL("https://cdn.discordapp.com/attachments/758557229653426186/771918318625488896/20201011_222910.jpg"));
        dialog.setIconImage(image);} catch (Exception e) {
            // ruftiesghtiuseghtuisaehtioueshtguos
        }
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        dialog.dispose();
    }
}
