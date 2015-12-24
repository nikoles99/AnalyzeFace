package by.balinasoft.faceanalyzer.utils;

public interface ServerObserver<T, P> {

    void successExecute(T jsonObject);

    void failedExecute(P errorMessage);
}