/**
 * Copyright © 2020-2021 Alessio Arleo 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vaim.io.boot;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import vaim.io.local.disk.GraphReader;
import vaim.io.local.disk.InputReader.DirectedGMLInputReader;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { Constants.BOOT_PACKAGE, Constants.BASE_PACKAGE , Constants.CONFIGURATION_PACKAGE})
public class VAIMBoot implements CommandLineRunner {

	private static final String REDRAW_OPTION = "--redraw";
	private static final String PATH_OPTION = "-f";

	@Override
	public void run(String... arg0) throws Exception {
		if (arg0.length > 0 && arg0[0].equals("exitcode")) {
			throw new ExitException();
		}
	}

	public static void main(String[] args) throws Exception {
				
		if(args.length > 1) {									
			boolean redraw = false;
			String path = null;
			int index = 0;
			try {    		
				while(index < args.length) {
					if(args[index].equals(REDRAW_OPTION)) {
						System.out.println("Redraw enabled");
						redraw = true;
					}else if(args[index].equals(PATH_OPTION)) {
						path = args[++index];    				
					}else {
						System.out.println("Unrecognised option " + args[index]);
					}
					index++;
				}
				File dir = new File(path);
				if(dir.isDirectory())
					loadGraphs(dir, redraw);
				else
					throw new NullPointerException();
			}catch(NullPointerException npe) {
				System.out.println("Invalid path or path is not a folder. Please check and try again");
				showHelp();
				System.exit(1);
			}catch(ArrayIndexOutOfBoundsException ioo) {
				System.out.println("Please provide a path after -f flag");
				showHelp();
				System.exit(1);
			}
			System.exit(0);
		}else
			new SpringApplication(VAIMBoot.class).run(args);    	
	}

	class ExitException extends RuntimeException implements ExitCodeGenerator {
		private static final long serialVersionUID = 1L;

		@Override
		public int getExitCode() {
			return 10;
		}

	}

	private static void loadGraphs(File dir, boolean redraw) throws Exception{
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if(!child.isDirectory()) {
					System.out.println("Uploading " + FilenameUtils.removeExtension(child.getName()));
					GraphReader.uploadGraphToDatabase(child, DirectedGMLInputReader.class, redraw);
					System.out.println("Done.");
				}
			}
		} else
			System.out.println("No files in folder " + dir);
	}

	private static void showHelp() {

	}
}
