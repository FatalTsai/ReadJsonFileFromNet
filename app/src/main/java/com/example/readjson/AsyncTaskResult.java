package com.example.readjson;

interface AsyncTaskResult<T> {
    public void taskFinish( T result );
}
