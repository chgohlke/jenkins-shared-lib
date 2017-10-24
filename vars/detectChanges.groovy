def call(modules) {
	def outfile = "ci_module_storage.txt"
    sh "find . -name $outfile -exec rm '{}' +"
    sh "touch $outfile"
    if (params.APP == "Automatic") {
    	echo "Detecting changes is in automatic mode!"
    	currentBuild.description = "Automatic Mode"
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