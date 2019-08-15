package mantra.fingerauth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

import MFS100.DeviceInfo;
import MFS100.FingerData;
import MFS100.MFS100;
import MFS100.MFS100Event;

public class Authentication implements MFS100Event {
	public static final String filePath = System.getProperty("user.dir") + "\\resources\\finger.txt";

	static byte[] ansiTemplate;

	@Override
	public void OnCaptureCompleted(boolean arg0, int arg1, String arg2, FingerData fingerData) {
		ansiTemplate = fingerData.ANSITemplate();
		String encoded = Base64.encodeBase64String(ansiTemplate);
		System.out.println(" Capture completed!: " +encoded);
	}

	@Override
	public void OnPreview(FingerData arg0) {
	}

	public void writeFile(byte[] rawData) {
		try {
			FileOutputStream outputFile = new FileOutputStream(new File(filePath));
			outputFile.write(rawData);
			System.out.println("Writing file...");
			outputFile.close();
		} catch (IOException e) {
			System.out.println(" File Writing Exception: " + e);
		}
	}

	private void writeBytesToFile(String FileName, byte[] Bytes) {
		try {
			String FilePath = System.getProperty("user.dir") + "\\resources";
			File file = new File(FilePath);
			if (!file.exists()) {
				file.mkdir();
			}
			FilePath += "\\" + FileName;
			FileOutputStream fos = new FileOutputStream(FilePath);
			fos.write(Bytes);
			fos.close();
		} catch (Exception ex) {
		}
	}

	public static void main(String[] args) throws InterruptedException {
		scanFinger();
	}

	public static void scanFinger() throws InterruptedException {
		
		Authentication event = new Authentication();
		MFS100 mfs = new MFS100(event);
		boolean isConnected = mfs.IsConnected();
		System.out.println(" isConnected: " + isConnected);

		String sdkVersion = mfs.GetSDKVersion();
		System.out.println(" SDK Version: " + sdkVersion);

		DeviceInfo deviceInfo = null;
		int ret = mfs.Init();
		if (ret != 0) {
			System.out.println(mfs.GetErrorMsg(ret));
		} else {
			deviceInfo = mfs.GetDeviceInfo();
			System.out.println(" Device info: " + deviceInfo);
		}

		int startCapture = mfs.StartCapture(55, 5000, true);
		System.out.println(" Start: " + startCapture);
		Thread.sleep(5000);
		FingerData fingerData = new FingerData();
		startCapture = mfs.AutoCapture(fingerData, 5000, true, true);

		int score = 0;
		score = mfs.MatchANSI(fingerData.ANSITemplate(), ansiTemplate);
		if(score < 14000){
			System.out.println(" Finger print does not matched!");
		} else {
			System.out.println(" Finger print matched!");
		}
		System.out.println(score);
	}
}
