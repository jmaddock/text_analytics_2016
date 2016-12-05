compare_tokens:
	javac -classpath "/Users/klogg/dev/java_packages/lucene-6.3.0/core/*:/Users/klogg/dev/java_packages/lucene-6.3.0/analysis/common/lucene-analyzers-common-6.3.0.jar:/Users/klogg/dev/java_packages/core_nlp/*:/Users/klogg/dev/java_packages/commons-math3-3.6.1/*" -d classes src/hw2/TokenizerComparison.java src/hw2/readWrite.java
	java -cp "/Users/klogg/dev/java_packages/lucene-6.3.0/core/*:/Users/klogg/dev/java_packages/lucene-6.3.0/analysis/common/lucene-analyzers-common-6.3.0.jar:/Users/klogg/dev/java_packages/core_nlp/*:/Users/klogg/dev/java_packages/commons-math3-3.6.1/*:./classes" hw2.TokenizerComparison

compare_normalizers:
	javac -classpath "/Users/klogg/dev/java_packages/lucene-6.3.0/core/*:/Users/klogg/dev/java_packages/lucene-6.3.0/analysis/common/lucene-analyzers-common-6.3.0.jar:/Users/klogg/dev/java_packages/core_nlp/*:/Users/klogg/dev/java_packages/commons-math3-3.6.1/*" -d classes src/hw2/TokenizerComparison.java src/hw2/readWrite.java src/hw2/NormalizerComparison.java
	java -cp "/Users/klogg/dev/java_packages/lucene-6.3.0/core/*:/Users/klogg/dev/java_packages/lucene-6.3.0/analysis/common/lucene-analyzers-common-6.3.0.jar:/Users/klogg/dev/java_packages/core_nlp/*:/Users/klogg/dev/java_packages/commons-math3-3.6.1/*:./classes" hw2.NormalizerComparison

frequency:
	javac -classpath "/Users/klogg/dev/java_packages/lucene-6.3.0/core/*:/Users/klogg/dev/java_packages/lucene-6.3.0/analysis/common/lucene-analyzers-common-6.3.0.jar:/Users/klogg/dev/java_packages/core_nlp/*:/Users/klogg/dev/java_packages/commons-math3-3.6.1/*" -d classes src/hw2/TokenizerComparison.java src/hw2/readWrite.java src/hw2/NormalizerComparison.java src/hw2/FrequencyAnalyzer.java
	java -cp "/Users/klogg/dev/java_packages/lucene-6.3.0/core/*:/Users/klogg/dev/java_packages/lucene-6.3.0/analysis/common/lucene-analyzers-common-6.3.0.jar:/Users/klogg/dev/java_packages/core_nlp/*:/Users/klogg/dev/java_packages/commons-math3-3.6.1/*:./classes" hw2.FrequencyAnalyzer
