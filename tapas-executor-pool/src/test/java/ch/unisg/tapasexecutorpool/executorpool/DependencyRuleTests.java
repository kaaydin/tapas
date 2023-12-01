package ch.unisg.tapasexecutorpool.executorpool;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTests {

	@Test
	void testPackageDependencies() {
		noClasses()
				.that()
				.resideInAPackage("ch.unisg.tapasexecutorpool.executorpool.domain..")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("ch.unisg.tapasexecutorpool.executorpool.application..")
				.check(new ClassFileImporter()
						.importPackages("ch.unisg.tapasexecutorpool.executorpool.."));
	}

}
