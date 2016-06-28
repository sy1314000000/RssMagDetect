package com.jqd.rssmagdetect.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import com.jqd.rssmagdetect.model.SensorsDataManager;
import com.jqd.rssmagdetect.model.WiFiDataManager;
import com.jqd.rssmagdetect.ui.MainActivity;
import com.jqd.rssmagdetect.util.GlobalPara;

/**
 * @author jiangqideng@163.com
 * @date 2016-6-28 ����3:51:19
 * @description �ɼ��õ����ݵĴ洢
 */
public class FileManager {
	/**
	 * �������ÿ�δ������ļ���"dataRssi_at_1" �� "dataBssid.txt"
	 * dataRssi_at_1�����rssi�ʹ��������ݣ�ÿ��ʱ�̵�һ�����ݰ���n��AP��rssi��15������������ֵ���������ӽ�ȥ��
	 * dataBssid�����Wifi�ȵ�һЩ��Ϣ��˳�������Ķ�Ӧ ע�⣺����Ѵ��ڸ��ļ�����������������µ��ļ��Ḳ��֮ǰ�ġ���
	 * APP��һ�ο�����ȡ��BSSID˳��͹ر�APP�ٿ������вɼ��õ���BSSID˳���ǲ�һ���ģ�
	 * ����app���߼���ֻ�иı�λ�ú󣬴洢���ڴ�����ݲ����㣬����ͬһλ�õĶ�δ洢����Ӱ�졣
	 */
	public void saveData() {
		saveRssiAndSensors(); // ������
		saveWifiBssids(); // ��wifi��bssid
	}

	private void saveRssiAndSensors() {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath()
					+ "/CIPS-DataCollect");
			directory.mkdirs();
			File file = new File(directory, "dataRssi_at_"
					+ GlobalPara.getInstance().position_index + ".txt");
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStream fos = fOut;
			DataOutputStream dos = new DataOutputStream(fos);
			for (int i = 0; i < WiFiDataManager.getInstance().dataCount; i++) {
				// ��wifi��Rssi����
				for (int j = 0; j < WiFiDataManager.getInstance().dataBssid
						.size(); j++) {
					if (WiFiDataManager.getInstance().dataRssi.get(j)
							.containsKey(i)) {
						dos.writeInt(WiFiDataManager.getInstance().dataRssi
								.get(j).get(i));
					} else {
						dos.writeInt(0); // û�еĻ��ʹ�0
					}
				}
				// �洫�������ݣ�rss��������15��int
				SensorsDataManager sdm = SensorsDataManager.getInstance();
				dos.writeInt(sdm.dataMagnetic.get(0).get(i));
				dos.writeInt(sdm.dataMagnetic.get(1).get(i));
				dos.writeInt(sdm.dataMagnetic.get(2).get(i));
				dos.writeInt(sdm.dataOrientation.get(0).get(i));
				dos.writeInt(sdm.dataOrientation.get(1).get(i));
				dos.writeInt(sdm.dataOrientation.get(2).get(i));
				dos.writeInt(sdm.dataAccelerate.get(0).get(i));
				dos.writeInt(sdm.dataAccelerate.get(1).get(i));
				dos.writeInt(sdm.dataAccelerate.get(2).get(i));
				dos.writeInt(sdm.dataGyroscope.get(0).get(i));
				dos.writeInt(sdm.dataGyroscope.get(1).get(i));
				dos.writeInt(sdm.dataGyroscope.get(2).get(i));
				dos.writeInt(sdm.dataGravity.get(0).get(i));
				dos.writeInt(sdm.dataGravity.get(1).get(i));
				dos.writeInt(sdm.dataGravity.get(2).get(i));
			}
			dos.close();
			
			Toast toast = Toast.makeText(WiFiDataManager.getInstance().activity,
					"�洢����/CIPS-DataCollect��", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (FileNotFoundException e) {
			Toast.makeText(WiFiDataManager.getInstance().activity, "�洢ʧ�ܡ�",
					Toast.LENGTH_SHORT).show();
			return;
		} catch (IOException e) {
			Toast.makeText(WiFiDataManager.getInstance().activity, "�洢ʧ�ܡ�",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	private void saveWifiBssids() {
		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File directory = new File(sdCard.getAbsolutePath()
					+ "/CIPS-DataCollect");
			directory.mkdirs();
			File file = new File(directory, "dataBssid.txt");
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStream fos = fOut;
			DataOutputStream dos = new DataOutputStream(fos);
			for (String bssid : WiFiDataManager.getInstance().dataBssid
					.keySet()) {
				int j = WiFiDataManager.getInstance().dataBssid.get(bssid);
				String tempString = j + 1 + "  BSSID: ";
				dos.write(tempString.getBytes());
				dos.write(bssid.getBytes());
				dos.write("  SSID:".getBytes());
				dos.write(WiFiDataManager.getInstance().dataWifiNames.get(j)
						.getBytes());
				dos.write("\n".getBytes());
			}
			dos.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			return;
		}
	}
}