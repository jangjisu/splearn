package com.clean.splearn;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "com.clean.splearn", importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {
    /**
     * Application 클래스를 의존하는 클래스는 application, adapter 에만 존재해야 한다.
     */
    @ArchTest
    void hexagonalArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain").definedBy("com.clean.splearn.domain..")
                .layer("application").definedBy("com.clean.splearn.application..")
                .layer("adapter").definedBy("com.clean.splearn.adapter..")
                .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
                .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
                .check(classes);


    }
}
