package com.meetyou.assassin.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Linhh on 17/5/31.
 */

public class AssassinMethodClassVisitor extends ClassVisitor {

    public final static String OPTION_NORAML = "normal";
    public final static String OPTION_ALL = "all";

    public String mReveiver = "com/meetyou/assassin/impl/AssassinReveiver";

    private HashMap<String, ArrayList<AssassinDO>> mProcess;

    private boolean mAllInsert = false;
    private boolean mAllReplace = false;

    public AssassinMethodClassVisitor(ClassVisitor classVisitor, HashMap<String, ArrayList<AssassinDO>> process){
        super(Opcodes.ASM5,classVisitor);
        init(process);
    }


    protected boolean jude(String type ){
        ArrayList<AssassinDO> inserts = mProcess.get(type);
        if(inserts == null){
            return false;
        }
        AssassinDO all = new AssassinDO();
        all.name = "all";
        all.des = "all";
        for(AssassinDO assassinDO : inserts){
            if(assassinDO.equals(all)){
                return true;
            }
        }
        return false;
    }

    private void init(HashMap<String, ArrayList<AssassinDO>> process){
        mProcess = process;
        mAllInsert = jude("insert");
        mAllReplace = jude("replace");
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {



            public void print(String msg){
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn(msg);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
                            "(Ljava/lang/String;)V", false);
            }
//            @Override
//            public void visitCode() {
//                super.visitCode();
//
//            }
//
//            @Override
//            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
//                super.visitMethodInsn(opcode, owner, name, desc, itf);
//            }

//            @Override
//            public org.objectweb.asm.AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//
//                return super.visitAnnotation(desc, visible);
//            }

            protected boolean findAssassin(String name, String method){
                ArrayList<AssassinDO> list = mProcess.get(name);
                if(list == null){
                    return false;
                }
                AssassinDO assassinDO = new AssassinDO();
                assassinDO.name = method;
                assassinDO.des = "normal";
                for(AssassinDO item : list){
                    if(item.equals(assassinDO)){
                        return true;
                    }
                }
                return false;
            }


            @Override
            protected void onMethodEnter() {
                String type = "default";
                if(!mAllInsert && !mAllReplace){
                    //非插入和非替换,需要判断是否是列表中
                    if(findAssassin("insert", name)){
                        type = "insert";
                    }else if(findAssassin("replace", name)){
                        type = "replace";
                    }else{
                        return;
                    }
                }
                print("onMethodEnter:"+name + ",desc=" + methodDesc + ",return="+Type.getReturnType(desc).toString());
//                returnValue();

//                for(Type type : types){
                    /**
                     * 05-22 17:13:42.674 21722-21722/com.meiyou.meetyoucostdemo I/System.out: ========startttt=========onClick,desc=(Landroid/view/View;)V,signature= null
                     05-22 17:13:42.694 21722-21722/com.meiyou.meetyoucostdemo I/System.out: ========type=========Landroid/view/View;
                     05-22 17:13:42.694 21722-21722/com.meiyou.meetyoucostdemo I/System.out: yes3:android.widget.Button{3a3fd7af VFED..C. ...P.... 0,0-720,96 #7f0b0056 app:id/maidian}
                     05-22 17:13:42.694 21722-21722/com.meiyou.meetyoucostdemo I/System.out: ========startttt=========show2,desc=(Landroid/view/View;ILjava/lang/String;)V,signature= null
                     05-22 17:13:42.694 21722-21722/com.meiyou.meetyoucostdemo I/System.out: ========type=========Landroid/view/View;
                     05-22 17:13:42.694 21722-21722/com.meiyou.meetyoucostdemo I/System.out: ========type=========I
                     05-22 17:13:42.694 21722-21722/com.meiyou.meetyoucostdemo I/System.out: ========type=========Ljava/lang/String;
                     */
//                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                    mv.visitLdcInsn("========type=========" + type.toString());
//                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println",
//                            "(Ljava/lang/String;)V", false);
//                }
//
//                    mv.visitLdcInsn(name);
////                    Type.getMethodDescriptor(m)
//                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
//                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/meetyoucost/TimeCache", "setStartTime",
//                            "(Ljava/lang/String;J)V", false);
//                }
//                if(name.equals("onClick")){
                    //onclick
//                    mv.visitFieldInsn(GETSTATIC,"java/lang/System", "out", "Ljava/io/PrintStream;");
                    //stack: PrintStream

//                    mv.visitMaxs(1, 3);

                    ///Date date=new Date();
                    ////local[1]=new Object[10]
//                    mv.visitIntInsn(BIPUSH, 10);
//                    //stack: 10
//                    mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
//                    mv.visitVarInsn(ASTORE, 19);


//                    mv.visitIntInsn(BIPUSH, 10);
//                    //stack: 10
//                    mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
//                    mv.visitVarInsn(ASTORE, 3);
                    //保存数据
//                    mv.visitVarInsn(ALOAD, 19);
//                    mv.visitInsn(ICONST_0);
//                    mv.visitTypeInsn(NEW, "java/lang/Object");
//                    mv.visitInsn(DUP);
//                    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
//                    mv.visitInsn(AASTORE);
//                    //拿出第二个数组
////                    mv.visitVarInsn(ALOAD, 3);
//                    mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/meetyoucost/TimeCache", "handleList",
//                            "([Ljava/lang/Object;)V", false);

//                    mv.visitTypeInsn(NEW, "com/meiyou/meetyoucost/Method");
//
//                    mv.visitInsn(DUP);
//                    mv.visitVarInsn(ASTORE, 3);
//                    	//mv.visitVarInsn(ASTORE,2); //store the reference to be used later
//
//                    mv.visitMethodInsn(INVOKESPECIAL, "com/meiyou/meetyoucost/Method", "<init>", "()V");
//
//                    //stack: use one copy. another copy is in stack
//                    //mv.visitVarInsn(ALOAD, 2); //load object from stack
//                    mv.visitLdcInsn(10);
//                    //	mv.visitVarInsn(ALOAD, 1);
//                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/meetyoucost/Method", "setParamsSize",
//                            "(I)V", false);
//                    mv.visitInsn(POP);
//                    mv.visitInsn(DUP);
//                    mv.visitVarInsn(ALOAD, 2);
//                    mv.visitMethodInsn(INVOKEVIRTUAL, "com/meiyou/meetyoucost/Method", "doLog",
//                            "()V", false);
//                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V");
//                    Type[] types = Type.getArgumentTypes(methodDesc);
//                    int start_index = types.length + 1;
//
//                //包括this=0
//                    mv.visitIntInsn(BIPUSH, types.length + 1);
//                    mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
//                    mv.visitVarInsn(ASTORE, start_index);
//                //存储this
//                    mv.visitVarInsn(ALOAD, start_index);
//                mv.visitIntInsn(BIPUSH, 0);
//                mv.visitVarInsn(ALOAD, 0);
//                mv.visitInsn(AASTORE);
//                //遍历方法变量
//                    for(int i = 0; i < types.length ; i++){
//                        print(types[i].toString());
//
//
//                        mv.visitVarInsn(ALOAD, start_index);
//                        mv.visitIntInsn(BIPUSH, i + 1);
//                        mv.visitVarInsn(ALOAD, i + 1);
//                        //判断基础类型
//                        if(types[i].toString().equals("I")){
//                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
//                        }
//                        mv.visitInsn(AASTORE);

//                        if(types[i].toString().equals("Landroid/view/View;")){
//                            //view
////                    mv.visitLdcInsn();
//                            mv.visitVarInsn(ILOAD, i + 1);
//                            mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/meetyoucost/TimeCache", "setView",
//                                    "(Landroid/view/View;)V", false);
//                            break;
//                        }
//                    }

                //载入this到栈顶
                loadThis();
                mv.visitLdcInsn(name);
                loadArgArray();
                    Type[] types = Type.getArgumentTypes(methodDesc);
                    int start_index = types.length + 1;
                mv.visitLdcInsn(Type.getReturnType(methodDesc).toString());
//                mv.visitVarInsn(ALOAD, start_index);
                    mv.visitMethodInsn(INVOKESTATIC, mReveiver, "onMethodEnter",
                            "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", false);
                mv.visitVarInsn(ASTORE, start_index);

                //如果拦截器为真
                if(mAllReplace || type.equals("replace")) {
                    print("override:" + name);
//                String methodKey = "method." + name;
//                mv.visitMethodInsn(INVOKESTATIC, "com/meiyou/meetyoucost/TimeCache", "onIntecept", "()Z", false);
//                Label l0 = new Label();
//                mv.visitJumpInsn(IFEQ, l0);
                    onMethodExit(-1);
                    String return_v = Type.getReturnType(desc).toString();
                    if (!return_v.equals("V")) {
                        //有返回值
                        mv.visitVarInsn(ALOAD, start_index);
                        returnValue();
                    } else {
                        mv.visitInsn(RETURN);
                    }

//                    mv.visitLabel(l0);
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                }


            }

            @Override
            protected void onMethodExit(int i) {
                String type = "default";
                if(!mAllInsert && !mAllReplace){
                    //非插入和非替换,需要判断是否是列表中
                    if(findAssassin("insert", name)){
                        type = "insert";
                    }else if(findAssassin("replace", name)){
                        type = "replace";
                    }else{
                        return;
                    }
                }
                print("onMethodExit:"+name + ",desc=" + methodDesc + ",return="+Type.getReturnType(desc).toString());
                loadThis();
                mv.visitLdcInsn(name);
                loadArgArray();
                mv.visitLdcInsn(Type.getReturnType(methodDesc).toString());
                mv.visitMethodInsn(INVOKESTATIC, mReveiver, "onMethodEnd",
                        "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;Ljava/lang/String;)V", false);
            }
        };
        return methodVisitor;

    }
}
