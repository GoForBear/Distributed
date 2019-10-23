import NIO.NIOProject;

public class Test {
    public static void main(String[] args) {
        String srcFileName = "D:\\taobao-tomcat-7.0.59\\temp\\D400_DAY_DEDUCTION_0020_20190927121212_0001.TXT";
        String dstFileName ="D:\\TEST.TXT";
        int result  = NIOProject.copyFile(srcFileName,dstFileName);
        System.out.println(result);
    }
}
