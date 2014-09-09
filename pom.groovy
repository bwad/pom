#!/usr/bin/env groovy

def pom = new XmlSlurper().parseText(new File('pom.xml').text)

//=============================================================================
// Project class
//=============================================================================

class Project {
  
  def pom
  
  Project(pom) { this.pom = pom }
  
  String attributes() { 
  """\nProject:
  modelVersion: ${pom.modelVersion}
  groupId: ${pom.groupId}
  artifactid: ${pom.artifactId}
  version: ${pom.version}
  packaging: ${pom.packaging}"""
  }
  
  def dependencies() {  
    def deps = [] 
    pom.dependencies.dependency.each {
       deps << new Dependency(it)
    }
    deps
  }
  
  String plugins() {
    String rval = "\nPlugins: (${pom.build.plugins.plugin.size()})"
    pom.build.plugins.plugin.each { 
      rval += "\n  ${it.groupId}:${it.artifactId}" 
    }
    rval
  }

}

//=============================================================================
// Dependency class
//=============================================================================

class Dependency {
  def dep
  Dependency(dep) {this.dep = dep }
  String toString() { "  ${dep.groupId}:${dep.artifactId}"}
}

//=============================================================================
// main
//=============================================================================

project = new Project(pom)
println project.attributes()

print "\n Dependencies: (${project.dependencies().size()})\n"
project.dependencies().each { println it}

println project.plugins()
