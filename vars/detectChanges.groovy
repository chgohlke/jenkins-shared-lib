def call(availableModules) {
	def outfile = "ci_module_storage.txt"
    sh "find . -name $outfile -exec rm '{}' +"
    sh "touch $outfile"
    if (params.APP == "Automatic") {
    	echo "Detecting changes is in automatic mode!"
        currentBuild.description = "Automatic Mode"
        List<String> allChanges = []
        currentBuild.changeSets.each { cs ->
            cs.getItems().each { item ->
                item.getAffectedFiles().each { f ->
                    String change = f.path.split('/')[0]
                    echo "Change: $change"
                    allChanges << change
                }
            }
        }

        def whitelistedChanges = allChanges
            .unique()
            .findAll { availableModules.contains(it) }

        if (whitelistedChanges.isEmpty()) {
            echo "Will build all modules because no module was changed."
            availableModules.each { module ->
                sh "echo $module >> $outfile"
            }
        } else {
            whitelistedChanges
                .each {
                sh "echo $it >> $outfile"
                echo "Found changes for module: $it"
            }
        }
    } else {
		echo "Detecting changes is disabled because an APP was provided as parameter."
        String input = params.APP
        currentBuild.description = "$input"
        if (input.contains(",")) {
            sh "touch $outfile"
            input.split(',').each {
                sh "echo ${it.trim()} >> $outfile"
            }
        } else {
            sh "echo $input > $outfile"
        }
    }

}