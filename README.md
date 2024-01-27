# Tool cho phép kiểm tra toàn bộ dự án và xuất ra report cho các trường hợp không thỏa mãn
### Giả sử architect quyết định rằng việc tổ chức viết code phải tuân theo quy định chặt chẽ về tổ chức và cấu trúc. Các nguyên lý được hướng dẫn như sau:
1. Các package trong dự án phải theo mẫu: com.companyname.* (*:tên bất kỳ)
2. Các class phải có tên là một danh từ hoặc cụm danh ngữ và phải bắt đầu bằng chữ hoa.
3. Mỗi lớp phải có một comment mô tả cho lớp. Trong comment đó phải có ngày tạo
(created-date) và author.
4. Các fields trong các class phải là danh từ hoặc cụm danh ngữ và phải bắt đầu bằng một
chữ thường.
5. Tất cả các hằng số phải là chữ viết hoa và phải nằm trong một interface.
6. Tên method phải bắt đầu bằng một động từ và phải là chữ thường
7. Mỗi method phải có một ghi chú mô tả cho công việc của method trừ phương thức
default constructor, accessors/mutators, hashCode, equals, toString.

___________
# Build Tool
### Công nghệ hỗ trợ
 1. en-pos-maxent.bin: mô hình gán nhãn từ loại cho tiếng Anh (verb hay noun...), được huấn luyện bằng thuật toán maxent.
    ``` js
    private static final String MODEL_PATH = "src/main/resources/en-pos-maxent.bin";
    ```
 3. org.apache.opennlp:opennlp-tools:2.3.1: thư viện Java cung cấp các công cụ xử lý ngôn ngữ tự nhiên.
    ``` js
    implementation ("org.apache.opennlp:opennlp-tools:2.3.1")
    ```
 5. JavaParser: phân tích các file Java
    ``` js
    implementation ("com.github.javaparser:javaparser-core:3.25.8")
    ```
### Nguyên tắc chung
1. Dùng DirExplorer để duyệt qua các files.
2. Trong mỗi file java được duyệt qua, dùng visitor để “ghé thăm” các thành phần trong file.
3. Override hàm visit với các tham số tùy đối tượng bạn muốn truy cập.
 
### Build
#### 1. POSTagging
- Lớp này sử dụng mô hình en-pos-maxent.bin để gán nhãn từ loại cho các từ trong tiếng Anh.
  ``` js
  public POSTagging() {
        try {
            InputStream inputStream = new FileInputStream(MODEL_PATH); //MODEL_PATH = "src/main/resources/en-pos-maxent.bin"
            model = new POSModel(inputStream);
            tagger = new POSTaggerME(model);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  ```
- Lớp này có hai phương thức isNoun và isVerb để kiểm tra xem một từ có phải là danh từ hoặc động từ hay không.
  ``` js
  public boolean isNoun(String word) {
        String[] token = SimpleTokenizer.INSTANCE.tokenize(word);
        String[] tags = tagger.tag(token);
        for (String tag : tags) {
            if (tag.startsWith("NN")) { //NN: Noun, singular or mass
                return true;
            }
        }
        return false;
    }
  ```
  ``` js
  public boolean isVerb(String word) {
        String[] token = SimpleTokenizer.INSTANCE.tokenize(word);
        String[] tags = tagger.tag(token);
        for (String tag : tags) {
            if (tag.startsWith("VB")) { //VB: Verb, base form
                return true;
            }
        }
        return false;
    }
  ```
  #### 2. LegalChecking
  - Lớp này sử dụng các biểu thức chính quy (Regular Expression) để kiểm tra tính hợp pháp của các gói, lớp, phương thức và trường
  - Ex: 
    ``` js
    public void checkLegalPackage(String pkgName) {
        String regex = "com\\.edu\\.fit.*";

        if (!pkgName.matches(regex)) {
            System.out.println("Package [" + pkgName + "] is invalid (must follow 'com.edu.fit.*' (*:anything)) ");
        }
    }
    ```
  #### 3. CommonOperations
  - Lớp này sử dụng DirExplorer để duyệt qua các file Java trong một thư mục và VoidVisitorAdapter để thăm các nút trong cây cú pháp trừu tượng (AST) của các file Java.
  - Gọi các phương thức kiểm tra hợp pháp từ lớp LegalChecking
    ``` js
    private static LegalChecking legalChecking = new LegalChecking();
    public static void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println(path);
            System.out.println(Strings.repeat("=", path.length()));
            try {
                new VoidVisitorAdapter<>() {
                    @Override
                    public void visit(PackageDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println("(Package) " + n.getNameAsString());
                        legalChecking.checkLegalPackage(n.getNameAsString()); //check
                    }

                    @Override
                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                        super.visit(n, arg);
                        System.out.println(" [ " + n.getBegin() + "] (Class) " + n.getNameAsString());
                        legalChecking.checkLegalClass(n.getNameAsString(), n); //check
                    }

                   .......

                }.visit(StaticJavaParser.parse(file), null);
                System.out.println();
            } catch (Exception e) {
                new RuntimeException(e);
            }
            return true;
        }).explore(projectDir);
    }
 
### Result
![image](https://github.com/HaThiPhuongLinh/Week02_Software-Architecture-and-Design/assets/109422010/931f322a-94e9-42a1-990b-101be1bfc375)
