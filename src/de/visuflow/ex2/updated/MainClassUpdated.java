package de.visuflow.ex2.updated;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.visuflow.ex2.FlowAbstraction;
import de.visuflow.reporting.EmptyReporter;
import de.visuflow.reporting.IReporter;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Main;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.jimple.changeset.BranchModel;

public class MainClassUpdated {

	public static void main(Map<String, Map<String, Map<Unit, Set<FlowAbstraction>>>> cache,
			Map<String, List<BranchModel>> changeSet) {
		runAnalysis(new EmptyReporter(), 3, cache, changeSet);
	}

	public static void runAnalysis(final IReporter reporter, final int exercisenumber,
			Map<String, Map<String, Map<Unit, Set<FlowAbstraction>>>> cache, Map<String, List<BranchModel>> changeSet) {
		G.reset();

		// Register the transform
		Transform transform = new Transform("jtp.analysis", new BodyTransformer() {
			@Override
			protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
				IntraproceduralAnalysis ipa = new IntraproceduralAnalysis(reporter, b);
				ipa.doAnalysis(cache, changeSet);
//				Reader reader;
//				
//				String path = "C:\\Users\\karth\\git\\updated\\result\\" + b.getMethod().getName().replaceAll("[<>]","")+".csv";
//				BufferedWriter writer = null;
//				CSVPrinter csvPrinter = null;
//				
//				try {
//					reader = Files.newBufferedReader(Paths.get(path));
//					CSVParser csvParser = new CSVParser(reader,
//							CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
//					Iterable<CSVRecord> csvRecords = csvParser.getRecords();
//					try {
//						 writer = Files.newBufferedWriter(Paths.get(path));
//						 csvPrinter = new CSVPrinter(writer,
//									CSVFormat.DEFAULT.withHeader("Unit", "OutSet","UpdatedOutSet"));
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					for (Unit u : b.getUnits()) {
//						for (CSVRecord csvRecord : csvRecords) {
//							if (csvRecord.get("Unit").equals(u.toString())) {
//								System.out.println("Hello");
//								String[] str= {csvRecord.get("Unit"),csvRecord.get("OutSet"),ipa.getFlowAfter(u).toString()};
//								csvPrinter.printRecord(str);
//								csvPrinter.flush();
//							}
//
//						}
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		PackManager.v().getPack("jtp").add(transform);
		//String targetDir = "C:\\Users\\karth\\git\\Simple-Java-Text-Editor\\bin";
		String targetDir = "C:\\Users\\karth\\git\\visuflow-uitests-target\\bin";
		Main.main(new String[] { "-pp", "-process-dir", targetDir, "-w", "-exclude", "javax", "-allow-phantom-refs",
				"-no-bodies-for-excluded", "-src-prec", "class", "-output-format", "J" });
	}

}
