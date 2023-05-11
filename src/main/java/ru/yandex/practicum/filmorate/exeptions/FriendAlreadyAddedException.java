package ru.yandex.practicum.filmorate.exeptions;

public class FriendAlreadyAddedException extends RuntimeException{
    public FriendAlreadyAddedException(String message){
        super(message);
    }

}
