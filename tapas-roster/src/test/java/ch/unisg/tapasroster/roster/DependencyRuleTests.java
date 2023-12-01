package ch.unisg.tapasroster.roster;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DependencyRuleTests {

	@Test
	void testPackageDependencies() {
		noClasses()
				.that()
				.resideInAPackage("ch.unisg.tapasroster.roster.domain..")
				.should()
				.dependOnClassesThat()
				.resideInAnyPackage("ch.unisg.tapasroster.roster.application..")
				.check(new ClassFileImporter()
						.importPackages("ch.unisg.tapasroster.roster.."));
	}

}
