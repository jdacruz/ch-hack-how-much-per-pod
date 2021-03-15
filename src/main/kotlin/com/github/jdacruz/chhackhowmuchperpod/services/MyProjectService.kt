package com.github.jdacruz.chhackhowmuchperpod.services

import com.github.jdacruz.chhackhowmuchperpod.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
