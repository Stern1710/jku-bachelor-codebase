package at.jku.tk.mms.voice.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


import at.jku.tk.mms.voice.VoiceMemo;
import at.jku.tk.mms.voice.model.Recording;

public class VoiceMemoImpl implements Runnable {
	
	private boolean playing, recording;
	
	public AudioFormat audioFormat;
	
	private float fFrameRate = 44100.0F;
	
	private Recording lastRecording;
	
	private Recording nextToPlay;
	
	private VoiceMemo ui;
	
	public VoiceMemoImpl(VoiceMemo ui) {
		this.ui = ui;
		playing = false;
		recording = false;
		audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fFrameRate, 16, 2, 4, fFrameRate, false);
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public boolean isRecoding() {
		return recording;
	}
	
	public void startRecording() {
		recording = true;
		new Thread(this).start();
	}

	public void stopRecording() {
		recording = false;
	}

	public void startPlaying() {
		playing = true;
		new Thread(this).start();
	}

	public void stopPlaying() {
		playing = false;
	}
	
	public synchronized Recording getLastRecording() {
		try {
			wait();
			return lastRecording;
		} catch (InterruptedException e) {
		}
		return null;
	}
	
	private synchronized void setLastRecording(Recording r) {
		this.lastRecording = r;
		notify();
	}
	
	public void setNextToPlay(Recording r) {
		this.nextToPlay = r;
	}

	@Override
	public void run() {
		if(playing) {
			threadPlaying();
		}
		if(recording) {
			threadRecording();
		}
			
	}
	
	private void threadPlaying() {
		ByteArrayInputStream stream = new ByteArrayInputStream(nextToPlay.getPcmAudio());
		int length = nextToPlay.getPcmAudio().length;
		
		AudioInputStream audioStream = new AudioInputStream(stream, audioFormat, length);
		AudioFormat format = audioStream.getFormat();
		
		byte[] ba = new byte [1024];
		int numOfBytes = 0;
		
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			SourceDataLine sourceLine = (SourceDataLine)AudioSystem.getLine(info);
			sourceLine.open();
			sourceLine.start();
			
			while (playing) {
				numOfBytes = audioStream.read(ba, 0, 1024);
				if (numOfBytes == -1) {
					//Reset UI to enable all controls
					playing = false;
					break;
				}
				sourceLine.write(ba, 0, ba.length);
			}
			
			sourceLine.stop();
			sourceLine.close();	
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ui.updateUi();
	}
	
	private void threadRecording() {
		Recording r = new Recording();
		TargetDataLine audioLine;
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
		
		if (AudioSystem.isLineSupported(info)) {
			try {
				audioLine = (TargetDataLine)AudioSystem.getLine(info);
				audioLine.open(audioFormat);
				
				ByteArrayOutputStream out  = new ByteArrayOutputStream();
				int numBytesRead;
				byte[] data = new byte[64];
				
				audioLine.start();
				while (recording) {
					numBytesRead =  audioLine.read(data, 0, data.length);
					out.write(data, 0, numBytesRead);
					if (numBytesRead == -1) {
						break;
					}
				}
				
				audioLine.stop();
				audioLine.close();
				
				r.setPcmAudio(out.toByteArray());
				setLastRecording(r);

			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		
		ui.updateUi();
	}
	
}
