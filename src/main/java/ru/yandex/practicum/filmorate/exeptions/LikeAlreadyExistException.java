package ru.yandex.practicum.filmorate.exeptions;

public class LikeAlreadyExistException extends RuntimeException{
    public LikeAlreadyExistException(String message){
        super(message);
    }
}
