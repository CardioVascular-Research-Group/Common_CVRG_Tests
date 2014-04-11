package edu.jhu.cvrg.seleniummain;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.jhu.cvrg.waveformtests.WaveformTestProperties;
import edu.jhu.cvrg.waveformtests.analyze.AnalyzeTester;
import edu.jhu.cvrg.waveformtests.upload.UploadTester;
import edu.jhu.cvrg.waveformtests.visualize.VisualizeTester;

public class TestController {
	
	private WaveformTestProperties waveformProps;
	private Calendar todaysDate;
	private DateFormat dateFormat;
	private LogfileManager logger;
	private String hostname;
	private String logfilePath;
	private String username;
	private String password;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length != 5 || args[0].equals("--help")) {
			System.out.println("Usage:  CVRG_Tests.jar <LOGON|WAVEFORM|CEP> <hostname> <username> <password> <logfile_location>\n");
			System.exit(0);
		}

		String testType = args[0];
		String hostname = args[1];
		String username = args[2];
		String password = args[3];
		String logfilePath = args[4];

		String propertiesLocation = "";
		
		System.out.println(propertiesLocation);
		
		TestController mainControl = new TestController(hostname, logfilePath, username, password);
		
		switch(testType) {
			case "LOGON":
				mainControl.testAuthentication(propertiesLocation);
				break;
			case "WAVEFORM":
				break;
			case "CEP":
				break;
			default:
				// Do all?
				break;
		}
		
	}
	
	public TestController(String newHostname, String newLogfilePath, String newUsername, String newPassword) {
		hostname = newHostname;
		logfilePath = newLogfilePath;
		username = newUsername;
		password = newPassword;
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	}
	
	public void testAuthentication(String propertiesFileLocation) {
		setup();
	}
	
	public void testWaveform(String propertiesFileLocation) {
		
		WaveformTestProperties testProps = WaveformTestProperties.getInstance();
		
		setup();
		
		try {
			testProps.loadConfiguration(propertiesFileLocation);
			
			logger.addToLog("Waveform 3 Selenium Test Begin:  " + dateFormat.format(todaysDate.getTime()));
		
			String uploadPath = testProps.getUploadPath();
			String visualizePath = testProps.getVisualizePath();
			String welcomePath = testProps.getWelcomePath();
			String analyzePath = testProps.getAnalyzePath();
			
			UploadTester upload = new UploadTester(hostname, uploadPath, welcomePath, username, password);
			
			
			upload.login();
			upload.validateFolderTree();
			upload.uploadFile();
			upload.logout();
			
			AnalyzeTester analysis = new AnalyzeTester(hostname, analyzePath, welcomePath, username, password);
			
			analysis.login();
	
			analysis.analyzeOneECG();
	
			
			
			VisualizeTester visualize = new VisualizeTester(hostname, visualizePath, welcomePath, username, password);
			
			visualize.login();
			visualize.goToPage();
			visualize.testVisualizeViews();
			
	
			logger.addToLog("Waveform 3 Selenium Tests Completed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	private void setup() {
		LogfileManager logger = LogfileManager.getInstance();
		logger.setLogfileLocation(logfilePath);
		todaysDate = Calendar.getInstance();
	}

}