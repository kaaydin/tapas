package ch.unisg.tapastasks.tasks;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTests {

	@Test
	void testPackageDependencies() {
		noClasses()
				.that()
				.resideInAPackage("ch.unisg.tapastasks.tasks.domain..")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("ch.unisg.tapastasks.tasks.application..")
				.check(new ClassFileImporter()
						.importPackages("ch.unisg.tapastasks.tasks.."));
	}

}
