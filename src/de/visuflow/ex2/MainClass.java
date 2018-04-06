package de.visuflow.ex2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.visuflow.ex2.updated.MainClassUpdated;
import de.visuflow.reporting.EmptyReporter;
import de.visuflow.reporting.IReporter;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Main;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.jimple.changeset.AnalysisChangeSetFinder;
import soot.jimple.changeset.BranchModel;

public class MainClass {

	public static void main(String[] args) {
		Map<String, Map<String, Map<Unit, Set<FlowAbstraction>>>> cache = new HashMap<String, Map<String, Map<Unit, Set<FlowAbstraction>>>>();

		runAnalysis(new EmptyReporter(), 3, cache);

		AnalysisChangeSetFinder finder = new AnalysisChangeSetFinder(args[0], args[1], args[2]);
//		System.out.println("Working Directory = " +
//	              System.getProperty("user.dir"));
//		 AnalysisChangeSetFinder finder = new AnalysisChangeSetFinder(
//		 "initial/bin",
//		 "updated/bin",
//		 "de.visuflow.ex2.IntraproceduralAnalysis");
		// AnalysisChangeSetFinder finder = new
		// AnalysisChangeSetFinder("C:\\Users\\karth\\git\\UpdateAbleAnalysis\\bin",
		// "C:\\Users\\karth\\git\\UpdateAbleAnalysis\\updated-bin",
		// "de.visuflow.ex2.IntraproceduralAnalysis");
		Map<String, List<BranchModel>> changeSet = finder.computeChangeSet();

		MainClassUpdated.main(cache, changeSet);
	}

	public static void runAnalysis(final IReporter reporter, final int exercisenumber,
			Map<String, Map<String, Map<Unit, Set<FlowAbstraction>>>> cache) {
		G.reset();

		// Register the transform
		Transform transform = new Transform("jtp.analysis", new BodyTransformer() {
			@Override
			protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
				IntraproceduralAnalysis ipa = new IntraproceduralAnalysis(reporter, b, cache);
				ipa.doAnalyis(cache);

			}
		});
		PackManager.v().getPack("jtp").add(transform);
		//String targetDir = "C:\\Users\\karth\\git\\Simple-Java-Text-Editor\\bin";
		String targetDir = "C:\\Users\\karth\\git\\visuflow-uitests-target\\bin";
		Main.main(new String[] { "-pp", "-process-dir", targetDir, "-w", "-exclude", "javax", "-allow-phantom-refs",
				"-no-bodies-for-excluded", "-src-prec", "class", "-output-format", "J" });
	}

}
