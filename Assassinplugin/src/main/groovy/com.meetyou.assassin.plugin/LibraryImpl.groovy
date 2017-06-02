package com.meetyou.assassin.plugin

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project;

/**
 * Created by Linhh on 17/6/2.
 */

public class LibraryImpl  extends PluginImpl{

    @Override
    Object invokeMethod(String s, Object o) {
        return super.invokeMethod(name, args)
    }

    @Override
    void apply(Project project) {
        super.apply(project)
        def android = project.extensions.getByType(LibraryExtension);
        android.registerTransform(this)
    }

    @Override
    Object getProperty(String s) {
        return super.getProperty(s)
    }

    @Override
    void setProperty(String s, Object o) {

    }

    @Override
    MetaClass getMetaClass() {
        return super.getMetaClass();
    }

    @Override
    void setMetaClass(MetaClass metaClass) {

    }

    @Override
    public String getName() {
        return "LibraryImpl";
    }
}