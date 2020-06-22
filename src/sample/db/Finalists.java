package sample.db;

import sample.objects.Finalist;

public class Finalists{ //extends FileIO<Finalist> {
    /*public Finalists() {
        super("finalists.txt", Finalist.class);
    }*/
    private static FileIO<Finalist> fileIO = new FileIO<>("finalists.txt", Finalist.class);

    public static FileIO<Finalist> getFileIO() {
        return fileIO;
    }
}
