package it.playfellas.superapp.events.game;

import android.bluetooth.BluetoothDevice;

import it.playfellas.superapp.events.NetEvent;
import it.playfellas.superapp.tiles.Tile;
import lombok.Getter;

/**
 * Created by affo on 02/09/15.
 */
public class YourTurnEvent extends NetEvent {
    @Getter
    String playerAddress;
    @Getter
    Tile[] stack;

    public YourTurnEvent(BluetoothDevice player, Tile[] stack) {
        this.stack = stack;
        this.playerAddress = player.getAddress();
    }

    @Override
    public String toString() {
        String tos = "Player: " + playerAddress + "\n";
        for (int i = 0; i < stack.length; i++) {
            tos += "[" + i + "]: " + stack[i] + "\n";
        }
        return tos;
    }
}
