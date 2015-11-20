package it.playfellas.superapp.events;

import lombok.Getter;

/**
 * Created by Stefano Cappa on 03/08/15.
 */
public class PhotoEvent extends NetEvent {
    @Getter
    private byte[] photoByteArray;

    public PhotoEvent(byte[] photoByteArray) {
        this.photoByteArray = photoByteArray;
    }
}
