package com.mgrzeszczak.transactionaloutboxpattern

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.TestInstance

@AnalyzeClasses(packagesOf = [ArchitectureTest::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArchitectureTest {

    @ArchTest
    val `no dependency on infrastructure in domain` = ArchRuleDefinition.noClasses()
            .that()
            .resideInAPackage("com.mgrzeszczak.transactionaloutboxpattern.domain")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("com.mgrzeszczak.transactionaloutboxpattern.infrastructure")

}