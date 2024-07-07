package edu.hunre.course_management.exception;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
