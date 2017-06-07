package com.meetyou.assassin.plugin

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * Created by Linhh on 17/6/6.
 */

public class AssassinPlugin implements Plugin<Project> {
    boolean isLibrary = false;

    @Override
    public void apply(Project project) {
        println "=======AssassinPlugin is start"
        if(project.plugins.hasPlugin("com.android.application")){
            isLibrary = false;
            println '===assassin apply application=====' + project.getName()
            processApplication(project)
        }else if(project.plugins.hasPlugin("com.android.library")){
            isLibrary = true;
            println '===assassin apply lib=====' + project.getName()
            processLibrary(project)
        }else{
            println '===assassin apply other type=====' + project.getName()
        }
    }

    void processLibrary(Project project){
        project.android.libraryVariants.all {
            it ->
                println it.name.capitalize() + " is start to run"
                println "compile${it.name.capitalize()}JavaWithJavac" + " is afterEvaluate"
                def compileTask = project.tasks.findByName("compile${it.name.capitalize()}JavaWithJavac")
//                uploadTask.configure {
//
//                    //println 'deploy complete'
//                }
                processTask(compileTask)

        }
    }

    void processApplication(Project project){
        project.android.applicationVariants.all {
            it ->
                println it.name.capitalize() + " is start to run"
                println "compile${it.name.capitalize()}JavaWithJavac" + " is afterEvaluate"
                def compileTask = project.tasks.findByName("compile${it.name.capitalize()}JavaWithJavac")
//                uploadTask.configure {
//
//                    //println 'deploy complete'
//                }
                processTask(compileTask)

        }
    }

    void processTask(Task compileTask){
        if (compileTask != null) {

            List<Action<? super Task>> list = new ArrayList<>()
            list.add(new Action<Task>() {
                @Override
                void execute(Task task) {
                    println "=======compileTask is running"
                    if (task.outputs.files.files) {
                        task.project.logger.warn "taskName:${task.name} "
                        task.project.logger.warn "inputs.files.files:-----------start------- "
                        task.outputs.files.files.each {
                            task.project.logger.warn "${it.absolutePath} "
                        }
                        task.project.logger.warn "inputs.files.files: -----------end---------- "
                    }
//                            println "collect${variant.name.capitalize()}MultiDexComponents action execute!---------XXXXXXX mini main dex生效了!!!!$projectDir"
//                            def dir = new File("$projectDir/build/intermediates/multi-dex/${variant.dirName}");
//                            if (!dir.exists()) {
//                                println "$dir 不存在,进行创建"
//                                dir.mkdirs()
//                            }
//                            def manifestkeep = new File(dir.getAbsolutePath() + "/manifest_keep.txt")
//                            manifestkeep.delete()
//                            manifestkeep.createNewFile()
//                            println "先删除,后创建manifest_keep"
//                            def backManifestListFile = new File("$projectDir/manifest_keep.txt")
//                            backManifestListFile.eachLine {
//                                line ->
//                                    manifestkeep << line << '\n'
//                            }
                }
            })
            compileTask.doLast(list)
        }
    }
}
