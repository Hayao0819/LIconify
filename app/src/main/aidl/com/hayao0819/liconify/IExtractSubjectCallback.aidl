package com.hayao0819.liconify;

interface IExtractSubjectCallback {
    void onStart(String message);
    void onResult(boolean success, String message);
}