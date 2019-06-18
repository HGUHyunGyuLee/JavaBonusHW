package edu.handong.csee.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ls {

	String path;
	boolean a;
	boolean m;
	boolean S;
	boolean r;
	boolean d;
	String dArg;

	public static void main(String[] args) {

		ls myls = new ls();
		myls.run(args);
	}

	private void run(String[] args) {
		Options options = createOptions();

		if (parseOptions(options, args)) {
			path = System.getProperty("user.dir");
			File f = new File(path);
			if (a) {
				int i = 0;
				ArrayList<String> subFilesNames = new ArrayList<String>();
				for (String fileName : f.list()) 
					subFilesNames.add(fileName);
				Collections.sort(subFilesNames);
				for (String fileNames : subFilesNames) {
					i++;
					System.out.print(fileNames + "\t");
					if (i % 5 == 0)
						System.out.println();
				}
				return;
			}
			if (m) {
				int i=0;
				String temp[] = f.list();
				for (String fileName : f.list()) {
					i++;
					if(i!=temp.length)System.out.print(fileName + ", ");
					else System.out.print(fileName);
				}
			}
			if (d) {
				for (String fileName : f.list()) {
					if(fileName.equals(dArg)) {
						System.out.print(fileName);
						return;
					}
				}
				System.out.print("ls: " + dArg + ": No such file or directory");
			}
			if (r) {
				ArrayList<String> subFilesNames = new ArrayList<String>();
				
				for (String fileName : f.list()) {
					subFilesNames.add(fileName);
				}
				
				Collections.sort(subFilesNames, Collections.reverseOrder());
				int i=0;
				for (String fileNames : subFilesNames) {
					if(!fileNames.startsWith("."))System.out.print(fileNames + "\t");
					i++;
					if (i % 5 == 0)
						System.out.println();
				}
			}

			if (S) {
				int i=0;
				ArrayList<Integer> subFilesSize = new ArrayList<Integer>();
				HashMap<Integer, String> subFileHash = new HashMap<Integer, String>();
				for (String fileName : f.list()) {
					String tempFilePath = path + "/" + fileName;
					File tempFile = new File(tempFilePath);
					if(!subFilesSize.contains((int) tempFile.length()))subFilesSize.add((int) tempFile.length());
					if(subFileHash.containsKey((int) tempFile.length())) 
					{
						if(subFileHash.get((int) tempFile.length()).compareTo(fileName)==1) {
							subFileHash.put((int) tempFile.length()-1, fileName);
							subFilesSize.add((int) tempFile.length()-1);
						}
						else if((subFileHash.get((int) tempFile.length()).compareTo(fileName)==-1)) {
							subFileHash.put((int) tempFile.length()+1, fileName);
							subFilesSize.add((int) tempFile.length()+1);
						}
					}
					else subFileHash.put((int) tempFile.length(), fileName);
					
				}
				Collections.sort(subFilesSize, Collections.reverseOrder());
				for (Integer fileSize : subFilesSize) {
				//	i++;
					if(!subFileHash.get(fileSize).startsWith(".")) {
						System.out.print(subFileHash.get(fileSize) + "\t");
						i++;
					}
					if (i % 5 == 0)
						System.out.println(); 
				}

			}
		}

	}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			// path = cmd.getOptionValue("p");
			a = cmd.hasOption("a");
			m = cmd.hasOption("m");
			S = cmd.hasOption("S");
			r = cmd.hasOption("r");
			d = cmd.hasOption("d");
			dArg = cmd.getOptionValue("d");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	// Definition Stage
	private Options createOptions() {
		Options options = new Options();

		// add options by using OptionBuilder
		options.addOption(Option.builder("a").desc("Display all the files")
				// .hasArg()
				.argName("File names to display")
				// .required()
				.build());
		options.addOption(Option.builder("r").desc("Display files in a reverse oreder")
				// .hasArg()
				.argName("File names in a reverse oreder to display")
				// .required()
				.build());

		// add options by using OptionBuilder
		options.addOption(Option.builder("m").desc("Display files with a separator comma")
				// .hasArg() // this option is intended not to have an option value but just an
				// option
				.argName("Displaying files linearly")
				// .required() // this is an optional option. So disabled required().
				.build());
		options.addOption(Option.builder("S").desc("Display files in the order of the size")
				// .hasArg() // this option is intended not to have an option value but just an
				// option
				.argName("Display files from the biggest to the smallest")
				// .required() // this is an optional option. So disabled required().
				.build());
		options.addOption(Option.builder("d").desc("Display the file if exists").hasArg() // this option is intended not
																							// to have an option value
																							// but just an
				// option
				.argName("Diplay a specific file")
				// .required() // this is an optional option. So disabled required().
				.build());

		// add options by using OptionBuilder
		options.addOption(Option.builder("h").longOpt("help").desc("Help").build());

		return options;
	}

	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "ls implementation in java";
		String footer = "";
		formatter.printHelp("ls commander", header, options, footer, true);
	}

}
