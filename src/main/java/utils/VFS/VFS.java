package utils.VFS;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/*
 * oppa google style
 */
public class VFS implements VFSInterface{
    private String root;

    public VFS(String root){
        this.root = root;
    }

    public class FileIterator implements Iterator<String>{

        private Queue<File> files = new LinkedList<>();

        public FileIterator(String path){
            File file = new File(path);
            if(file.isDirectory()){
                File[] listFiles = file.listFiles();
                if (listFiles != null){
                    Collections.addAll(files, listFiles);
                }
            } else if(file.isFile()) {
                files.add(file);
            }
        }

        @Override
        public boolean hasNext() {
            return !files.isEmpty();
        }

        @Override
        public String next(){
            if(hasNext())
                return files.poll().getPath();  // getAbsolutePath();
            return null;
        }

        @Override
        public void remove() {

        }
    }

    @Override
    public boolean isFile(String path) {
        return new File(root+path).isFile();
    }

    @Override
    public Iterator<String> getIterator(String startDir) {
        return new FileIterator(this.root + startDir);
    }
}
