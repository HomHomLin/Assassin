package com.meetyou.assassin.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

/**
 * Created by Linhh on 17/5/31.
 */

public class PluginImpl extends Transform implements Plugin<Project> {
    def props = new Properties()
    void apply(Project project) {
        /*project.task('testTask') << {
             println "Hello gradle plugin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
         }

         //task耗时监听
         project.gradle.addListener(new TimeListener())
    */
        //读写配置
        new File("assassin.properties").withInputStream {
            stream -> props.load(stream)
        }

        //遍历class文件和jar文件，在这里可以进行class文件asm文件替换
        def android = project.extensions.getByType(AppExtension);
        android.registerTransform(this)
    }


    @Override
    public String getName() {
        return "PluginImpl";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println '//===============asm visit start===============//'
        //配置文件读取
        Iterator<String> it = props.stringPropertyNames().iterator();
        ArrayList<AssassinDO> propsItem = new ArrayList<>();
        String option  = AssassinMethodClassVisitor.OPTION_DEFAULT;
        while(it.hasNext()){
            String key = it.next();
            String v = props[key];
            //打印配置
            println key + "=" + v
            if(key.equals(AssassinMethodClassVisitor.OPTION_NAME)){
                option = v;
                continue;
            }
            //将配置数据封装
            AssassinDO assassinDO = new AssassinDO();
            String[] itemKey = key.split("\\.");
            assassinDO.mate = itemKey[0];
            assassinDO.type = itemKey[1];
            assassinDO.key = itemKey[2];
            assassinDO.value = v;

            println assassinDO.toString()

            propsItem.add(assassinDO);
        }
        //遍历inputs里的TransformInput
        inputs.each { TransformInput input ->
            //遍历input里边的DirectoryInput
            input.directoryInputs.each {
                DirectoryInput directoryInput ->
                    //是否是目录
                    if (directoryInput.file.isDirectory()) {
                        //遍历目录
                        directoryInput.file.eachFileRecurse {
                            File file ->
                                def filename = file.name;
                                def name = file.name
                                //这里进行我们的处理 TODO
                                if (name.endsWith(".class") && !name.startsWith("R\$") &&
                                        !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {
                                    ClassReader classReader = new ClassReader(file.bytes)
                                    ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
                                    ClassVisitor cv = new AssassinMethodClassVisitor(classWriter, option, propsItem)
                                    classReader.accept(cv, EXPAND_FRAMES)
                                    byte[] code = classWriter.toByteArray()
                                    FileOutputStream fos = new FileOutputStream(
                                            file.parentFile.absolutePath + File.separator + name)
                                    fos.write(code)
                                    fos.close()
                                    AssassinMethodClassVisitor
                                }
                                println '//PluginImpl find file:' + file.getAbsolutePath()
                                //project.logger.
                        }
                    }
                    //处理完输入文件之后，要把输出给下一个任务
                    def dest = outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    FileUtils.copyDirectory(directoryInput.file, dest)
            }


            input.jarInputs.each { JarInput jarInput ->
                /**
                 * 重名名输出文件,因为可能同名,会覆盖
                 */
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                println '//PluginImpl find Jar:' + jarInput.getFile().getAbsolutePath()

                //处理jar进行字节码注入处理 TODO

                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)

                FileUtils.copyFile(jarInput.file, dest)
            }
        }
        println '//===============asm visit end===============//'

    }
}