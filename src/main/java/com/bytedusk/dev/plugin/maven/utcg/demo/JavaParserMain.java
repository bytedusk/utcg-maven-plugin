package com.bytedusk.dev.plugin.maven.utcg.demo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import javax.tools.JavaCompiler;

public class JavaParserMain {
    public static void main(String args[]){
        //String srcPath = "D:\\workSpace\\idea\\utcg-maven-plugin\\src\\main\\java\\com\\bytedusk\\dev\\plugin\\maven\\utcg\\demo";
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaParserMain.class).resolve("src/main/java"));

        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("com.bytedusk.dev.plugin.maven.utcg.demo", "RessCryptoUtils.java");
        cu.getImports().forEach(item -> System.out.println(item.getName()));

        cu.getTypes().forEach(typeDeclaration -> System.out.println(typeDeclaration.getName()));

        cu.accept(new VoidVisitorAdapter() {
            public void visit(final FieldDeclaration m, final Object arg) {
                m.getVariables().forEach(var ->System.out.println(var.getName()));
            }
        } , null );

        cu.accept(new VoidVisitorAdapter() {
            public void visit(final MethodDeclaration m, final Object arg) {
                System.out.println(m.getName()+":"+m.getType());
            }
        } , null );
    }
}
