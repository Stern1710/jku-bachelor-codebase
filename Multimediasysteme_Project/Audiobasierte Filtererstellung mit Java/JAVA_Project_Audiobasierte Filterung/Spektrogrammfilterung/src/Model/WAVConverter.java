package Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class has the needed methods, to convert the WAV file to a byte array.
 * Therefore it is necessary to know about the data segmentation and storage of WAV Files
 * @see https://de.wikipedia.org/wiki/RIFF_WAVE
 * @author Christopher Holzweber
 */
public class WAVConverter {

	private byte[] origFileData;

	// get information about the file
	private AudioFormat audioFormat;
	private AudioFileFormat fileFormat;
	private String filepath;
	
	/**
	 * uses the filepath, which the user can choose in the user interface.
	 * @param filepath String with path (relativ or absolute) to the file
	 */
	public WAVConverter(String filepath) {
		this.filepath = filepath;
		
		try {
			// get all bytes of the chosen file
			Path path = Paths.get(filepath);
			origFileData = Files.readAllBytes(path);

			// setting for audioinformation
			File sourceFile = new File(filepath);
			fileFormat = AudioSystem.getAudioFileFormat(sourceFile);
			audioFormat = fileFormat.getFormat();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("PROBLEMS while converting the WAV FILE");
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the sampling rate of the wav file
	 * 
	 * @return sampling rate int, which is stored in 4 bytes (24-28) of wav file
	 */
	public int getSamplingRate() {
		return (int) audioFormat.getSampleRate();
	}

	/**
	 * @return number of channels, which is stored on postion 22,23
	 */
	public int getChannelsNo() {
		return audioFormat.getChannels();
	}

	/**
	 * Block alignment, in bytes. The block alignment is the minimum atomic unit of
	 * data. For PCM data, the block alignment is the number of bytes used by a
	 * single sample, including data for both channels if the data is stereo. For
	 * example, the block alignment for 16-bit stereo PCM is 4 bytes (2 channels — 2
	 * bytes per sample).
	 * 
	 * Stored on position 32
	 * @return
	 */
	public int getBlockAlign() {
		return (int) audioFormat.getFrameSize();
	}

	/**
	 * main method of this class, which converts the given byte array The data
	 * starts at position 44(after the head)
	 * https://stackoverflow.com/questions/39295589/creating-spectrogram-from-wav-using-fft-in-java
	 * 
	 * @return the essential data of the file, which is not a byte array, but an
	 *         double array. might thorw excpetion, if it is not a stereo file of
	 *         blocksize 4byte!
	 */
	public double[] getDataArray() throws WAVFormatException {
		byte[] essentialData = Arrays.copyOfRange(origFileData, 44, origFileData.length);

		// Mono data of all channels get stored in the double array
		double[] convertedData = new double[essentialData.length / getBlockAlign()];

		// if we have a stereo file, we only deal with stereo files here with block size
		// of 4 bytes
		if (getChannelsNo() == 2 && getBlockAlign() == 4) {
			// var values for left and right values
			double left, right;
			for (int i = 0; 4 * i + 3 < essentialData.length; i++) {
				// in order to get a signed 16 bit result out of unsigned bytes cast to short
				left = (short) ((essentialData[4 * i + 1] & 0xff) << 8) | (essentialData[4 * i] & 0xff);
				right = (short) ((essentialData[4 * i + 3] & 0xff) << 8) | (essentialData[4 * i + 2] & 0xff);
				convertedData[i] = (left + right) / 2.0; // producing the average of left and right channel
			}

		} else {
			throw new WAVFormatException("Wrong format of the WAV FILE!");
		}
		return convertedData; // might be empty!
	}

	public String getOriginalFilePath() {
		return filepath;
	}
}
