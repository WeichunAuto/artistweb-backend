package com.bobby.artistweb.exception;

/**
 * This class is used to capture the exception for invalid image type uploading.
 */
public class ImageTypeDoesNotSupportException extends Exception {
    public ImageTypeDoesNotSupportException(String message) {
        super(message);
    }

    public ImageTypeDoesNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
