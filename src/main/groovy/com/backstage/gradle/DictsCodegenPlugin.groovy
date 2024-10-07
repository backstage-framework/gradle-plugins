package com.backstage.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property

class DictsCodegenPlugin implements Plugin<Project>
{
	@Override
	void apply(Project project)
	{
		var bootRunProvider = project.tasks.named('bootRun')

		if (!bootRunProvider.present)
		{
			println("Dicts codegen plugin requires Spring Boot (bootRun task).")

			return
		}

		var bootRun = bootRunProvider.get()

		def extension = project.extensions.create('dictsCodegen', DictsCodegenPluginExtension)

		project.tasks.register("dictsCodegen", bootRun.class as Class<Task>, {
			group = 'backstage'
			classpath = bootRun.classpath
			mainClass = bootRun.mainClass

			environment("app.dicts.codegen.outputPath", extension.outputPath.get())
			environment("app.dicts.codegen.targetPackage", extension.targetPackage.get())
		})
	}
}

class DictsCodegenPluginExtension
{
	Property<String> outputPath

	Property<String> targetPackage

	DictsCodegenPluginExtension(Project project)
	{
		outputPath = project.objects.property(String.class).convention(project.sourceSets.main.java.srcDirs.first().toString())
		targetPackage = project.objects.property(String.class).convention("com.backstage.dicts.generated")
	}
}
