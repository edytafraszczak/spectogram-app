package pl.edu.uksw;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

public class AudioUtils {
    public static String[] getAudioFileTypes() {
        AudioFileFormat.Type[] formatTypes = AudioSystem.getAudioFileTypes();
        String[] types = new String[formatTypes.length];
        for (int ii = 0; ii < types.length; ii++) {
            types[ii] = formatTypes[ii].getExtension();
        }
        return types;
    }

}
