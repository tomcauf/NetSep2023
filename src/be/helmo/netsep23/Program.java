package be.helmo.netsep23;

import be.helmo.netsep23.parser.IMessageBuilder;
import be.helmo.netsep23.parser.IParser;
import be.helmo.netsep23.parser.MessageBuilder;
import be.helmo.netsep23.parser.Parser;
import be.helmo.netsep23.server.Server;
import be.helmo.netsep23.server.TLSSocketFactory;
import be.helmo.netsep23.tools.DataManager;
import be.helmo.netsep23.tools.IDataManager;

public class Program {
	private Server server;
	private IDataManager dataManager;
	private IParser parser;
	private IMessageBuilder messageBuilder;

	public Program(String jsonFile, String mcastAddress, int mcastPort, int unicastPort) {
		dataManager = new DataManager(jsonFile);
		parser = new Parser();
		messageBuilder = new MessageBuilder();
		TLSSocketFactory tlsSocketFactory = new TLSSocketFactory("./cert", "servercert.p12", "cert2023");
		server = new Server(dataManager, unicastPort, mcastAddress, mcastPort, parser, messageBuilder, tlsSocketFactory);
		server.run();
	}

	public static void main(String[] args) {
		System.out.println("[*] Start !");
		if (args.length == 4) {
			new Program(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		} else {
			new Program("./data/data-1.json", "226.225.224.224", 60321, 8372);
		}
	}

}
