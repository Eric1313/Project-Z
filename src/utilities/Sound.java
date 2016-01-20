package utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Handles sound for the game.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class Sound {
	AudioInputStream in;
	AudioFormat decodedFormat;
	AudioInputStream din;
	AudioFormat baseFormat;
	SourceDataLine line;
	private boolean loop;
	String file;
	private BufferedInputStream stream;

	/**
	 * Recreates the stream.
	 */
	public void reset() {
		try {
			stream = new BufferedInputStream(new FileInputStream(file));
			try {
				in = AudioSystem.getAudioInputStream(stream);
			} catch (UnsupportedAudioFileException e) {
			}
			din = AudioSystem.getAudioInputStream(decodedFormat, in);
			line = getLine(decodedFormat);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the stream.
	 */
	public void close() {
		try {
			line.close();
			din.close();
			in.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Constructs a new Sound object.
	 * 
	 * @param filename
	 *            the path of the file.
	 * @param loop
	 *            whether or not to loop the sound.
	 */
	public Sound(String filename, boolean loop) {
		this(filename);
		this.loop = loop;
	}

	/**
	 * Constructs a new Sound object.
	 * 
	 * @param filename
	 *            the path of the file.
	 */
	public Sound(String filename) {
		file = filename;
		this.loop = false;
		try {
			stream = new BufferedInputStream(new FileInputStream(filename));

			in = AudioSystem.getAudioInputStream(stream);
			din = null;

			if (in != null) {
				baseFormat = in.getFormat();

				decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16,
						baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);

				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				line = getLine(decodedFormat);
			}
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets data line from the file.
	 * 
	 * @param audioFormat
	 *            the audio format of the file.
	 * @return format of file
	 * @throws LineUnavailableException
	 *             when file is invalid.
	 */
	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine) AudioSystem.getLine(info);
		res.open(audioFormat);
		return res;
	}

	/**
	 * Plays the sound.
	 */
	public void play() {
		try {
			// Plays sound once
			boolean firstTime = true;
			while (firstTime || loop) {

				firstTime = false;
				byte[] data = new byte[4096];

				if (line != null) {

					line.start();
					int nBytesRead = 0;

					while (nBytesRead != -1) {
						nBytesRead = din.read(data, 0, data.length);
						if (nBytesRead != -1)
							line.write(data, 0, nBytesRead);
					}

					line.drain();
					line.stop();
					line.close();
					reset();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}