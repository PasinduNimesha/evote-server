package com.evote.server.exception;

public class CustomExceptions {

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    public static class OtpExpiredException extends RuntimeException {
        public OtpExpiredException(String message) {
            super(message);
        }
    }

    public static class InvalidOtpException extends RuntimeException {
        public InvalidOtpException(String message) {
            super(message);
        }
    }

    public static class DuplicateVoteException extends RuntimeException {
        public DuplicateVoteException(String message) {
            super(message);
        }
    }

    public static class VoteNotAllowedException extends RuntimeException {
        public VoteNotAllowedException(String message) {
            super(message);
        }
    }

    public static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }
}