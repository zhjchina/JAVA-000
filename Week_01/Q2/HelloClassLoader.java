

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 自定义ClassLoader，从Hello.xlass加载Hello类。调用Hello类后hello方法
 * 
 * @author ajian
 * @date 2020/10/21
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            // "Hello"是className，必须与字节码中的类名相同，this.defineClass()会校验
            Class<?> klass = new HelloClassLoader().findClass("Hello");
            Object obj = klass.newInstance();
            // System.out.println(obj);
            Method method = klass.getMethod("hello");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // Hello.xlass文件内容是Hello.class文件所有字节(x=255-x)处理后得到的
            byte[] bytes = Files.readAllBytes(Paths.get("Hello.xlass"));
            for (int i = 0; i < bytes.length; i++) {
                // 还原为原来的字节，255对应的byte是-1
                bytes[i] = (byte)(-1 - bytes[i]);
            }
            return this.defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }
}
