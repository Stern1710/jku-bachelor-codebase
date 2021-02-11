package Model;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

/**
 * Offers methods to generate a mp3 from a wav and vice versa
 * @author Simon Sternbauer
 * @see Inspiration for code on http://www.sauronsoftware.it/projects/jave/manual.php
 */
public class MP3WavConverter {

	/**
	 * Converts an MP3-File to WAV equivalent
	 * @param filepath String with the exact absolute path to the file
	 * @return a new File that holds a reference to the generated WAV file
	 */
	public static File convertMP3ToWavFile(String filepath) {
		//Set path to input and output file, set properties and encoder
		String outputPath = System.getProperty("user.dir") + "\\converted\\" + new File(filepath).getName().replaceAll(".mp3", ".wav");
		File source = new File(filepath);
		File target = new File(outputPath);
		
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		
		EncodingAttributes attr = new EncodingAttributes();
		attr.setFormat("wav");
		attr.setAudioAttributes(audio);
		
		Encoder encoder = new Encoder();
		try {
			encoder.encode(source, target, attr);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}

		return target;
	}
	
	/**
	 * Converts an WAV-File to the MP3 equivalent
	 * @param filepath String with the exact absolute path to the file
	 * @return a new File that holds a reference to the generated MP3 file
	 */
	public static File convertWAVToMP3File(String filepath) {
		//Set path to input and output file
		String outputPath = System.getProperty("user.dir") + "\\converted\\" + new File(filepath).getName().replaceAll(".wav", ".mp3");		
		File source = new File(filepath);
		File target = new File(outputPath);
		
		//Set target codec and other attrbites
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		
		//Set the encoding attributes and encode file
		EncodingAttributes attr = new EncodingAttributes();
		attr.setFormat("mp3");
		attr.setAudioAttributes(audio);
		
		Encoder encoder = new Encoder();
		try {
			encoder.encode(source, target, attr);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}

		return target;
	}
}
