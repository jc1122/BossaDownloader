package app.bossaAPI;

//http://technofovea.com/blog/archives/815
public interface JnaEnum<T> {
    int getIntValue();

    T getForValue(int i);
}
