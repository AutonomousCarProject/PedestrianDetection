package com.looi.looi;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;

/**
 * This class contains useful methods that deal with media (and we also have 
 * exitSystem()). These methods can be accessed through a LooiObject or by
 * calling the static methods referring to the class. This class is 
 * uninstantiable except by members of this package
 * @author peter_000
 */
public interface MiscellaneousMethods
    {
        /**
         * Shuts down the entire program
         */
        public static void exitSystem()
            {
                System.exit(0); 
            }
        /**
         * Loads an image
         * @param path The file location of the image
         * @return The Image
         */
        public static Image loadImage(String path)
            {
                return new ImageIcon(path).getImage();
            }
        /**
         * Loads a file
         * @param path The file location of the file
         * @return The File
         */
        public static File loadFile(String path)
            {
                return new File(path);
            }
        /**
         * Plays a sound
         * @param soundFile The sound in the form of a File
         */
        public static void playSound(File soundFile)
            {
                Clip clip;
                try 
                    {
                        clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(soundFile));
                        clip.start();
                    } 
                catch (Exception e) {}
            }
        public static void beep()
        {
            Toolkit t = Toolkit.getDefaultToolkit();
            t.beep();
        }
    }