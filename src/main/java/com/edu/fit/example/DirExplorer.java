package com.edu.fit.example;

import java.io.File;

public class DirExplorer {
    /**
     * Phương thức handle được gọi mỗi khi một tệp hoặc thư mục đáp ứng điều kiện của Filter được tìm thấy trong quá trình duyệt
     */
    public interface FileHandler {
        void handle(int level, String path, File file);
    }

    /**
     * Phương thức interested được sử dụng để kiểm tra xem một tệp cụ thể có đáp ứng điều kiện cụ thể nào đó không (ex: đuôi .java)
     */
    public interface Filter {
        boolean interested(int level, String path, File file);
    }

    private FileHandler fileHandler;
    private Filter filter;

    public DirExplorer(FileHandler fileHandler, Filter filter) {
        this.fileHandler = fileHandler;
        this.filter = filter;
    }

    public void explore(File root){
        explore(0,"",root);
    }

    private void explore(int level, String path, File file) {
        if(file.isDirectory()){
            for(File child : file.listFiles()){
                explore(level+1,path+"/" + child.getName(),child);
            }
        } else {
            //Kiem tra xem tep co duoc "interested" boi Filter khong (endsWith .java)
            if(filter.interested(level, path, file)){
                //Neu co thi goi phuong thuc handle de xu ly
                fileHandler.handle(level, path, file);
            }
        }
    }

    public static void main(String[] args) {
        File projectDir = new File("C:\\Users\\ThanhTu\\Desktop\\DEMO_PT\\GiuaKy_PhanTanJava_2023");
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            return true;
        }).explore(projectDir);
    }

}
