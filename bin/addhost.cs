using System;
using System.Collections;
using System.Text;
using System.IO;
using System.Collections.Specialized;
public class AddHost {
	static void Main(string[] args) {
		if (args.Length != 2) 
			Environment.Exit(1);
		string ip = args[0];
	   	string hostname = args[1];
		try {
			String hosts = Environment.ExpandEnvironmentVariables(@"%SystemRoot%\system32\drivers\etc\hosts");
			string[] lines = File.ReadAllLines(hosts);
			StringCollection linesToSave = new StringCollection();
			foreach (string l in lines) {
				if (!l.Contains(hostname) && !l.Contains(ip)) {
					linesToSave.Add(l);
				}
			}
			if (!".".Equals(ip))
				linesToSave.Add(ip + "\t" + hostname);
			lines = new String[linesToSave.Count];
			linesToSave.CopyTo(lines, 0);
			File.WriteAllLines(hosts, lines);
		} catch (Exception e) {
            		Console.WriteLine("{0}", e);
        	}
	}
}