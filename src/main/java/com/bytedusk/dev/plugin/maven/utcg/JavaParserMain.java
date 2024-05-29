package com.bytedusk.dev.plugin.maven.utcg;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.util.ArrayList;
import java.util.List;

public class JavaParserMain {
    public static void main(String args[]){
        //String srcPath = "D:\\workSpace\\idea\\utcg-maven-plugin\\src\\main\\java\\com\\bytedusk\\dev\\plugin\\maven\\utcg\\demo";
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaParserMain.class).resolve("src/main/java"));

        TypeSolver typeSolver = new ReflectionTypeSolver(false);
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(symbolSolver);

        sourceRoot.setParserConfiguration(parserConfiguration);
        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("com.bytedusk.dev.plugin.maven.utcg", "TestMojo.java");
        cu.getImports().forEach(item -> System.out.println(item.getName()));
        cu.getTypes().forEach(typeDeclaration -> System.out.println(typeDeclaration.getName()));

        //field
        List<FieldDeclaration> fdl = new ArrayList<FieldDeclaration>();
        cu.accept(new VoidVisitorAdapter<Object>() {
            public void visit(final FieldDeclaration f, final Object arg) {
                fdl.add(f);
                System.out.println(f.getVariables().getFirst());
                System.out.println(f.getAnnotations().getFirst());
            }
        } , null );

        //method
        List<MethodDeclaration> mdl = new ArrayList<MethodDeclaration>();
        cu.accept(new VoidVisitorAdapter<Object>() {
            public void visit(final MethodDeclaration m, final Object arg) {
                mdl.add(m);
                System.out.println(m.getName()+":"+m.getType()+":"+m.getParameters());
                m.findAll(MethodCallExpr.class).forEach(mce ->{
                    System.out.println(mce.getScope()+":"+mce.getName()+":"+mce.resolve().getQualifiedSignature());
                });
            }
        } , null );
    }
}
