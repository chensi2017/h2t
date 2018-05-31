package com.htdata.iiot.hdfs2tsdb.cmd;

import com.htdata.iiot.hdfs2tsdb.Config.Configs;
import org.apache.commons.cli.*;

public class Cmd {
	public static void parserCmd(String[] args) {
		Options options = new Options();
		options.addOption("h", "help", false, "Print this usage information");
		options.addOption("t", "threads", true, "The number of threads,advice set as the numbers of the partition");
		options.addOption("p","path",true,"The directory path in HDFS");
		options.addOption("z", "zookeeper", true, "The connection info of zk,like:master.htdata.com:2181");
		options.addOption("d", "debug", false, "debug");
		CommandLine cmd=null;
		CommandLineParser parser = new BasicParser();
		try {
			cmd = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e1) {
			// TODO Auto-generated catch block
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("TSDStore", options);
			e1.printStackTrace();
			System.exit(0);
		}
		if(cmd.hasOption("h")){
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("TSDStore", options);
			System.exit(0);
		}
		if(cmd.hasOption("p")){
			Configs.HdfsConfig.HDFS_PATH = cmd.getOptionValue("p");
		}
		if(cmd.hasOption("z")){
			System.setProperty("htiiot.zookeeper", cmd.getOptionValue("z"));
		}
		
		if(cmd.hasOption("t")){
			Configs.BaseConfig.threads = Integer.parseInt(cmd.getOptionValue("t"));
		}

		if(cmd.hasOption("d")){
			Configs.BaseConfig.url="http://127.0.0.1:4242/api/put?details";
			Configs.BaseConfig.log=true;
		}
		
	}

}
