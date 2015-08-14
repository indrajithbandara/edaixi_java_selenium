using System;
using System.Collections;
using System.Net;
using System.Net.Security;
using System.Net.Sockets;
using System.Security.Authentication;
using System.Text;
using System.Security.Cryptography.X509Certificates;
using System.IO;
using System.Diagnostics;
public class InstallCert {

  static byte[] data = null;

  static bool ValidateServerCertificate(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors sslPolicyErrors) {
  	foreach (X509ChainElement element in chain.ChainElements) {
			Console.WriteLine ("Element issuer name: {0}", element.Certificate.Issuer);
	}
		
    if (chain.ChainElements != null && chain.ChainElements.Count != 0) {
      X509ChainElement chainElement = chain.ChainElements[chain.ChainElements.Count - 1];
      data = chainElement.Certificate.Export(X509ContentType.Cert);
    }

    return true;
  }

  static void Main(string[] args) {
    if (args.Length != 1) 
      Environment.Exit(1);
    Uri uri = new Uri(args[0]);

    string server = uri.Host;
    int port = uri.Port;
    if (port < 0)
      port = 443;
    try {
      TcpClient client = new TcpClient(server, port);
      SslStream sslStream = new SslStream(client.GetStream(), false, new RemoteCertificateValidationCallback (ValidateServerCertificate), null);
      sslStream.AuthenticateAsClient(server);
    } catch (Exception e) {
      Console.WriteLine("Failed to get certificate. {0}", e);
    }

	try {
	    if (data != null) {
	      string filePath = System.IO.Path.GetTempPath() + "server.cer";
	      Console.WriteLine("Get certificate to {0}", filePath);
	      File.WriteAllBytes(filePath, data);
	      Process process = new Process();
	      process.StartInfo.UseShellExecute = false;
	      process.StartInfo.RedirectStandardOutput = true;
	      process.StartInfo.FileName = "CertUtil";
	      process.StartInfo.Arguments = "-addstore root \"" + filePath + "\"";
	      process.Start();
	      string output = process.StandardOutput.ReadToEnd();
	      process.WaitForExit();
	      Console.WriteLine(output);
	    }
    } catch (Exception e) {
        Console.WriteLine("{0}", e);
    }
  }

}
