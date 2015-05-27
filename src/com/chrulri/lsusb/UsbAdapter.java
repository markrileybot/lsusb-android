/******************************************************************************
 *  lsusb, an open source graphical USB host device list for Android          *
 *  Copyright (C) 2012  Christian Ulrich <chrulri@gmail.com>                  *
 *                                                                            *
 *  This program is free software: you can redistribute it and/or modify      *
 *  it under the terms of the GNU General Public License as published by      *
 *  the Free Software Foundation, either version 3 of the License, or         *
 *  (at your option) any later version.                                       *
 *                                                                            *
 *  This program is distributed in the hope that it will be useful,           *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             *
 *  GNU General Public License for more details.                              *
 *                                                                            *
 *  You should have received a copy of the GNU General Public License         *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.     *
 ******************************************************************************/

package com.chrulri.lsusb;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

final class UsbAdapter extends ArrayAdapter<UsbDevice> {

	private static final StringBuilder tmpBuf = new StringBuilder();
	private final UsbManager manager;
	private final LayoutInflater inflater;

	public UsbAdapter(Context context) {
		super(context, 0);
		this.manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void loadDevices() {
		setNotifyOnChange(false);
		clear();
		addAll(manager.getDeviceList().values());
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listitem_usb, parent, false);
		}

		UsbDevice device = getItem(position);
		TextView manufacturer = (TextView) convertView.findViewById(R.id.manufacturer);
		TextView product = (TextView) convertView.findViewById(R.id.product);
		TextView vid = (TextView) convertView.findViewById(R.id.vid);
		TextView pid = (TextView) convertView.findViewById(R.id.pid);
		TextView path = (TextView) convertView.findViewById(R.id.path);
		TextView serial = (TextView) convertView.findViewById(R.id.serial);

		vid.setText(zeroPad(Integer.toHexString(device.getVendorId()), 4));
		pid.setText(zeroPad(Integer.toHexString(device.getProductId()), 4));
		path.setText(device.getDeviceName());
		if(VERSION.SDK_INT >= 21) {
			manufacturer.setText(device.getManufacturerName());
			product.setText(device.getProductName());
			serial.setText(device.getSerialNumber());
		}
		return convertView;
	}

	private static String zeroPad(String text, int to) {
		if(text.length() < to) {
			tmpBuf.setLength(0);
			tmpBuf.append(text);
			while (tmpBuf.length() < to) {
				tmpBuf.insert(0, "0");
			}
			text = tmpBuf.toString();
		}
		return text;
	}
}
