package com.meetyou.assassin.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.aQute.lib.env.Env
import org.gradle.internal.impldep.org.apache.http.util.TextUtils
import org.gradle.util.TextUtil
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import sun.instrument.TransformerManager

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES

/**
 * Created by Linhh on 17/5/31.
 */

public class PluginImpl extends Transform implements Plugin<Project> {
    //metas
    HashMap<String, ArrayList<String>> mMetas = new HashMap<>();
    //project name
    String mProjectName;
    String mAssassinFile = "com/meetyou/assassin/pro/";
    String mRootDir;

    HashMap<String, ArrayList<AssassinDO>> process = new HashMap<>();
    String mReceiver;

    boolean isLibrary = false;

    void processFile(String type, String it){
        if(type.equals("receiver")){
            //接收器处理
            mReceiver = it.trim().split("\\;")[0];
            println "assassin----> receiver:" + mReceiver
            return;
        }
        ArrayList<AssassinDO> arrayList = process.get(type);
        if(arrayList == null){
            arrayList = new ArrayList<>();
        }
        String m = it.trim().split("\\;")[0];
        AssassinDO assassinDO = new AssassinDO();
        if(!m.trim().startsWith("*")){
            //包名
            String[] r = m.trim().split("\\*");
            assassinDO.des = r[0];//包名
            assassinDO.name = r[1];
        }else {
            String[] r = m.trim().split("\\.");
            if (r[0].trim().equals("**")) {
                //如果是两个*代表是全量
                assassinDO.des = "all";
            } else if (r[0].trim().equals("*")) {
                assassinDO.des = "normal";
            }
            assassinDO.name = r[1];
        }
        arrayList.add(assassinDO);
        println "assassin----> assassinDO" + assassinDO.toString()
        process.put(type, arrayList);
    }

    void apply(Project project) {
        mProjectName = project.name;
        println "--->root=" + project.rootDir;
        mRootDir = project.rootDir.absolutePath;
        println "--->pr=" + project.buildDir.absolutePath;
        /*project.task('testTask') << {
             println "Hello gradle plugin!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
         }

         //task耗时监听
         project.gradle.addListener(new TimeListener())
    */
        //读写配置
//        new File("assassin.properties").withInputStream {
//            stream -> props.load(stream)
//        }

        String type_default = "default";
        String type_insert = "insert";
        String type_replace = "replace";
        String type_finsert = "finsert";
        String type_freplace = "freplace";
        String type_receiver = "receiver";
        String curr_type = type_default;

        new File("assassin.pro").eachLine {
            if(!it.trim().startsWith("#")) {
                //#开头代表是注释,直接跳过
                if (it.trim().startsWith("}")) {
                    //终止
                    curr_type = type_default;
                }
                if (it.trim().startsWith("-insert")) {
                    //插入
                    curr_type = type_insert;
                } else if (it.trim().startsWith("-replace")) {
                    //替换
                    curr_type = type_replace;
                } else if (it.trim().startsWith("-receiver")) {
                    //监听器
                    curr_type = type_receiver;
                } else if(it.trim().startsWith("-matchreplace")){
                    //完整匹配替换
                    curr_type = type_freplace;
                } else if(it.trim().startsWith("-matchinsert")){
                    //完整匹配插入
                    curr_type = type_finsert;
                }
                //语法体
                if (it.trim().endsWith(";")) {
                    println curr_type + ":" + it
                    if (!curr_type.equals(type_default)) {
                        processFile(curr_type, it)
                    }
                }
            }
        }
        //遍历class文件和jar文件，在这里可以进行class文件asm文件替换
        if(project.plugins.hasPlugin("com.android.application")){
            println '===assassin apply application=====' + project.getName()
            def android = project.extensions.getByType(AppExtension);
            android.registerTransform(this)
        }else if(project.plugins.hasPlugin("com.android.library")){
            isLibrary = true;
            println '===assassin apply lib=====' + project.getName()
            def android = project.extensions.getByType(LibraryExtension);
            android.registerTransform(this)
        }else{
            println '===assassin apply other type=====' + project.getName()
        }

        println '==================apply end=================='
    }


    @Override
    public String getName() {
        return "Assassin";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        if (isLibrary) {
            return TransformManager.SCOPE_FULL_LIBRARY;
//            return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT, QualifiedContent.Scope.PROJECT_LOCAL_DEPS);
        }
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        println '==================assassin start=================='
        String filter = null;

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
                                    AssassinMethodClassVisitor cv = new AssassinMethodClassVisitor(classWriter, mAssassinFile + mProjectName,mReceiver, process)
                                    classReader.accept(cv, EXPAND_FRAMES)

                                    byte[] code = classWriter.toByteArray()
                                    FileOutputStream fos = new FileOutputStream(
                                            file.parentFile.absolutePath + File.separator + name)
                                    fos.write(code)
                                    fos.close()

                                    if(filter == null){
                                        String clazzName = cv.clazzName
                                        String[] c = clazzName.split("\\.")

                                        filter = file.parentFile.absolutePath.split(File.separator + c[0] + File.separator)[0]
                                    }

                                    if(cv.nlist != null) {
                                        for (AnnotationNode annotationNode : cv.nlist) {
                                            List vl = annotationNode.values;
                                            if (vl != null) {
                                                //vl.get(0).toString() 属性名字
                                                ArrayList<String> metaClazz = mMetas.get(vl.get(1));
                                                if (metaClazz == null){
                                                    metaClazz = new ArrayList<>();
                                                }
                                                if(!metaClazz.contains(cv.clazzName)){
                                                    metaClazz.add(cv.clazzName);
                                                }
                                                mMetas.put(vl.get(1), metaClazz);
                                                println cv.clazzName + ';' + vl.get(0).toString() + "=" + vl.get(1)
                                            }
                                        }
                                    }
                                    //println 'Assassin-----> assassin file:' + file.getAbsolutePath()
                                }
//                                println 'Assassin-----> find file:' + file.getAbsolutePath()
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
                //println 'Assassin-----> find Jar:' + jarInput.getFile().getAbsolutePath()

                //处理jar进行字节码注入处理 TODO

                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)

                FileUtils.copyFile(jarInput.file, dest)
            }
        }

        println 'filter:' + filter

        List<String> list;
        File dir = new File(mRootDir);
        dir.mkdirs()
        File file = new File(mRootDir + "/pro");
        if(file.exists()){
            //先读出原本的内容
            list = FileUtils.readLines(file)
        }

        if(isLibrary){
            //库,将数据写入配置文件
            if(list == null){
                list = new ArrayList<>();
            }
            for(Map.Entry<String, ArrayList<String>> entrySet: mMetas.entrySet()){
                String content_line = entrySet.key + "=";
                for(String s : entrySet.value){
                    content_line = content_line + s + "#"
                }
                list.add(content_line);
            }
            FileUtils.writeLines(file, list)
        }else{
            //主工程
            if(list != null){
                for(String str : list){
                    String[] c = str.split("\\=");
                    String key = c[0];
                    String[] v = c[1].split("\\#");
                    if(mMetas.containsKey(key)){
                        ArrayList<String> l = mMetas.get(key);
                        for(String s : v){
                            if(!TextUtils.isEmpty(s)){
                                l.add(s);
                            }
                        }
                        mMetas.put(key,l)
                    }
                }
            }
            //将meta输出到文件中
            String metafile = filter + File.separator + mAssassinFile;
            File fmeta = new File(metafile)
            fmeta.mkdirs()

            AssassinMaker maker = new AssassinMaker();

            FileOutputStream fos = new FileOutputStream(metafile + "AssassinMap.class")
            fos.write(maker.make(mAssassinFile + "AssassinMap", mMetas))
            fos.close()
        }




        println '==================assasin end=================='

    }

}