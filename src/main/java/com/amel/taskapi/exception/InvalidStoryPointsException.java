package com.amel.taskapi.exception;

public class InvalidStoryPointsException extends RuntimeException{
    public InvalidStoryPointsException(Integer storyPoint){
        super("Story points must be a positive Fibonacci number between 1 and 21 : " + storyPoint);
    }
}
