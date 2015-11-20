package it.playfellas.superapp.events.bt;

import android.bluetooth.BluetoothDevice;

public class BTDisconnectedEvent extends BTEvent {

  public BTDisconnectedEvent(BluetoothDevice device) {
    super(device);
    this.message = "Disconnected";
  }
}
