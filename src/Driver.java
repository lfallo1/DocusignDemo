
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import com.docusign.esignature.DocuSignClient;
import com.docusign.esignature.json.Document;
import com.docusign.esignature.json.Recipients;
import com.docusign.esignature.json.RequestSignatureFromDocuments;
import com.docusign.esignature.json.SignHereTab;
import com.docusign.esignature.json.Signer;
import com.docusign.esignature.json.Tabs;

public class Driver {
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println("Starting sample...");
		
		//=======================================================================================================================
		// STEP 1: Login API 
		//=======================================================================================================================
		
		DocuSignClient dsClient = new DocuSignClient("fallon.lance@gmail.com", "Snoopy08", "MYGE-3bffab4d-daed-4ccf-9eea-5f26085c1b8f");
		dsClient.login();
		
		//=======================================================================================================================
		// STEP 2: Create and Send Envelope API (with embedded recipient)
		//=======================================================================================================================
		
		RequestSignatureFromDocuments request = new RequestSignatureFromDocuments();

		Signer signer = new Signer();
		signer.setEmail("fallon.lance@gmail.com");
		signer.setName("Lance");
		signer.setRecipientId("1");
		
		// Note: clientUserId property must be set to indicate recipient is embedded
		signer.setClientUserId("101");
		
		SignHereTab tab1 = new SignHereTab();
		tab1.setDocumentId("1"); 
		tab1.setPageNumber("1");
		tab1.setXPosition("100");
		tab1.setYPosition("150");		
		List<SignHereTab> signatureTabs = Arrays.asList(tab1);
		
		Tabs tabs = new Tabs();
		tabs.setSignHereTabs(signatureTabs);
		signer.setTabs(tabs);
		
		Document document = new Document();
		document.setName("TEST.PDF");
		document.setDocumentId("1");
		List<Document> documents = Arrays.asList(document);
		
		List<Signer> signers = Arrays.asList(signer);
		Recipients recipients = new Recipients();
		recipients.setSigners(signers);
		
		// configure the request object
		request.setRecipients(recipients);
		request.setDocuments(documents);
		request.setEmailSubject("Please sign my document");
		request.setEmailBlurb("This goes in the email body");
		request.setStatus("sent");	// "sent" to send, "created" to save as draft in cloud
		
		File testFile = new File("/Users/lancefallon/Documents/UW_Temp/refs/docusign/sample_pdf.pdf");
		File[] files = new File[]{testFile};
		
		String envelopeId = dsClient.requestSignatureFromDocuments(request, files);
		
		System.out.println("Envelope has been sent, envelopeId = " + envelopeId);
		
		//=======================================================================================================================
		// STEP 3: Request Recipient View API (aka Signing URL)
		//======================================================================================================================= 
		
		String returnUrl = "http://localhost:8080/MyGene2/#/familydashboard";
		String authMethod = "email";
		
		String signingUrl = dsClient.requestRecipientView(envelopeId,  "Lance",  "fallon.lance@gmail.com",  "101",  returnUrl, authMethod);
		
		System.out.println("\nOpen the following URL in an iFrame or Webview:  " + signingUrl);
	}
}