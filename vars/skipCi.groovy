def call(scmVars) {
	def commitMessage = sh(script: "git log -1 --pretty=%B ${scmVars.GIT_COMMIT}", returnStdout: true).trim()
    if (commitMessage =~ /\[skip-?ci\]/) {
        currentBuild.result = 'NOT_BUILT'
        error("Skipping Build - found '[skipci]' in commit message.")
    }
        
}