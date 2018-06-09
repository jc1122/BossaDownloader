package app.API;

public interface OnOffOperations {
    String initialize() throws IllegalStateException;

    // function for getting the information about verson of dll
    String getVersion();

    String shutdown();
}
