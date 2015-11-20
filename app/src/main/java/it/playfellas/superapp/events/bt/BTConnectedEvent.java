package it.playfellas.superapp.events.bt;

import android.bluetooth.BluetoothDevice;

public class BTConnectedEvent extends BTEvent {

  public BTConnectedEvent(BluetoothDevice device) {
    super(device);
    this.message = "Connected";
  }
}
