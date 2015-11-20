package it.playfellas.superapp.events.bt;

import android.bluetooth.BluetoothDevice;

public class BTErrorEvent extends BTEvent {

  public BTErrorEvent(BluetoothDevice device, String msg) {
    super(device);
    this.message = msg;
  }
}
